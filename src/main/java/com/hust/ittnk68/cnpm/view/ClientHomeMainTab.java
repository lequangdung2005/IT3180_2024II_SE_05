package com.hust.ittnk68.cnpm.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ClientHomeMainTab extends AnchorPane {
    ClientHomeMainTab() {
	super();

	ClientHomeMainTabInfoSection ap1 = new ClientHomeMainTabInfoSection();
	ClientHomeMainTabScrollSection ap2 = new ClientHomeMainTabScrollSection();
	//
	// ap1.setPrefHeight(300);
	// ap2.setPrefHeight(300);

	// SplitPane sp = new SplitPane();
	// sp.setDividerPositions(0.3f);
	// sp.getItems().addAll(ap1, ap2);
	// sp.setOrientation(Orientation.VERTICAL);
	
	VBox wrapper = new VBox(
	    ap1,
	    new Separator(Orientation.HORIZONTAL),
	    ap2
	);

	this.getChildren().add(wrapper);
	AnchorPane.setTopAnchor(wrapper, 0.0);
	AnchorPane.setBottomAnchor(wrapper, 0.0);
	AnchorPane.setLeftAnchor(wrapper, 0.0);
	AnchorPane.setRightAnchor(wrapper, 0.0);
    }
}
