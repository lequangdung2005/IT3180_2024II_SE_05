package com.hust.ittnk68.cnpm.interactor;

import org.apache.commons.codec.digest.DigestUtils;

import com.hust.ittnk68.cnpm.controller.ClientHomeScreenController;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.model.LoginModel;
import com.hust.ittnk68.cnpm.type.AccountType;

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

            String loginResult = sceneController.getRestClient().get()
                .uri(sceneController.getUriBase()
                        + String.format("/api/account-auth?username=%s&digestPassword=%s",
                                username, digestPassword))
                .retrieve()
                .body(String.class);

            assert loginResult != null;

            // xu ly exception
            // khong ket noi dc => thong bao

            if(loginResult.equals(AccountType.UNVALID.toString())) {
                System.out.println("dang nhap lai");
            }
            else if(loginResult.equals(AccountType.ADMIN.toString())) {
                System.out.println("admin");
                sceneController.setRoot(new ClientHomeScreenController(sceneController).getView());
            }
            else if(loginResult.equals(AccountType.USER.toString())) {
                System.out.println("user");
                sceneController.setRoot(new ClientHomeScreenController(sceneController).getView());
            }
        }
    }

    public void forgetPasswordButtonHandler() {
        System.out.println("forget");
    }
}
