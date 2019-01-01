package com.hust.ittnk68.cnpm.view;

import com.hust.ittnk68.cnpm.view.IconButton;
import org.kordamp.ikonli.javafx.FontIcon;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Button;

public class IconButton extends Button {
    public IconButton (FontIcon icon) {
	super (null, icon); 
	this.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ROUNDED);
    }
}
