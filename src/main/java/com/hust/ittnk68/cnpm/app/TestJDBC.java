package com.hust.ittnk68.cnpm.app;

import java.util.*;
import java.io.*;
import java.sql.*;

import javax.sql.*;

import javafx.util.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.effect.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.animation.*;


public class TestJDBC {
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String url = "jdbc:mysql://127.0.0.1:3306/testjdbc";
			String username = "cnpm";
			String password = "cnpm";

			Connection con = DriverManager.getConnection(url, username, password);

			if(con.isClosed()) {
				System.out.println("Connection is closed");
			}
			else {
				System.out.println("Connection created...");
			}

			// create table
			// String q = "create table table1(tId int(20) primary key auto_increment, tName varchar(200) not null, tCity varchar(400))";
			// Statement stmt = con.createStatement();
			// stmt.executeUpdate(q);

			// insert data
			// String q = "insert into table1(tName, tCity) values (?,?)";
			// PreparedStatement pstmt = con.prepareStatement(q);
			// pstmt.setString(1, "Durgesh Tiwari");
			// pstmt.setString(2, "Ha Noi");
			// pstmt.executeUpdate();

			// String q = "insert into table1(tName, tCity) values (?,?)";
			// PreparedStatement pstmt = con.prepareStatement(q);
			// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			// System.out.println("Enter name:");
			// String name = br.readLine();
			// System.out.println("Enter city:");
			// String city = br.readLine();
			// pstmt.setString(1, name);
			// pstmt.setString(2, city);
			// pstmt.executeUpdate();
			
			String q = "select * from table1";
			Statement stmt = con.createStatement();
			ResultSet s = stmt.executeQuery(q);

			while(s.next()) {
				System.out.printf("row: %d %s %s\n", s.getInt(1), s.getString(2), s.getString(3));
			}

			con.close();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

