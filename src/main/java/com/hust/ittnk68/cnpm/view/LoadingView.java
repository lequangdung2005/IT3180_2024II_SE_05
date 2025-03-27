package com.hust.ittnk68.cnpm.view;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class LoadingView extends VBox {

    ProgressBar pBar;
    Label statusLabel;

    public <T> LoadingView(Task<T> task) {
        ProgressBar pBar = new ProgressBar();
        pBar.progressProperty().bind(task.progressProperty());
        pBar.setPrefSize (200, 24);

        Label statusLabel = new Label();
        statusLabel.textProperty().bind(task.messageProperty());

        this.pBar = pBar;
        this.statusLabel = statusLabel;

        this.setSpacing (5);
        this.getChildren ().addAll (pBar, statusLabel);

        this.getStylesheets().add (
            getClass().getResource (
                "/css/progress-bar.css"
            ).toExternalForm()
        );
    }
}
