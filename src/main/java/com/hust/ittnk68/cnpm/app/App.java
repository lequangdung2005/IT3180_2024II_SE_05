package com.hust.ittnk68.cnpm.app;

import com.hust.ittnk68.cnpm.type.Date;
import com.hust.ittnk68.cnpm.type.Nation;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.Sex;

import com.hust.ittnk68.cnpm.model.Person;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import com.hust.ittnk68.cnpm.config.MySQLDefaultConfig;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.exception.ConfigFileException;
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.Family;

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
		RestClient restClient = RestClient.create ();
		ServerResponseStartSession res = restClient.post()
			.uri ("http://127.0.0.1:8080" + ApiMapping.START_SESSION)
			.body (new ClientMessageStartSession ("abcd", "efgh"))
			.retrieve ()
			.body (ServerResponseStartSession.class);
	}
}
