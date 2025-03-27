package com.hust.ittnk68.cnpm.app;

import java.security.InvalidParameterException;
import java.util.Locale;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;

import atlantafx.base.theme.PrimerLight;

import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.im.InputContext;

public class Client extends Application {
	private static ClientSceneController sceneController;

	public static void main(String[] args) {

		InputContext.getInstance();
		Locale.setDefault (Locale.forLanguageTag ("vi"));

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
