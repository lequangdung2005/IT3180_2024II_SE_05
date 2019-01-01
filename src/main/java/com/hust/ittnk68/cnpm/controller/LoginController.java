package com.hust.ittnk68.cnpm.controller;

import com.hust.ittnk68.cnpm.interactor.LoginInteractor;
import com.hust.ittnk68.cnpm.model.LoginModel;
import com.hust.ittnk68.cnpm.view.LoginView;
import javafx.scene.layout.Region;

public class LoginController {
    private final Region view; 

    public LoginController(ClientSceneController sceneController) {
        LoginModel model = new LoginModel();
        LoginInteractor interactor = new LoginInteractor(model, sceneController);
        view = new LoginView(sceneController, model, interactor);
        sceneController.setTitle("BlueMoon - Đăng nhập");
    }

    public Region getView() {
        return view;
    }
}
