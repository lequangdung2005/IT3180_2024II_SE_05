package com.hust.ittnk68.cnpm.controller;

import com.hust.ittnk68.cnpm.model.ClientModel;
import com.hust.ittnk68.cnpm.view.LoginView;
import javafx.scene.layout.Region;

public class LoginController {
    private final Region view; 

    public LoginController(ClientSceneController sceneController) {
        ClientModel model = new ClientModel();
        view = new LoginView(sceneController);
        sceneController.setTitle("BlueMoon - Đăng nhập");
    }

    public Region getView() {
        return view;
    }
}
