package com.hust.ittnk68.cnpm.view;

import javafx.geometry.Insets;
import javafx.scene.control.TabPane;

public class DuongFXTabPane extends TabPane {
    public DuongFXTabPane () {
	this.setPadding (new Insets (12, 26, 26, 26));
	this.getStyleClass().addAll ("duongfxtabpane", "graywrapper");
	this.setTabClosingPolicy (TabClosingPolicy.UNAVAILABLE);
    }
}
