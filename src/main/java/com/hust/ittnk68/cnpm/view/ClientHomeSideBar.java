package com.hust.ittnk68.cnpm.view;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.group.AccentToggleGroup;
import com.hust.ittnk68.cnpm.interactor.ClientInteractor;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2RoundAL;
import org.kordamp.ikonli.material2.Material2SharpAL;

import atlantafx.base.theme.Styles;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ClientHomeSideBar extends BorderPane {

    public ClientHomeSideBar (ClientSceneController sceneController) {
        super ();

        ClientInteractor interactor = sceneController.getClientInteractor ();

	IconButton btn1 = new IconButton (new FontIcon (Material2AL.HOME));
	btn1.setOnAction (e -> interactor.setRoot (new ClientHomePage (sceneController)));

	IconButton btn2 = new IconButton (new FontIcon (Material2RoundAL.FAMILY_RESTROOM));
	btn2.setOnAction (e -> {});

	IconButton btn3 = new IconButton (new FontIcon (Material2SharpAL.DIRECTIONS_CAR));
	btn3.setOnAction (e -> {});

	IconButton btn4 = new IconButton (new FontIcon (Material2AL.ATTACH_MONEY)); 
	btn4.setOnAction (e -> interactor.setRoot (new ClientExpensePage (sceneController)));

	IconButton btn5 = new IconButton (new FontIcon (Material2AL.FAVORITE));
	btn5.setOnAction (e -> interactor.setRoot (new ClientDonationPage (sceneController)));

	AccentToggleGroup accentGroup = new AccentToggleGroup (btn1, btn2, btn3, btn4, btn5);
	accentGroup.setActive (btn1);
	interactor.setRoot (new ClientHomePage (sceneController));

	VBox groupWrapper = new VBox (btn1, btn2, btn3, btn4, btn5);
	groupWrapper.setAlignment (Pos.CENTER);
	groupWrapper.getStyleClass().addAll("graywrapper", "updownpadding");

	this.setTop (groupWrapper);
	
	IconButton logoutBtn = new IconButton (new FontIcon(Material2AL.LOG_OUT));
	logoutBtn.getStyleClass ().add(Styles.DANGER);
	logoutBtn.setOnAction (e -> interactor.logout ());

	HBox logoutWrapper = new HBox (logoutBtn);
	logoutWrapper.getStyleClass().addAll("graywrapper");
	logoutWrapper.setAlignment (Pos.CENTER);;

	this.setBottom (logoutWrapper);
    }
}
