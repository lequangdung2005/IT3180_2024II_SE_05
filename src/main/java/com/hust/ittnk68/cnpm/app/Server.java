package com.hust.ittnk68.cnpm.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hust.ittnk68.cnpm.communication.AdminCreateObject;
import com.hust.ittnk68.cnpm.communication.AdminDeleteObject;
import com.hust.ittnk68.cnpm.communication.AdminFindObject;
import com.hust.ittnk68.cnpm.communication.AdminUpdateObject;
import com.hust.ittnk68.cnpm.communication.ApiMapping;
import com.hust.ittnk68.cnpm.communication.ClientMessageStartSession;
import com.hust.ittnk68.cnpm.communication.ServerCreateObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerDeleteObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerFindObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerObjectByIdQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerPaymentStatusQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerResponseStartSession;
import com.hust.ittnk68.cnpm.communication.ServerUpdateObjectResponse;
import com.hust.ittnk68.cnpm.communication.UserQueryObjectById;
import com.hust.ittnk68.cnpm.communication.UserQueryPaymentStatus;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.exception.UserCreateSecondSession;
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.Expense;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.model.Person;
import com.hust.ittnk68.cnpm.session.Session;
import com.hust.ittnk68.cnpm.session.SessionController;
import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

@RestController
@SpringBootApplication
public class Server {

	private boolean checkPrivilegeAdminAbove (Session session) {
		if (!session.getAccount().getAccountType().equals(AccountType.ROOT)
			&& !session.getAccount().getAccountType().equals(AccountType.ADMIN))
			return false;
		return true;
	}

	private boolean checkObjectUserCanQuery (GetSQLProperties g) {
		// if (g instanceof Expense) {
		// 	return true;
		// }
		// return false;
		return true;
	}

