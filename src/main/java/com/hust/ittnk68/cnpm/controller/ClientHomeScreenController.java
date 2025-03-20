package com.hust.ittnk68.cnpm.controller;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.interactor.ClientInteractor;
import com.hust.ittnk68.cnpm.interactor.ClientInteractor;
import javafx.scene.layout.Region;

import com.hust.ittnk68.cnpm.view.ClientHomeScreenView;

public class ClientHomeScreenController {
    private final Region view;

    public ClientHomeScreenController(ClientSceneController sceneController) {
        ClientInteractor interactor = new ClientInteractor(sceneController);
        view = new ClientHomeScreenView (sceneController, interactor);
        sceneController.setTitle("BlueMoon - Trang chủ");
    }

    public Region getView() {
        return view;
    }
}
