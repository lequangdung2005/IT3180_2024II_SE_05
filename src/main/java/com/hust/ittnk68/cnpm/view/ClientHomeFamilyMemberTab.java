package com.hust.ittnk68.cnpm.view;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Message;
import atlantafx.base.controls.Notification;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class ClientHomeFamilyMemberTab extends Region {
    public ClientHomeFamilyMemberTab() {
	Notification obj = new Notification(
	    "lorem ipsum lorem ipsum lorem ipsum lorem ipsum"
	);
	obj.getStyleClass().addAll(Styles.ELEVATED_1, Styles.ACCENT);
	obj.setGraphic(new FontIcon(Material2AL.ACCESS_ALARM));
	obj.setOnClose(e -> Animations.flash(obj).playFromStart());

	this.getChildren().add(obj);
    }
}
