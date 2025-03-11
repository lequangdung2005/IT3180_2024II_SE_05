package com.hust.ittnk68.cnpm.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class ClientHomeMainTabInfoSection extends AnchorPane {
    private HBox createBox(String s) {
	Label label = new Label(s);
	label.setMinSize(120, 80);
	label.setAlignment(Pos.CENTER);
	label.setStyle("-fx-background-color:-color-accent-subtle;");

	HBox box = new HBox(label);
	box.setAlignment(Pos.CENTER);
	
	return box;
    }

    public ClientHomeMainTabInfoSection() {
	VBox vb = new VBox(10);
	this.getChildren().add(vb);
	AnchorPane.setTopAnchor(vb, 0.0);
	AnchorPane.setBottomAnchor(vb, 0.0);
	AnchorPane.setLeftAnchor(vb, 0.0);
	AnchorPane.setRightAnchor(vb, 0.0);

	Label label1 = new Label("Xin chao dau buoi");


	vb.getChildren().addAll(label1, createBox("abc"));
	vb.setAlignment(Pos.CENTER);
    }
}
