package com.hust.ittnk68.cnpm.interactor;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.controller.LoginController;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class ClientHomeScreenInteractor {
    ClientSceneController sceneController;
    AnchorPane mainScreen;

    public ClientHomeScreenInteractor (ClientSceneController sceneController) {
        this.sceneController = sceneController;
        this.mainScreen = new AnchorPane ();
    }

    public void logout () {
        sceneController.endSession ();
        sceneController.setRoot (new LoginController (sceneController).getView ());
    }

    public void setRoot (Parent p) {
        this.mainScreen.getChildren().clear();
        this.mainScreen.getChildren().add (p);
        AnchorPane.setLeftAnchor (p, 0.0);
        AnchorPane.setTopAnchor (p, 0.0);
        AnchorPane.setRightAnchor (p, 0.0);
        AnchorPane.setBottomAnchor (p, 0.0);
    }

    public AnchorPane getMainScreen() {
        return mainScreen;
    }
}

