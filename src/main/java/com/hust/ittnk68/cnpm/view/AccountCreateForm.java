package com.hust.ittnk68.cnpm.view;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;

import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class AccountCreateForm extends DuongFXGridPane {
    public AccountCreateForm (ClientSceneController sceneController) {
	super (10, 10);

	this.setHgap (10);
	this.setVgap (10);
	this.setGridLinesVisible (true);

	Text taoTaiKhoan = new Text("Tạo tài khoản");
	this.add (taoTaiKhoan, 0, 0, 2, 1);
    }
}
