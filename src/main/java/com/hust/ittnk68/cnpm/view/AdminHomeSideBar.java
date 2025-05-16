package com.hust.ittnk68.cnpm.view;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.group.AccentToggleGroup;
import com.hust.ittnk68.cnpm.interactor.ClientInteractor;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.kordamp.ikonli.material2.Material2RoundAL;

import atlantafx.base.theme.Styles;
import javafx.geometry.Pos;
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

	IconButton btn8 = new IconButton (new FontIcon (Material2AL.DIRECTIONS_CAR));
	AdminVehicleManagePage adminVehicleManagePage = new AdminVehicleManagePage(sceneController);
	btn8.setOnAction (e -> interactor.setRoot (adminVehicleManagePage));

	IconButton btn6 = new IconButton (new FontIcon (Material2OutlinedAL.CONTACT_PAGE));
	btn6.setOnAction (e -> interactor.setRoot (new AdminTemporaryStayAbsentView (sceneController)));

	IconButton btn7 = new IconButton (new FontIcon (Material2OutlinedAL.AUTORENEW));
	btn7.setOnAction (e -> interactor.setRoot (new AdminToolView(sceneController)));

	new AccentToggleGroup (btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8);

	VBox groupWrapper = new VBox (btn1, btn2, btn3, btn4, btn5, btn8);
	groupWrapper.setAlignment (Pos.CENTER);
	groupWrapper.getStyleClass().addAll("graywrapper", "updownpadding");

	VBox groupWrapper2 = new VBox (btn6, btn7);
	groupWrapper2.setAlignment (Pos.CENTER);
	groupWrapper2.getStyleClass().addAll("graywrapper", "updownpadding");

	this.setTop (new VBox(15,
	    groupWrapper,
	    groupWrapper2
	));
	
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
