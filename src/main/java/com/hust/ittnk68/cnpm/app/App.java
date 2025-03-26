package com.hust.ittnk68.cnpm.app;

import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.Date;
import com.hust.ittnk68.cnpm.type.Nation;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.Sex;

import com.hust.ittnk68.cnpm.model.Person;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;
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

		GetSQLProperties g = new Person ();

		try {

		}
		catch (Exception e) {
			e.printStackTrace ();
		}
		System.out.println (DigestUtils.sha256Hex ("root"));

	} 
}
