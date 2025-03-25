package com.hust.ittnk68.cnpm.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class DuongFXIntegerField extends TextField {
    public DuongFXIntegerField () {
	super ();
	initIntegerRestriction ();
    }
    public DuongFXIntegerField (String text) {
	super (text);
	initIntegerRestriction ();
    }

    private void initIntegerRestriction () {
	DuongFXIntegerField integerField = this;
	integerField.textProperty().addListener (new ChangeListener<String>() {
	    @Override
	    public void changed (ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (!newValue.matches ("-?\\d*")) {
		    integerField.setText(oldValue);
		}
	    }
	});
    }
}
