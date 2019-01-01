package com.hust.ittnk68.cnpm.app;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hust.ittnk68.cnpm.controller.ClientHomeScreenController;
import com.hust.ittnk68.cnpm.controller.LoginController;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.view.ClientHomeScreenView;
import com.hust.ittnk68.cnpm.model.Person;
import com.hust.ittnk68.cnpm.session.SessionController;
import com.hust.ittnk68.cnpm.session.Token;
import com.hust.ittnk68.cnpm.type.Date;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Client extends Application {
	private static ClientSceneController sceneController;

	public static void main(String[] args) {

		if(args.length != 1 && args.length != 2) {
			throw new InvalidParameterException("So luong tham so khong hop le.");
		} 
		sceneController = new ClientSceneController();
		if(args.length == 1) {
			sceneController.setUriBase(args[0]);
		}
		else if(args.length == 2) {
			sceneController.setUriBase(args[0], args[1]);
		}

		System.out.println(sceneController.getUriBase());

		launch(args);
	}

	@Override
	public void stop(){
		System.out.println("Stage is closing");
		sceneController.endSession ();
	}

	@Override
	public void start(Stage primaryStage) {
		Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
		sceneController.start(primaryStage, "javafx app title", 1200, 700);
	}
} 
