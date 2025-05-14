package com.hust.ittnk68.cnpm.view;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class LoadingView extends VBox {

    ProgressBar pBar;
    Label statusLabel;

    public <T> LoadingView(Task<T> task) {
        ProgressBar pBar = new ProgressBar();
        pBar.progressProperty().bind(task.progressProperty());
        pBar.setPrefSize (300, 24);

        Label statusLabel = new Label();
        statusLabel.textProperty().bind(task.messageProperty());

        this.pBar = pBar;
        this.statusLabel = statusLabel;

        this.setSpacing (5);
        this.setPadding (new Insets (16));
        this.getChildren ().addAll (pBar, statusLabel);

        this.getStylesheets().addAll (
            getClass().getResource (
                "/css/progress-bar.css"
            ).toExternalForm()
        );

        this.getStyleClass ().addAll (
            "graywrapper", "loadingview"
        );

        Rectangle clip = new Rectangle ();
        clip.widthProperty ().bind (this.widthProperty ());
        clip.heightProperty ().bind (this.heightProperty ());
        clip.setArcWidth (10);
        clip.setArcHeight (10);
        this.setClip (clip);
    }
}
