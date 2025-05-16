package com.hust.ittnk68.cnpm.view;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.opencsv.CSVReader;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CsvViewer extends VBox {

    private ClientSceneController sceneController;
    private TableView<List<String>> tableView = new TableView<>();

    public void loadCsvToTable (String content)
    {
        tableView.getItems().clear ();
        tableView.getColumns().clear ();

        try (CSVReader csvReader = new CSVReader(new StringReader(content)))
        {
            List<String[]> allLines = csvReader.readAll ();

            if (allLines.isEmpty ()) return ;

            String headers[] = allLines.get (0);

            for (int i = 0; i < headers.length; ++i) {
                final int colIndex = i;
                TableColumn<List<String>, String> column = new TableColumn<> (headers[i]);
                column.setCellValueFactory(data -> {
                    List<String> rowValues = data.getValue ();
                    if (colIndex >= rowValues.size ()) {
                        return new SimpleStringProperty("");
                    }
                    else {
                        return new SimpleStringProperty(rowValues.get(colIndex)); 
                    }
                });
                tableView.getColumns().add(column);
            }

            ObservableList <List<String>> data = FXCollections.observableArrayList();
            for (int i = 1; i < allLines.size(); ++i) {
                data.add (List.of (allLines.get (i)));
            }

            tableView.setItems(data);
        }
        catch (Exception e)
        {
            sceneController.getClientInteractor().notificate("Thất bại", e.toString ());
        }

    }

    public CsvViewer (ClientSceneController sceneController, String titleContent, String content)
    {
        this.sceneController = sceneController;

        Text title = new Text (titleContent);
        title.getStyleClass ().addAll (Styles.TEXT, Styles.TITLE_3, Styles.ACCENT);

        loadCsvToTable (content);

        this.setSpacing (5);
        this.getChildren ().addAll (
            new Spacer (),
            title,
            new Separator(),
            tableView,
            new Spacer()
        );
    }
}
