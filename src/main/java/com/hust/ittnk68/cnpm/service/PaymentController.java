package com.hust.ittnk68.cnpm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.hust.ittnk68.cnpm.communication.ApiMapping;
import com.hust.ittnk68.cnpm.communication.PaymentRequest;
import com.hust.ittnk68.cnpm.communication.PaymentResponse;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.model.Expense;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.security.AuthorizationService;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

@RestController
public class PaymentController
{
	@Autowired
	private MySQLDatabase mysqlDb;

	@Autowired
	private AuthorizationService authz;

	@RequestMapping(ApiMapping.PAYMENT)
	public PaymentResponse pay (@RequestBody PaymentRequest req)
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

			ps = PaymentStatus.convertFromMap (res.get (0));

			Expense ex = new Expense ();
			ex.setId (ps.getExpenseId ());
			res = mysqlDb.findById (ex);
			if (res.size () == 0) {
				return new PaymentResponse (ResponseStatus.ILLEGAL_OPERATION, "no such expense of payment status");
			}
			else if (res.size () != 1) {
				return new PaymentResponse (ResponseStatus.INTERNAL_ERROR, "query by id give more than 1 ...");
			}

			ex = Expense.convertFromMap (res.get (0));

			// check authorization
			if (! authz.canPay (req, ps, ex)) {
				return new PaymentResponse (ResponseStatus.PERMISSION_ERROR, "not have permission");
			}

			if (ps.getTotalPay () + req.getAmount () > ex.getTotalCost ())
			{
				return new PaymentResponse (ResponseStatus.ILLEGAL_OPERATION, "total payed exceed total cost");
			}

			ps.setTotalPay (ps.getTotalPay () + req.getAmount ());
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

}

