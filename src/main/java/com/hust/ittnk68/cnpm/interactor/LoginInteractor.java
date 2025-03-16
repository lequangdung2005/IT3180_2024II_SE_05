package com.hust.ittnk68.cnpm.interactor;

import org.apache.commons.codec.digest.DigestUtils;

import com.hust.ittnk68.cnpm.communication.ClientMessageStartSession;
import com.hust.ittnk68.cnpm.communication.ServerResponseStartSession;
import com.hust.ittnk68.cnpm.controller.ClientHomeScreenController;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.model.LoginModel;
import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

public class LoginInteractor {
    LoginModel model;
    ClientSceneController sceneController;

    public LoginInteractor(LoginModel model, ClientSceneController sceneController) {
        this.model = model;
        this.sceneController = sceneController;
    }

    public void loginHandler() {
        String username = model.getUsername();
        String password = model.getPassword();
        if(username == null || username.trim().length() == 0) {
            System.out.println("no username");
        }
        else if(password == null || password.trim().length() == 0) {
            System.out.println("no password");
        }
        else {
            String digestPassword = DigestUtils.sha256Hex(password);

            ClientMessageStartSession message = new ClientMessageStartSession (username, digestPassword);
            ServerResponseStartSession res = sceneController.startSession (message);

            // xu ly exception
            // khong ket noi dc => thong bao

            if (res.getResponseStatus ().equals (ResponseStatus.NOT_OK))
            {
                System.out.println ("ko ket noi den server dc");
                return;
            }

            if (res.getResponseStatus ().equals (ResponseStatus.SESSION_ERROR))
            {
                System.out.println ("tai khoan dang dc dang nhap ...");
                return;
            }

            sceneController.setToken (res.getToken ());
            System.out.println ("token = " + sceneController.getToken ());

            if(res.getAccountType().equals(AccountType.UNVALID)) {
                System.out.println("dang nhap lai");
            }
            else if(res.getAccountType().equals(AccountType.ADMIN)) {
                System.out.println("admin");
                sceneController.setRoot(new ClientHomeScreenController(sceneController).getView());
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
}
