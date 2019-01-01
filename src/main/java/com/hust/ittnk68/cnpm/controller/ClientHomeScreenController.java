package com.hust.ittnk68.cnpm.controller;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.interactor.ClientHomeScreenInteractor;
import javafx.scene.layout.Region;

import com.hust.ittnk68.cnpm.model.ClientHomeScreenModel;
import com.hust.ittnk68.cnpm.view.ClientHomeScreenView;

public class ClientHomeScreenController {
    private final Region view;

    public ClientHomeScreenController(ClientSceneController sceneController) {
        ClientHomeScreenModel model = new ClientHomeScreenModel();
        ClientHomeScreenInteractor interactor = new ClientHomeScreenInteractor (sceneController);
        view = new ClientHomeScreenView (sceneController, interactor);
        sceneController.setTitle("BlueMoon - Trang chủ");
    }

    public Region getView() {
        return view;
    }
}
