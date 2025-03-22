package com.hust.ittnk68.cnpm.app;

import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.Date;
import com.hust.ittnk68.cnpm.type.Nation;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.Sex;

import com.hust.ittnk68.cnpm.model.Person;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import com.hust.ittnk68.cnpm.config.MySQLDefaultConfig;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.exception.ConfigFileException;
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.Family;
import com.fasterxml.jackson.databind.ObjectMapper;

// import javafx.util.*;
// import javafx.application.*;
// import javafx.event.ActionEvent;
// import javafx.event.EventHandler;
// import javafx.scene.*;
// import javafx.scene.control.*;
// import javafx.scene.layout.*;
// import javafx.scene.text.*;
// import javafx.scene.paint.*;
// import javafx.scene.shape.*;
// import javafx.scene.effect.*;
// import javafx.stage.*;
// import javafx.geometry.*;
// import javafx.animation.*;

import com.hust.ittnk68.cnpm.communication.*;

public class App {
	public static void main(String[] args) throws NoSuchFileException, IOException, ConfigFileException {
		int familyId;
		String username;
		String password;
		String type;

		Scanner scanner = new Scanner (System.in);

		try {
			familyId = scanner.nextInt ();
			username = scanner.next ();
			password = scanner.next ();
			type = scanner.next ();
		}
		catch (Exception e) {
			scanner.close ();
			return;
		}

		Account acc = new Account (familyId, username, password, null, AccountType.matchByString (type).get());

		AdminCreateAccount req = new AdminCreateAccount ("not check yet", acc);

		ObjectMapper mapper = new ObjectMapper();

		String jsonString = mapper.writeValueAsString (req.getAccount());
		System.out.println (jsonString);

		System.out.println ("Do you want to continue? [y/n]");

		String option;
		try { 
			option = scanner.next();
		}
		catch (Exception e) {
			scanner.close ();
			return;
		}

		if (!option.equals ("y")) {
			scanner.close ();
			return;
		}
		scanner.close ();


		RestClient restClient = RestClient.create (); 
		ServerResponseBase res =  restClient.post()
									.uri ("http://127.0.0.1:8080/" + ApiMapping.CREATE_ACCOUNT)
									.body (req)
									.retrieve ()
									.body (ServerResponseBase.class);

		System.out.println (res.getResponseStatus() + " " + res.getResponseMessage());
	} 
}
