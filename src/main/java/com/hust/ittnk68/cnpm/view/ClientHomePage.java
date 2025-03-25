package com.hust.ittnk68.cnpm.view;

import java.util.stream.IntStream;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;

import atlantafx.base.controls.Calendar;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ClientHomePage extends DuongFXGridPane {
    public ClientHomePage (ClientSceneController sceneController) {
	super (12, 9);
	// this.setGridLinesVisible (true);
	// this.getStyleClass().addAll ("fullpadding");
	this.setHgap (10);
	this.setVgap (10);

	CategoryAxis x = new CategoryAxis ();
	NumberAxis y = new NumberAxis ();
	y.setLabel ("(Việt Nam đồng)");

	XYChart.Series<String, Number> donate = new XYChart.Series<>();
	donate.setName ("Thiện nguyện");

	donate.getData().addAll (
	    new XYChart.Data<> ("Tháng 1", 50000000),
	    new XYChart.Data<> ("Tháng 2", 45000000),
	    new XYChart.Data<> ("Tháng 3", 66000000) 
	);

	XYChart.Series<String, Number> expense = new XYChart.Series<>();
	expense.setName ("Chi phí");

	expense.getData().addAll (
	    new XYChart.Data<> ("Tháng 1", 30000000),
	    new XYChart.Data<> ("Tháng 2", 65000000),
	    new XYChart.Data<> ("Tháng 3", 26000000) 
	);

	BarChart<String, Number> chart = new BarChart <> (x, y);
	chart.setTitle ("Thống kê chi tiêu");
	chart.getData().addAll (donate, expense);

	HBox hb = createHbox ();
	hb.setPadding (new Insets (10, 0, 0, 0));
	HBox.setHgrow (chart, Priority.ALWAYS);
	hb.getChildren ().add (chart);

	this.add (hb, 0, 0, 8, 5);

	this.add (createHbox(), 8, 0, 4, 5);

	this.add (createHbox(), 0, 5, 4, 4);

	this.add (createHbox(), 4, 5, 8, 4);
    }

    HBox createHbox () {
	HBox a = new HBox ();
	a.getStyleClass ().addAll ("graywrapper");
	return a;
    }
}

