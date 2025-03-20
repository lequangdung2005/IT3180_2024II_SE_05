package com.hust.ittnk68.cnpm.interactor;

import org.apache.commons.codec.digest.DigestUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import com.hust.ittnk68.cnpm.communication.ClientMessageStartSession;
import com.hust.ittnk68.cnpm.communication.ServerResponseStartSession;
import com.hust.ittnk68.cnpm.controller.AdminHomeScreenController;
import com.hust.ittnk68.cnpm.controller.ClientHomeScreenController;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.controller.LoginController;
import com.hust.ittnk68.cnpm.model.ClientModel;
import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class ClientInteractor {
    ClientModel model;
    ClientSceneController sceneController;
    AnchorPane mainScreen;

    public ClientInteractor (ClientSceneController sceneController) {
        this.model = sceneController.getClientModel ();
        this.sceneController = sceneController;
        this.sceneController = sceneController;
        this.mainScreen = new AnchorPane ();
    }

    public void loginHandler() {
        String username = model.getUsername();
        String password = model.getPassword();
        if(username == null || username.trim().length() == 0) {
            System.out.println("no username");
            sceneController.openWarningNoti ("Quý khách vui lòng nhập tài khoản.",
                                            new FontIcon (Material2AL.FLAG));
        }
        else if(password == null || password.trim().length() == 0) {
            System.out.println("no password");
            sceneController.openWarningNoti ("Quý khách vui lòng nhập mật khẩu.",
                                            new FontIcon (Material2AL.FLAG));
        }
        else {
            String digestPassword = DigestUtils.sha256Hex(password);

            ClientMessageStartSession message = new ClientMessageStartSession (username, digestPassword);
            ServerResponseStartSession res = sceneController.startSession (message);

            // xu ly exception
            // khong ket noi dc => thong bao

            if (res.getResponseStatus ().equals (ResponseStatus.CANT_CONNECT_SERVER))
            {
                System.out.println ("ko ket noi den server dc");
                sceneController.openWarningNoti ("Không thể kết nối đến server.",
                                                new FontIcon (Material2AL.FLAG));
                return;
            } 
 
            if (res.getResponseStatus ().equals (ResponseStatus.SESSION_ERROR))
            {
                System.out.println ("tai khoan dang dc dang nhap ...");
                sceneController.openWarningNoti ("Tài khoản đang được đăng nhập trên thiết bị khác.",
                                                 new FontIcon (Material2AL.FLAG));
                return;
            }

            sceneController.setToken (res.getToken ());
            System.out.println ("token = " + sceneController.getToken ());

            if(res.getAccountType().equals(AccountType.UNVALID)) {
                System.out.println("dang nhap lai");
            }
            else if(res.getAccountType().equals(AccountType.ADMIN)) {
                System.out.println("admin");
                sceneController.setRoot(new AdminHomeScreenController(sceneController).getView());
            }
            else if(res.getAccountType().equals(AccountType.USER)) {
                System.out.println("user");
                sceneController.setRoot(new ClientHomeScreenController(sceneController).getView());
            }
        }
    }

    public void forgetPasswordButtonHandler() {
        System.out.println("forget");
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

