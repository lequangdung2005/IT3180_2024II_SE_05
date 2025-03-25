package com.hust.ittnk68.cnpm.view;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.interactor.ClientInteractor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminHomeView extends DuongFXGridPane {
    private static final int numCols = 15;
    private static final int numRows = 12;

    public AdminHomeView (ClientSceneController sceneController) {
        super (numCols, numRows);

        ClientInteractor interactor = sceneController.getClientInteractor ();

        // this.setGridLinesVisible (true);
        this.setHgap (10);
        this.setVgap (10);
        this.setPadding (new Insets (30, 50, 30, 50));

        this.getStyleClass().add("gradientbackground");
         
        this.add (interactor.getMainScreen(), 1, 1, numCols - 1, numRows - 1);

        BorderPane bp = new BorderPane ();
        // bp.setPadding (new Insets (0, 0, 0, 15));

        this.add (bp, 1, 0, numCols - 1, 1);

        Label greeting = new Label ("Xin chào, Đăng Dương!");
        greeting.setId ("greetinglabel");
        Label desc = new Label ("Cổng thông tin, dịch vụ chung cư BlueMoon (trang quản trị)");
        desc.setId ("desclabel");
        VBox greetingVBox = new VBox (greeting, desc);
        bp.setLeft (greetingVBox);

        IconButton chatBtn = new IconButton (new FontIcon (Material2AL.CHAT));
        IconButton notiBtn = new IconButton (new FontIcon (Material2MZ.NOTIFICATIONS));
        HBox notificationWrapper = new HBox (chatBtn, notiBtn);
        notificationWrapper.getStyleClass().addAll("graywrapper", "leftrightpadding");
        notificationWrapper.setAlignment (Pos.CENTER);
        bp.setRight (notificationWrapper);
        
        this.add (new Label(""), 0, 0, 1, 1);

        AdminHomeSideBar vb = new AdminHomeSideBar (sceneController);
        this.add (vb, 0, 1, 1, numRows - 1);

    }
}
