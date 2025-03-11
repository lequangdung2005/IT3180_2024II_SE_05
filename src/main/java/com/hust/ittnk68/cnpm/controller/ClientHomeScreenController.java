package com.hust.ittnk68.cnpm.controller;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import javafx.scene.layout.Region;

import com.hust.ittnk68.cnpm.model.ClientHomeScreenModel;
import com.hust.ittnk68.cnpm.view.ClientHomeScreenView;

public class ClientHomeScreenController {
    private final Region view;

    public ClientHomeScreenController(ClientSceneController sceneController) {
        ClientHomeScreenModel model = new ClientHomeScreenModel();
        view = new ClientHomeScreenView();
        sceneController.setTitle("BlueMoon - Trang chủ");
    }

    public Region getView() {
        return view;
    }
}
