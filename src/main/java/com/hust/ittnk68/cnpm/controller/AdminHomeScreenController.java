package com.hust.ittnk68.cnpm.controller;

import com.hust.ittnk68.cnpm.view.AdminHomeView;

import javafx.scene.layout.Region;

public class AdminHomeScreenController {
    private final Region view;

    public AdminHomeScreenController(ClientSceneController sceneController) {
        view = new AdminHomeView (sceneController);
    }

    public Region getView () {
        return view;
    }
}
