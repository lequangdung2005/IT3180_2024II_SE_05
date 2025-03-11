package com.hust.ittnk68.cnpm.view;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import com.hust.ittnk68.cnpm.model.LoginModel;
import com.hust.ittnk68.cnpm.view.LoginView;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Spacer;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class LoginView extends BorderPane {
    public LoginView(ClientSceneController sceneController, LoginModel model,
	    Runnable loginButtonHandler,
	    Runnable forgetPasswordButtonHandler)
    {
	super();

	TextField usernameTextField = new TextField();
	usernameTextField.setPromptText("Tên đăng nhập");
	model.getUsernameProperty().bind(usernameTextField.textProperty());

	PasswordField passwordField = new PasswordField();
	passwordField.setPromptText("Nhập mật khẩu");
	model.getPasswordProperty().bind(passwordField.textProperty());

	Button forgetPasswordButton = new Button("Quên mật khẩu");
	forgetPasswordButton.getStyleClass().addAll(Styles.FLAT, Styles.ACCENT, Styles.SMALL);
	forgetPasswordButton.setMnemonicParsing(true);
	forgetPasswordButton.setOnAction(evt -> forgetPasswordButtonHandler.run());
	forgetPasswordButton.setCursor(Cursor.HAND);

	HBox forgetPasswordButtonWrapper = new HBox(forgetPasswordButton);
	forgetPasswordButtonWrapper.setAlignment(Pos.BASELINE_RIGHT);

	VBox usernamePassword = new VBox(16, usernameTextField, passwordField);
	VBox usernamePasswordAndForgetPassword = new VBox(usernamePassword, forgetPasswordButtonWrapper); 
	usernamePasswordAndForgetPassword.setMaxWidth(350);
	HBox usernamePasswordAndForgetPasswordWrapper =
	    new HBox(usernamePasswordAndForgetPassword);
	usernamePasswordAndForgetPassword.setAlignment(Pos.CENTER);
	usernamePasswordAndForgetPasswordWrapper.setAlignment(Pos.CENTER);

	//;
	
	Button loginButton = new Button("Đăng nhập");
	loginButton.getStyleClass().addAll(Styles.ACCENT, Styles.SMALL);
	loginButton.setMnemonicParsing(true);
	loginButton.setMinWidth(100);
	loginButton.setCursor(Cursor.HAND);
	loginButton.setOnAction(evt -> loginButtonHandler.run());

	HBox loginButtonWrapper = new HBox(loginButton);
	loginButtonWrapper.setAlignment(Pos.BOTTOM_RIGHT);

	//;
	
	HBox iconWrapper = new HBox(new FontIcon(Material2AL.APARTMENT));
	iconWrapper.setAlignment(Pos.CENTER);

	Label label = new Label("BlueMoon");
	HBox labelWrapper = new HBox(label);
	labelWrapper.setAlignment(Pos.CENTER);

	VBox header = new VBox(6,
	    iconWrapper,
	    labelWrapper,
	    new Separator(Orientation.HORIZONTAL),
	    new Spacer(6, Orientation.VERTICAL),
	    new Label("ĐĂNG NHẬP")
	);
	header.setMinWidth(180);
	header.setMaxWidth(350);

	HBox headerWrapper = new HBox(header);
	headerWrapper.setAlignment(Pos.CENTER);

	//;

	Card card = new Card();
	card.getStyleClass().add(Styles.ELEVATED_1);
	card.getStyleClass().add(Styles.INTERACTIVE);
	card.setMaxWidth(360);
	card.setMaxHeight(380);

	card.setHeader(headerWrapper);
	card.setBody(usernamePasswordAndForgetPasswordWrapper);
	card.setFooter(loginButtonWrapper);

	this.setCenter(card);
    }
}
