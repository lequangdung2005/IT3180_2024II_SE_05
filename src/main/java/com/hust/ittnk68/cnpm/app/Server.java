package com.hust.ittnk68.cnpm.app;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.ittnk68.cnpm.communication.ApiMapping;
import com.hust.ittnk68.cnpm.communication.ClientMessageStartSession;
import com.hust.ittnk68.cnpm.communication.ServerResponseStartSession;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.exception.ConfigFileException;
import com.hust.ittnk68.cnpm.exception.UserCreateSecondSession;
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.Family;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.model.Person;
import com.hust.ittnk68.cnpm.session.SessionController;
import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

class UserHomeMetadata {
	public String fullname;
	public String houseNumber;
	public int totalSpendingThisMonth;
	public List<Person> familyPersonList;
}

@RestController
@SpringBootApplication
public class Server {

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

	private static List< Map<String, Object> > queryPaymentStatusFromDatabase(int familyId, int month) {
		try
		{
			return MySQLDatabase.findByCondition(
				String.format("family_id=%d AND DATE_FORMAT(published_date,'%%m')=%d", familyId, month),
				new PaymentStatus()
			);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int getTotalSpending(int familyId, int month) {
		List< Map<String, Object> > list = queryPaymentStatusFromDatabase(familyId, month);
		System.out.println(list);
		int totalSpending = 0;
		for(Map<String, Object> map : list) {
			totalSpending += (int)map.get("total_pay"); 
		}
		return totalSpending;
	}

	private static Map<String, Object> getMetadataById(int id, GetSQLProperties g) {
		try
		{
			List< Map<String, Object> > list = MySQLDatabase.findByCondition(
				String.format("%s_id=%d", g.getSQLTableName(), id),
				g
			);

			System.out.println(list);

			if(list.isEmpty()) {
				return null;
			}
			if(list.size() > 1) {
				// exception ?
			}
			return list.get(0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(ApiMapping.START_SESSION)
	ServerResponseStartSession startSession(@RequestBody ClientMessageStartSession message) {
		String username = message.getUsername ();
		String digestPassword = message.getDigestPassword ();

		Account acc = getAccountByUsernamePassword(username, digestPassword);
		String token = "";

		if(acc != null) {
			System.out.println(username + " " + digestPassword);
			System.out.println(acc.getAccountType());

			try {
				token = SessionController.newSession (username);
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
	void endSession (@RequestBody String token)
	{
		System.out.println ("End session, token = " + token);
		SessionController.endSession (token);
	}

	// @RequestMapping("/api/user-home-metadata")
	// UserHomeMetadata getUserHomeMetadata(@RequestParam String username, @RequestParam String digestPassword) throws SQLException, ConfigFileException, IOException
	// {
	// 	Account acc = getAccountByUsernamePassword(username, digestPassword);
	// 	int thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
	//
	// 	Map<String, Object> familyMetadata = getMetadataById(acc.getFamilyId(),
	// 														new Family());
	//
	// 	int representative_person_id = (int)familyMetadata.get("person_id");
	//
	// 	Map<String, Object> personMetadata = getMetadataById(representative_person_id,
	// 														new Person());
	//
	// 	UserHomeMetadata res = new UserHomeMetadata();
	// 	res.fullname = (String)personMetadata.get("fullname");
	// 	res.houseNumber = (String)familyMetadata.get("house_number");
	// 	res.totalSpendingThisMonth = getTotalSpending(acc.getFamilyId(), thisMonth);
	//
	// 	return res;
	// }

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
