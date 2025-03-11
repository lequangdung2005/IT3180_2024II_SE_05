package com.hust.ittnk68.cnpm.app;

import com.hust.ittnk68.cnpm.type.Date;
import com.hust.ittnk68.cnpm.type.Nation;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.Sex;

import com.hust.ittnk68.cnpm.model.Person;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

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

public class App {
	public static void main(String[] args) throws NoSuchFileException, IOException, ConfigFileException {

		// Person p1 = new Person(69, "ND Dang Duong", new Date(2005, 12, 3), "010039931183", "0912375999", Sex.MALE, Nation.VIETNAM, ResidenceStatus.PRESENT);
		// Person p2 = new Person(69, "Phuc Du", new Date(1997, 3, 06), "010039931183", "0912375999", Sex.MALE, Nation.USA, ResidenceStatus.ABSENT);
		// Person p3 = new Person(69, "Ablaham", new Date(1977, 9, 15), "010039931183", "0912375999", Sex.FEMALE, Nation.INDIA, ResidenceStatus.PRESENT);
		// Person p4 = new Person(1, "Ablaham", new Date(1977, 9, 15), "010039931183", "0912375999", Sex.FEMALE, Nation.INDIA, ResidenceStatus.PRESENT);
		// Person p5 = new Person(2, "Ablaham", new Date(1977, 9, 15), "010039931183", "0912375999", Sex.FEMALE, Nation.INDIA, ResidenceStatus.PRESENT);
		// Person p6 = new Person(33, "Ablaham", new Date(1977, 9, 15), "010039931183", "0912375999", Sex.FEMALE, Nation.INDIA, ResidenceStatus.PRESENT);
		//
		//
		// try {
		// 	sql.create(p1);
		// 	sql.create(p2);
		// 	sql.create(p3);
		// 	sql.create(p4);
		// 	sql.create(p5);
		// 	sql.create(p6);
		// }
		// catch (Exception e) {
		// 	e.printStackTrace();
		// }
		//
		// Family f = new Family(2, "A35");
		//
		// try {
		// 	sql.create(f);
		// }
		// catch (Exception e) {
		// 	e.printStackTrace();
		// }
		//
		// Account acc = new Account(1, "root", "tomoshibi");
		//
		// try {
		// 	int id = sql.create(acc);
		// 	System.out.printf("insert id = %d\n", id);
		// }
		// catch (Exception e) {
		// 	e.printStackTrace();
		// }
	} 
}
