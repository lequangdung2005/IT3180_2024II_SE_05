package com.hust.ittnk68.cnpm.service;

import java.sql.*;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.hust.ittnk68.cnpm.auth.JwtUtil;
import com.hust.ittnk68.cnpm.communication.AdminCreateObject;
import com.hust.ittnk68.cnpm.communication.AdminDeleteObject;
import com.hust.ittnk68.cnpm.communication.AdminFindObject;
import com.hust.ittnk68.cnpm.communication.AdminUpdateObject;
import com.hust.ittnk68.cnpm.communication.ApiMapping;
import com.hust.ittnk68.cnpm.communication.ServerCreateObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerDeleteObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerFindObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerObjectByIdQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerPaymentStatusQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerUpdateObjectResponse;
import com.hust.ittnk68.cnpm.communication.UserQueryObjectById;
import com.hust.ittnk68.cnpm.communication.UserQueryPaymentStatus;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.type.ResponseStatus;
import com.hust.ittnk68.cnpm.security.Token;

@RestController
public class ApiController
{

	@Autowired
	MySQLDatabase mysqlDb;

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
			Account account = jwtUtil.extractAccount (token);
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
}
