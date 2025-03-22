package com.hust.ittnk68.cnpm.view;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;

import javafx.scene.control.Tab;

public class AdminAccountManagePage extends DuongFXTabPane {
    public AdminAccountManagePage (ClientSceneController sceneController) {
	this.getTabs().addAll (new Tab("Tạo tài khoản", new AccountCreateForm (sceneController)),
				new Tab("Tìm kiếm tài khoản", null));
    }
} 
