package com.hust.ittnk68.cnpm.view;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import com.hust.ittnk68.cnpm.view.ClientMenuLabel;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ClientMenuLabel extends Label {
    private void init() {
	this.getStyleClass().addAll(Styles.TEXT_CAPTION);
    }

    public ClientMenuLabel(String labelContent) {
	super(labelContent);
	init();
    }
    public ClientMenuLabel(String labelContent, FontIcon icon) {
	super(labelContent, icon);
	init();
    }
}
