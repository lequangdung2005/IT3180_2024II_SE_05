package com.hust.ittnk68.cnpm.view;

import com.hust.ittnk68.cnpm.view.IconButton;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.kordamp.ikonli.material2.Material2RoundAL;
import org.kordamp.ikonli.material2.Material2SharpAL;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.group.AccentToggleGroup;
import com.hust.ittnk68.cnpm.interactor.ClientInteractor;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminHomeSideBar extends BorderPane {

    public AdminHomeSideBar (ClientSceneController sceneController) {
        ClientInteractor interactor = sceneController.getClientInteractor ();

	IconButton btn1 = new IconButton (new FontIcon (Material2OutlinedAL.ACCOUNT_CIRCLE));
	AdminAccountManagePage adminAccountManagePage = new AdminAccountManagePage (sceneController);
	btn1.setOnAction (e -> interactor.setRoot (adminAccountManagePage));

	IconButton btn2 = new IconButton (new FontIcon (Material2RoundAL.FAMILY_RESTROOM));
	AdminFamilyManagePage adminFamilyManagePage = new AdminFamilyManagePage (sceneController);
	btn2.setOnAction (e -> interactor.setRoot (adminFamilyManagePage));

	IconButton btn3 = new IconButton (new FontIcon (Material2MZ.PERSON));
	AdminPersonManagePage adminPersonManagePage = new AdminPersonManagePage (sceneController);
	btn3.setOnAction (e -> interactor.setRoot (adminPersonManagePage));
	
	IconButton btn4 = new IconButton (new FontIcon (Material2AL.ATTACH_MONEY)); 
	AdminExpenseManagePage adminExpenseManagePage = new AdminExpenseManagePage (sceneController);
	btn4.setOnAction (e -> interactor.setRoot (adminExpenseManagePage));

	IconButton btn5 = new IconButton (new FontIcon (Material2MZ.PAYMENTS));
	AdminPaymentStatusManagePage adminPaymentStatusPage = new AdminPaymentStatusManagePage (sceneController);
	btn5.setOnAction (e -> interactor.setRoot (adminPaymentStatusPage));

	// IconButton btn6 = new IconButton (new FontIcon (Material2MZ.SETTINGS));

	AccentToggleGroup accentGroup = new AccentToggleGroup (btn1, btn2, btn3, btn4, btn5);

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

	btn1.fire ();
    }
}
