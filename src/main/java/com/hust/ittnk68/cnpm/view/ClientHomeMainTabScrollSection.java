package com.hust.ittnk68.cnpm.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2RoundMZ;
import org.kordamp.ikonli.material2.Material2SharpAL;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ClientHomeMainTabScrollSection extends ScrollPane {
    ClientHomeMainTabScrollSection() {
	super();
	
	ClientMenuLabel label1 = new ClientMenuLabel("Khai báo tạm trú, tạm vắng",
								new FontIcon(Material2MZ.PEOPLE_ALT));
	ClientMenuLabel label2 = new ClientMenuLabel("Quản lí xe cộ ? (fix giùm)",
								new FontIcon(Material2AL.DIRECTIONS_CAR));
	ClientMenuLabel label3 = new ClientMenuLabel("Thanh toán", new FontIcon(Material2MZ.MONETIZATION_ON));
	ClientMenuLabel label4 = new ClientMenuLabel("Quyên góp", new FontIcon(Material2AL.FAVORITE));

	ClientMenuButton btn12 = new ClientMenuButton(new FontIcon(Material2MZ.PERSON_ADD), "Khai báo tạm trú");
	ClientMenuButton btn13 = new ClientMenuButton(new FontIcon(Material2MZ.PERSON_REMOVE), "Khai báo tạm vắng");
	HBox hb1 = new HBox(25);
	hb1.getChildren().addAll(btn12, btn13);

	ClientMenuButton btn21 = new ClientMenuButton(new FontIcon(Material2AL.INFO), "Giá niêm yết bãi gửi xe");	
	ClientMenuButton btn23 = new ClientMenuButton(new FontIcon(Material2AL.ADD_CIRCLE_OUTLINE), "Thêm xe");
	ClientMenuButton btn24 = new ClientMenuButton(new FontIcon(Material2RoundMZ.REMOVE_CIRCLE_OUTLINE), "Bớt xe ??");

	HBox hb2 = new HBox(25);
	hb2.getChildren().addAll(btn21, btn23, btn24);

	ClientMenuButton btn31 = new ClientMenuButton(new FontIcon(Material2AL.CLEANING_SERVICES), "Thanh toán tiền dịch vụ");
	ClientMenuButton btn32 = new ClientMenuButton(new FontIcon(Material2AL.ELECTRICAL_SERVICES), "Thanh toán tiền điện");
	ClientMenuButton btn33 = new ClientMenuButton(new FontIcon(Material2MZ.WATER_DAMAGE), "Thanh toán tiền nước");
	ClientMenuButton btn34 = new ClientMenuButton(new FontIcon(Material2AL.LOCAL_PARKING), "Thanh toán tiền gửi xe");

	HBox hb3 = new HBox(25, btn31, btn32, btn33, btn34);

	ClientMenuButton btn41 = new ClientMenuButton(new FontIcon(Material2MZ.MONEY), "Quyên góp vào quỹ tổ dân phố");
	ClientMenuButton btn42 = new ClientMenuButton(new FontIcon(Material2MZ.MONEY), "Quyên góp vào quỹ chung cư");
	ClientMenuButton btn43 = new ClientMenuButton(new FontIcon(Material2MZ.MONEY), "Quyên góp vào quỹ y tế");
	ClientMenuButton btn44 = new ClientMenuButton(new FontIcon(Material2MZ.MONEY), "Quyên góp vào quỹ từ thiện");

	HBox hb4 = new HBox(25, btn41, btn42, btn43, btn44);

	VBox vb = new VBox(15); // fix constructor?
	vb.getChildren().addAll(label1, hb1, label2, hb2, label3, hb3, label4, hb4);

	AnchorPane ap = new AnchorPane(vb);
	AnchorPane.setTopAnchor(vb, 9.0);
	AnchorPane.setLeftAnchor(vb, 36.0);
	AnchorPane.setBottomAnchor(vb, 36.0);

	this.setContent(ap);
    }
}
