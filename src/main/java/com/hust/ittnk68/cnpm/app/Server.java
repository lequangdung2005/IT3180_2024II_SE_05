package com.hust.ittnk68.cnpm.app;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.database.MySQLDatabaseUtils;
import com.hust.ittnk68.cnpm.exception.ConfigFileException;
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.Family;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.model.Person;

class UserMetadata {
	public String fullname;
	public String houseNumber;
	public int totalSpendingThisMonth;
}

@RestController
@SpringBootApplication
public class Server {

	private Account getAccountByUsernamePassword(String username, String digestPassword) {
		try (
			MySQLDatabase sql = new MySQLDatabase();
		)
		{
			List< Map<String, Object> > res = sql.findByCondition(
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
			Account acc = new Account((int)m.get("family_id"), (String)m.get("username"), (String)m.get("digest_password"));

			acc.setId((int)m.get("account_id"));
			return acc;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private List< Map<String, Object> > queryPaymentStatusFromDatabase(int familyId, int month) {
		try (
			MySQLDatabase sql = new MySQLDatabase();
		)
		{
			return sql.findByCondition(
				String.format("family_id=%d AND DATE_FORMAT(published_date,'%%m')=%d", familyId, month),
				new PaymentStatus()
			);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private int getTotalSpending(int familyId, int month) {
		List< Map<String, Object> > list = queryPaymentStatusFromDatabase(familyId, month);
		System.out.println(list);
		int totalSpending = 0;
		for(Map<String, Object> map : list) {
			totalSpending += (int)map.get("total_pay"); 
		}
		return totalSpending;
	}

	private Map<String, Object> getMetadataById(int id, GetSQLProperties g) {
		try (
			MySQLDatabase sql = new MySQLDatabase();
		)
		{
			List< Map<String, Object> > list = sql.findByCondition(
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

	@RequestMapping("/")
	String hello() {
		return "Hello, World!";
	}

	@RequestMapping("/api/user-metadata")
	UserMetadata getUserMetadata(@RequestParam String username, @RequestParam String digestPassword) throws SQLException, ConfigFileException, IOException
	{
		Account acc = getAccountByUsernamePassword(username, digestPassword);
		int thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

		Map<String, Object> familyMetadata = getMetadataById(acc.getFamilyId(),
															new Family());

		int representative_person_id = (int)familyMetadata.get("person_id");

		Map<String, Object> personMetadata = getMetadataById(representative_person_id,
															new Person());

		UserMetadata res = new UserMetadata();
		res.fullname = (String)personMetadata.get("fullname");
		res.houseNumber = (String)familyMetadata.get("house_number");
		res.totalSpendingThisMonth = getTotalSpending(acc.getFamilyId(), thisMonth);

		return res;
	}

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

}
