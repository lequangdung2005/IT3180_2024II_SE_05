package com.hust.ittnk68.cnpm.service;

import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hust.ittnk68.cnpm.auth.JwtUtil;
import com.hust.ittnk68.cnpm.communication.ApiMapping;
import com.hust.ittnk68.cnpm.communication.PaymentRequest;
import com.hust.ittnk68.cnpm.communication.PaymentResponse;
import com.hust.ittnk68.cnpm.communication.ServerCheckBankingResponse;
import com.hust.ittnk68.cnpm.communication.ServerQrCodeGenerateResponse;
import com.hust.ittnk68.cnpm.communication.UserGetPaymentQrCode;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.model.Expense;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.security.AuthorizationService;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController
{

	@Autowired
	private BankingTokenRepository bankingTokenRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private MySQLDatabase mysqlDb;

	@Autowired
	private AuthorizationService authz;

	@Autowired
	private QrCodeService qrCodeService;

	private Map<String, Object> format(Map<String, Object> input) {
        try {
			input.replaceAll((k, v) -> (v instanceof java.sql.Date) ? v.toString() : v);
			ObjectMapper objectMapper = new ObjectMapper ();
            String json = objectMapper.writeValueAsString(input);
			System.out.println (json);
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
			e.printStackTrace ();
            throw new RuntimeException("Failed to format map", e);
        }
    }

	public PaymentResponse pay (PaymentRequest req)
	{
		try {

			List< Map<String, Object> > res;

			PaymentStatus ps = new PaymentStatus ();
			ps.setId (req.getPaymentStatusId ());
			res = mysqlDb.findById (ps);
			if (res.size () == 0) {
				return new PaymentResponse (ResponseStatus.ILLEGAL_OPERATION, "no such payment status");
			}
			else if (res.size () != 1) {
				return new PaymentResponse (ResponseStatus.INTERNAL_ERROR, "query by id give more than 1 ...");
			}

			ps = PaymentStatus.convertFromMap ( format(res.get (0)) );

			Expense ex = new Expense ();
			ex.setId (ps.getExpenseId ());
			res = mysqlDb.findById (ex);
			if (res.size () == 0) {
				return new PaymentResponse (ResponseStatus.ILLEGAL_OPERATION, "no such expense of payment status");
			}
			else if (res.size () != 1) {
				return new PaymentResponse (ResponseStatus.INTERNAL_ERROR, "query by id give more than 1 ...");
			}

			ex = Expense.convertFromMap ( format(res.get (0)) );

			// check authorization
			if (! authz.canPay (req, ps, ex)) {
				return new PaymentResponse (ResponseStatus.PERMISSION_ERROR, "not have permission");
			}

			if (ps.getTotalPay () + req.getAmount () > ex.getTotalCost ())
			{
				return new PaymentResponse (ResponseStatus.ILLEGAL_OPERATION, "total payed exceed total cost");
			}

			System.out.println ("amount=" + String.valueOf(req.getAmount()));
			System.out.println ("debug=" + String.valueOf(ps.getTotalPay()));
			ps.setTotalPay (ps.getTotalPay () + req.getAmount ());
			System.out.println ("new amount=" + String.valueOf (ps.getTotalPay ()));

			if (mysqlDb.singleUpdate (ps) != 1)
			{
				return new PaymentResponse (ResponseStatus.INTERNAL_ERROR, "update affected row not equal 1!");
			}

			return new PaymentResponse (ResponseStatus.OK, "pay successfully");

		}
		catch (SQLException e) {
			e.printStackTrace();
			return new PaymentResponse (ResponseStatus.SQL_ERROR, e.toString());
		}
	}

	@PreAuthorize("@authz.canGetPaymentQrCode(#req)")
	@RequestMapping(ApiMapping.QR_CODE_GENERATOR)
	public ServerQrCodeGenerateResponse getPaymentQrCode (@RequestBody UserGetPaymentQrCode req)
	{
		try {
			String token = jwtUtil.generateToken (req.getPaymentRequest ());

			String serverUrl = req.getServerUrl ();

			String text = serverUrl
				+ ApiMapping.BANK_TRANSFER.substring (serverUrl.charAt(serverUrl.length() - 1) == '/' ? 1 : 0)
				+ "?token=" + URLEncoder.encode(token, "UTF-8");

			byte[] qrCodeData = qrCodeService.generateQrCodeImage(text, req.getWidth (), req.getHeight ());
			System.out.printf ("returned url = %s\n", text);
			return new ServerQrCodeGenerateResponse (ResponseStatus.OK, "success", token, qrCodeData);
		}
		catch (Exception e)
		{
			return new ServerQrCodeGenerateResponse (ResponseStatus.INTERNAL_ERROR, e.toString (), null, null);
		}
	}

	@GetMapping(ApiMapping.BANK_TRANSFER)
	public String bankTransfer (@RequestParam(required=true) String token)
	{
		System.out.println ("Bank transfered with token=" + token);
		return bankingTokenRepository.tokenArrive (token) ? "Success" : "Already transfered";
	}
	@GetMapping(ApiMapping.CHECK_BANKING)
	public ServerCheckBankingResponse checkBanking (@RequestParam(required=true) String token)
	{
		if (! bankingTokenRepository.resolveToken (token)) {
			return new ServerCheckBankingResponse (ResponseStatus.OK, "Have not payed yet", false);
		}

		try {
			PaymentRequest pr = jwtUtil.extract (token, PaymentRequest.class);
			PaymentResponse res = pay (pr);
			return new ServerCheckBankingResponse (res.getResponseStatus(), res.getResponseMessage(), true);
		}
		catch (Exception e)
		{
			return new ServerCheckBankingResponse (ResponseStatus.INTERNAL_ERROR, e.toString (), true);
		}
	}

}
