package com.hust.ittnk68.cnpm.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.hust.ittnk68.cnpm.auth.JwtUtil;
import com.hust.ittnk68.cnpm.communication.AdminCreateObject;
import com.hust.ittnk68.cnpm.communication.AdminDeleteObject;
import com.hust.ittnk68.cnpm.communication.AdminFindObject;
import com.hust.ittnk68.cnpm.communication.AdminUpdateObject;
import com.hust.ittnk68.cnpm.communication.ApiMapping;
import com.hust.ittnk68.cnpm.communication.ClientMessageBase;
import com.hust.ittnk68.cnpm.communication.PostTemporaryStayAbsentRequest;
import com.hust.ittnk68.cnpm.communication.ServerCreateObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerDeleteObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerFindObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerObjectByIdQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerPaymentStatusQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerQueryPersonByFIdResponse;
import com.hust.ittnk68.cnpm.communication.ServerResponseBase;
import com.hust.ittnk68.cnpm.communication.ServerResponseTemporaryStayAbsentRequest;
import com.hust.ittnk68.cnpm.communication.ServerUpdateObjectResponse;
import com.hust.ittnk68.cnpm.communication.UserQueryObjectById;
import com.hust.ittnk68.cnpm.communication.UserQueryPaymentStatus;
import com.hust.ittnk68.cnpm.communication.UserQueryPersonByFId;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.model.Person;
import com.hust.ittnk68.cnpm.model.TemporaryStayAbsentRequest;
import com.hust.ittnk68.cnpm.security.Token;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController
{

	@Autowired
	private MySQLDatabase mysqlDb;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private Token tokenGetter;

	@PreAuthorize ("@authz.canQueryObjectById(#req)")
	@RequestMapping(ApiMapping.QUERY_OBJECT_BY_ID)
	public ServerObjectByIdQueryResponse queryObjectById (@RequestBody UserQueryObjectById req) {
		try {
			GetSQLProperties g = req.getObject();
			List<Map<String, Object>> res = mysqlDb.findByCondition (String.format("%s_id='%d'", g.getSQLTableName(), g.getId()),
																			req.getObject());
			return new ServerObjectByIdQueryResponse (ResponseStatus.OK, "succeed", res);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return new ServerObjectByIdQueryResponse (ResponseStatus.SQL_ERROR, e.toString());
		}
	}

	@PreAuthorize ("@authz.canCreateObject(#req)")
	@RequestMapping(ApiMapping.CREATE_OBJECT)
	public ServerCreateObjectResponse createObject (@RequestBody AdminCreateObject req) {
		try {
			mysqlDb.create (req.getObject());
			return new ServerCreateObjectResponse (ResponseStatus.OK, "succeed", req.getObject()); 
		}
		catch (SQLException e) {
			e.printStackTrace ();
			return new ServerCreateObjectResponse (ResponseStatus.SQL_ERROR, e.toString ());
		}
	}

	@PreAuthorize ("@authz.canFindObject(#req)")
	@RequestMapping(ApiMapping.FIND_OBJECT)
	public ServerFindObjectResponse findObject (@RequestBody AdminFindObject req) {
		System.out.printf ("i'm hererhewfjweslfjksdlfjdsl\n");
		try {
			List<Map<String, Object>> res = mysqlDb.findByCondition (req.getCondition(), req.getObject());
			return new ServerFindObjectResponse (ResponseStatus.OK, "succeed", res);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return new ServerFindObjectResponse (ResponseStatus.SQL_ERROR, e.toString());
		}
	}

	@PreAuthorize ("@authz.canDeleteObject(#req)")
	@RequestMapping(ApiMapping.DELETE_OBJECT)
	public ServerDeleteObjectResponse deleteObject (@RequestBody AdminDeleteObject req) {
		try {
			int affectedRows = mysqlDb.deleteByCondition (req.getCondition(), req.getObject());
			return new ServerDeleteObjectResponse (ResponseStatus.OK, "succeed", affectedRows);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return new ServerDeleteObjectResponse (ResponseStatus.SQL_ERROR, e.toString());
		}
	}

	@PreAuthorize ("@authz.canUpdateObject(#req)")
	@RequestMapping(ApiMapping.UPDATE_OBJECT)
	public ServerUpdateObjectResponse updateObject (@RequestBody AdminUpdateObject req) {
		try {
			int affectedRows = mysqlDb.singleUpdate (req.getObject());
			return new ServerUpdateObjectResponse (ResponseStatus.OK, "succeed", affectedRows);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return new ServerUpdateObjectResponse (ResponseStatus.SQL_ERROR, e.toString());
		}
	}

	@PreAuthorize ("@authz.canQueryPaymentStatus(#req)")
	@RequestMapping(ApiMapping.QUERY_FAMILY_PAYMENT_STATUS)
	public ServerPaymentStatusQueryResponse queryPaymentStatus (@RequestBody UserQueryPaymentStatus req) {
		try {
			String token = tokenGetter.get ();
			System.out.printf ("token=%s\n", token);
			Account account = jwtUtil.extract (token, Account.class);
			StringBuilder conditionBuilder = new StringBuilder ( String.format("family_id='%d'", account.getFamilyId()) );
			String condition = conditionBuilder.toString ();
			List<Map<String, Object>> query = mysqlDb.findByCondition (condition, new PaymentStatus());
			return new ServerPaymentStatusQueryResponse(ResponseStatus.OK, "", query);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return new ServerPaymentStatusQueryResponse(ResponseStatus.SQL_ERROR, e.toString());
		}
		catch (InsufficientAuthenticationException e)
		{
			e.printStackTrace ();
			return new ServerPaymentStatusQueryResponse(ResponseStatus.INTERNAL_ERROR, e.toString());
		}
	}

	@PreAuthorize ("@authz.canQueryPersonByFamilyId(#req)")
	@RequestMapping(ApiMapping.QUERY_PERSON_BY_FID)
	public ServerQueryPersonByFIdResponse queryPersonByFamilyId (@RequestBody UserQueryPersonByFId req)
	{
		int family_id = jwtUtil.extract (tokenGetter.get (), Account.class) .getFamilyId ();
		try {
			List<Map<String, Object>> res = mysqlDb.findByCondition (
					String.format ("family_id='%d'", family_id), 
					new Person());
			return new ServerQueryPersonByFIdResponse(ResponseStatus.OK, "okay", res);
		}
		catch (SQLException e) {
			return new ServerQueryPersonByFIdResponse(ResponseStatus.SQL_ERROR, e.toString (), null);
		}
		catch (Exception e) {
			return new ServerQueryPersonByFIdResponse(ResponseStatus.INTERNAL_ERROR, e.toString (), null);
		}
	}

	@PreAuthorize("@authz.canPostTemporaryStayAbsentRequest(#req)")
	@RequestMapping(ApiMapping.POST_TEMPORARY_STAY_ABSENT_REQUEST)
	public ServerResponseBase postTemporaryStayAbsentRequest (@RequestBody PostTemporaryStayAbsentRequest req)
	{
		try {
			String token = tokenGetter.get ();
			Account acc = jwtUtil.extract (token, Account.class);
			req.getTemporaryStayAbsentRequest().setFamilyId (acc.getFamilyId ());
			mysqlDb.create (req.getTemporaryStayAbsentRequest ());
			return new ServerResponseBase (ResponseStatus.OK, "success");
		}
		catch (Exception e)
		{
			return new ServerResponseBase (ResponseStatus.INTERNAL_ERROR, e.toString ());
		}
	}

	@PreAuthorize("@authz.canQueryTemporaryStayAbsentRequest(#req)")
	@RequestMapping(ApiMapping.QUERY_TEMPORARY_STAY_ABSENT_REQUEST)
	public ServerResponseTemporaryStayAbsentRequest queryTemporaryStayAbsentRequest (@RequestBody ClientMessageBase req)
	{
		try {
			String token = tokenGetter.get ();
			Account acc = jwtUtil.extract (token, Account.class);
			return new ServerResponseTemporaryStayAbsentRequest(ResponseStatus.OK, "success",
					mysqlDb.findByCondition(String.format ("family_id=%d", acc.getFamilyId()), new TemporaryStayAbsentRequest())
				);
		}
		catch (Exception e)
		{
			return new ServerResponseTemporaryStayAbsentRequest (ResponseStatus.INTERNAL_ERROR, e.toString (), null);
		}
	}

	@PreAuthorize("@authz.canDeleteTemporaryStayAbsentRequest(#req)")
	@RequestMapping(ApiMapping.DELETE_TEMPORARY_STAY_ABSENT_REQUEST)
	public ServerResponseBase deleteTemporaryStayAbsentRequest (@RequestBody PostTemporaryStayAbsentRequest req)
	{
		try {
			System.out.printf ("debug at server: %d\n", req.getTemporaryStayAbsentRequest().getId ());
			System.out.println (req.getTemporaryStayAbsentRequest ().getId ());
			mysqlDb.deleteByCondition(String.format("temporary_stay_absent_request_id=%d", req.getTemporaryStayAbsentRequest().getId()),
					new TemporaryStayAbsentRequest());
			return new ServerResponseBase(ResponseStatus.OK, "Success");
		}
		catch (Exception e)
		{
			return new ServerResponseTemporaryStayAbsentRequest (ResponseStatus.INTERNAL_ERROR, e.toString (), null);
		}
	}

}
