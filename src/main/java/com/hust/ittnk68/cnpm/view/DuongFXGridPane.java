package com.hust.ittnk68.cnpm.view;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class DuongFXGridPane extends GridPane {

    public DuongFXGridPane (int numCols, int numRows) {
	super ();
	for (int i = 0; i < numCols; ++i) {
	    ColumnConstraints colConst = new ColumnConstraints ();
	    colConst.setPercentWidth (100.0 / numCols);
	    this.getColumnConstraints().add(colConst);
	}
	for (int i = 0; i < numRows; ++i) {
	    RowConstraints rowConst = new RowConstraints ();
	    rowConst.setPercentHeight (100.0 / numRows);
	    this.getRowConstraints().add(rowConst);
	}
    }

}
