package com.hust.ittnk68.cnpm.view;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class ClientMenuButton extends VBox {
    ClientMenuButton(FontIcon icon, String labelContent) {
	super(10);

	CustomButton btn = new CustomButton(icon);
	btn.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		System.out.println("button pressed " + this.getClass());
	    }
	});

	Label label = new Label(labelContent);
	label.setWrapText(true);
	label.textAlignmentProperty().set(TextAlignment.CENTER);

	HBox labelWrapper = new HBox(label);
	labelWrapper.setAlignment(Pos.CENTER);
	labelWrapper.setMinHeight(30);

	this.getChildren().addAll(btn, labelWrapper);
	this.setAlignment(Pos.CENTER);

	this.setPrefWidth(100);
    }
}