	private static Account getAccountByUsernamePassword(String username, String digestPassword) {
		try
		{
			List< Map<String, Object> > res = MySQLDatabase.findByCondition(
				String.format("(username='%s' and digest_password='%s')", username, digestPassword),
				new Account()
			); 
			System.out.println(res);

			if(res.isEmpty()) {
				return null;
			}
			if(res.size() > 1) {
				// return exceptions ?
			}

			assert res.size() == 1;

			Map<String, Object> m = res.get(0);
			AccountType accountType = AccountType.matchByString((String)m.get("account_type")).get();
			Account acc = new Account((int)m.get("family_id"), (String)m.get("username"), null, (String)m.get("digest_password"), accountType );

			acc.setId((int)m.get("account_id"));
			return acc;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping("/")
	private String welcome() {
		return "Hello, World!";
	}

	@RequestMapping(ApiMapping.QUERY_OBJECT_BY_ID)
	private ServerObjectByIdQueryResponse queryObjectById (@RequestBody UserQueryObjectById req) {
		String token = req.getToken ();

		Session session = SessionController.getSession (token);
		if (session == null)
			return new ServerObjectByIdQueryResponse (ResponseStatus.SESSION_ERROR, "token is invalid or is expired");
		
		if (!checkObjectUserCanQuery (req.getObject ()))
			return new ServerObjectByIdQueryResponse (ResponseStatus.PERMISSION_ERROR, "you don't have permission to do this...");

		try {
			GetSQLProperties g = req.getObject();
			List<Map<String, Object>> res = MySQLDatabase.findByCondition (String.format("%s_id='%d'", g.getSQLTableName(), g.getId()),
																			req.getObject());
			return new ServerObjectByIdQueryResponse (ResponseStatus.OK, "succeed", res);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return new ServerObjectByIdQueryResponse (ResponseStatus.SQL_ERROR, e.toString());
		}
	}

	@RequestMapping(ApiMapping.START_SESSION)
	private ServerResponseStartSession startSession(@RequestBody ClientMessageStartSession message) {
		String username = message.getUsername ();
		String digestPassword = message.getDigestPassword ();

		Account acc = getAccountByUsernamePassword(username, digestPassword);
		String token = "";

		if(acc != null) {
			System.out.println(username + " " + digestPassword);
			System.out.println(acc.getAccountType());

			try {
				token = SessionController.newSession (acc);
			}
			catch (UserCreateSecondSession e)
			{
				return new ServerResponseStartSession (ResponseStatus.SESSION_ERROR, "session-error", "", AccountType.UNVALID);
			}

		}


		return new ServerResponseStartSession (ResponseStatus.OK,
												"no message",
												token,
												((acc == null) ? AccountType.UNVALID : acc.getAccountType())
												);
	}

	@RequestMapping(ApiMapping.END_SESSION)
	private void endSession (@RequestBody String token)
	{
		System.out.println ("End session, token = " + token);
		SessionController.endSession (token);
	}

	@RequestMapping(ApiMapping.CREATE_OBJECT)
	private ServerCreateObjectResponse createObject (@RequestBody AdminCreateObject req) {
		String token = req.getToken ();

		Session session = SessionController.getSession (token);
		if (session == null)
			return new ServerCreateObjectResponse (ResponseStatus.SESSION_ERROR, "token is invalid or is expired");
		if (!checkPrivilegeAdminAbove (session))
			return new ServerCreateObjectResponse (ResponseStatus.PERMISSION_ERROR, "you don't have permission to do this...");

		try {
			MySQLDatabase.create (req.getObject());
			return new ServerCreateObjectResponse (ResponseStatus.OK, "succeed", req.getObject()); 
		}
		catch (SQLException e) {
			e.printStackTrace ();
			return new ServerCreateObjectResponse (ResponseStatus.SQL_ERROR, e.toString ());
		}
	}

	@RequestMapping(ApiMapping.FIND_OBJECT)
	private ServerFindObjectResponse findObject (@RequestBody AdminFindObject req) {
		String token = req.getToken ();

		Session session = SessionController.getSession (token);
		if (session == null)
			return new ServerFindObjectResponse (ResponseStatus.SESSION_ERROR, "token is invalid or is expired");
		if (!checkPrivilegeAdminAbove (session))
			return new ServerFindObjectResponse (ResponseStatus.PERMISSION_ERROR, "you don't have permission to do this...");

		try {
			List<Map<String, Object>> res = MySQLDatabase.findByCondition (req.getCondition(), req.getObject());
			return new ServerFindObjectResponse (ResponseStatus.OK, "succeed", res);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return new ServerFindObjectResponse (ResponseStatus.SQL_ERROR, e.toString());
		}
	}

	@RequestMapping(ApiMapping.DELETE_OBJECT)
	private ServerDeleteObjectResponse deleteObject (@RequestBody AdminDeleteObject req) {
		String token = req.getToken ();

		Session session = SessionController.getSession (token);
		if (session == null)
			return new ServerDeleteObjectResponse (ResponseStatus.SESSION_ERROR, "token is invalid or is expired");
		if (!checkPrivilegeAdminAbove (session))
			return new ServerDeleteObjectResponse (ResponseStatus.PERMISSION_ERROR, "you don't have permission to do this...");

		try {
			int affectedRows = MySQLDatabase.deleteByCondition (req.getCondition(), req.getObject());
			return new ServerDeleteObjectResponse (ResponseStatus.OK, "succeed", affectedRows);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return new ServerDeleteObjectResponse (ResponseStatus.SQL_ERROR, e.toString());
		}
	}

	@RequestMapping(ApiMapping.UPDATE_OBJECT)
	private ServerUpdateObjectResponse updateObject (@RequestBody AdminUpdateObject req) {
		String token = req.getToken ();

		Session session = SessionController.getSession (token);
		if (session == null)
			return new ServerUpdateObjectResponse (ResponseStatus.SESSION_ERROR, "token is invalid or is expired");
		if (!checkPrivilegeAdminAbove (session))
			return new ServerUpdateObjectResponse (ResponseStatus.PERMISSION_ERROR, "you don't have permission to do this...");

		try {
			int affectedRows = MySQLDatabase.singleUpdate (req.getObject());
			return new ServerUpdateObjectResponse (ResponseStatus.OK, "succeed", affectedRows);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return new ServerUpdateObjectResponse (ResponseStatus.SQL_ERROR, e.toString());
		}
	}

	@RequestMapping(ApiMapping.QUERY_FAMILY_PAYMENT_STATUS)
	private ServerPaymentStatusQueryResponse queryPaymentStatus (@RequestBody UserQueryPaymentStatus request) {
		String token = request.getToken ();

		Session session = SessionController.getSession (token); 
		if (session == null)
			return new ServerPaymentStatusQueryResponse(ResponseStatus.SESSION_ERROR, "token is invalid or is expired");

		try {
			StringBuilder conditionBuilder = new StringBuilder ( String.format("family_id='%d'", session.getAccount().getFamilyId()) );
			String condition = conditionBuilder.toString ();
			List<Map<String, Object>> query = MySQLDatabase.findByCondition (condition, new PaymentStatus());
			return new ServerPaymentStatusQueryResponse(ResponseStatus.OK, "", query);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return new ServerPaymentStatusQueryResponse(ResponseStatus.SQL_ERROR, e.toString());
		}
	}

	@EventListener({ ContextClosedEvent.class })
	void exitGracefully() {
		MySQLDatabase.close(); 
	}

	public static void main(String[] args) {
		try {
			MySQLDatabase.start();
			SpringApplication.run(Server.class, args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
