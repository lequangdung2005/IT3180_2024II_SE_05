package com.hust.ittnk68.cnpm.view;

import org.kordamp.ikonli.javafx.FontIcon;

import atlantafx.base.theme.Styles;
import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class CustomButton extends Button {
    public CustomButton(FontIcon icon) {
	super(null, icon);
	// this.setId("customButton");
	this.setPrefSize(68, 68);
	this.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
	this.setCursor(Cursor.HAND);
    }
}
