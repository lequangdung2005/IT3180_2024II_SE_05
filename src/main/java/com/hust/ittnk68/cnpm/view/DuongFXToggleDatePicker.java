package com.hust.ittnk68.cnpm.view;

import java.time.LocalDate;
import java.time.ZoneId;

import atlantafx.base.layout.InputGroup;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.Node;

public class DuongFXToggleDatePicker extends InputGroup {

    private BooleanProperty enable; 
    private ObjectProperty<LocalDate> value;

    private DuongFXToggleDatePicker (Node... nodes) {
	super (nodes);
	enable = new SimpleBooleanProperty (false); 
        value = new SimpleObjectProperty<> ();
    }

    public BooleanProperty enableProperty () {
	return enable;
    }

    public ObjectProperty<LocalDate> valueProperty () {
	return value;
    }

    public static DuongFXToggleDatePicker create () {

	CheckBox checkBox = new CheckBox ();
	checkBox.setSelected (false);

        LocalDate today = LocalDate.now (ZoneId.systemDefault());
        DatePicker datePicker = new DatePicker (today);
        datePicker.setEditable (true); 
	datePicker.getEditor().setDisable (true);

	checkBox.selectedProperty().addListener (new ChangeListener<Boolean>() {
	    @Override
	    public void changed (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		datePicker.getEditor().setDisable (!newValue);
	    }
	});

	DuongFXToggleDatePicker res = new DuongFXToggleDatePicker (new Label ("", checkBox), datePicker);
	res.enableProperty().bindBidirectional (checkBox.selectedProperty());
	res.valueProperty().bindBidirectional (datePicker.valueProperty());
	return res;
    } 
}
