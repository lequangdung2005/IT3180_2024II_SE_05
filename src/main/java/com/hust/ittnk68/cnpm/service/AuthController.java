package com.hust.ittnk68.cnpm.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.hust.ittnk68.cnpm.auth.JwtUtil;
import com.hust.ittnk68.cnpm.communication.ApiMapping;
import com.hust.ittnk68.cnpm.communication.ClientMessageStartSession;
import com.hust.ittnk68.cnpm.communication.ServerResponseStartSession;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

@RestController
public class AuthController
{
	@Autowired
	MySQLDatabase mysqlDb;

	@Autowired
	private JwtUtil jwtUtil;

	public Account getAccountByUsername (String username) {
		try
		{
			List< Map<String, Object> > res = mysqlDb.findByCondition(
				String.format("(username='%s')", username),
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
	
	@PreAuthorize ("@authz.canTest()")
	@RequestMapping("/test")
	public String test () throws Exception {
		return this.jwtUtil.generateToken (new Account ()).toString ();
	}

	@RequestMapping("/")
	public String welcome() {
		return "Hello, World!";
	}

	@PreAuthorize ("@authz.canStartSession(#message)")
	@PostMapping(ApiMapping.START_SESSION)
	public ServerResponseStartSession startSession(@RequestBody ClientMessageStartSession message) {
		String username = message.getUsername ();
		String digestPassword = message.getDigestPassword ();
		Account acc = getAccountByUsername(username);
		// password not match
		if (! acc.getDigestPassword ().equals (digestPassword)) {
			acc = null;
		}
		String token = "";
		if(acc != null) {
			System.out.println(username + " " + digestPassword);
			System.out.println(acc.getAccountType());
			try {
				token = jwtUtil.generateToken (acc);
			}
			catch (Exception e) {
				e.printStackTrace ();
				return new ServerResponseStartSession (ResponseStatus.INTERNAL_ERROR,
														"can not generate token",
														null, null);
			}
		}


		return new ServerResponseStartSession (ResponseStatus.OK,
												"no message",
												token,
												((acc == null) ? AccountType.UNVALID : acc.getAccountType()) );
	}

}
