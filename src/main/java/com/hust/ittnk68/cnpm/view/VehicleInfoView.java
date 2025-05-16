package com.hust.ittnk68.cnpm.view;

import java.util.concurrent.atomic.AtomicInteger;

import com.hust.ittnk68.cnpm.communication.ServerResponseObject;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VehicleInfoView extends VBox {

    final private Stage stage;
    final private ClientSceneController sceneController;
    final private GridPane con;

    public VehicleInfoView (Stage stage, ClientSceneController sceneController)
    {
        this.stage = stage;
        this.sceneController = sceneController;

        Button closeBtn = new Button (null, new FontIcon (Material2AL.CLOSE));
        closeBtn.getStyleClass ().addAll (Styles.BUTTON_ICON, Styles.DANGER);
        closeBtn.setOnAction (e -> stage.close ());
        HBox hb = new HBox(closeBtn);
        hb.setAlignment (Pos.TOP_RIGHT);

        Text title = new Text ("Bảng giá gửi xe");
        title.getStyleClass ().addAll (Styles.TEXT, Styles.TITLE_3, Styles.ACCENT);

        this.con = new GridPane();
        this.con.setHgap (50);
        this.con.setVgap (15);
        fetchData ();

        this.setSpacing (5);
        this.getChildren ().addAll (
            hb,
            new Spacer (),
            title,
            new Separator(),
            con
        );
        this.getStylesheets().addAll (
            getClass().getResource (
                "/css/client.css"
            ).toExternalForm()
        );
        this.getStyleClass ().addAll ("graywrapper", "border");

        Rectangle clip = new Rectangle ();
        clip.widthProperty ().bind (this.widthProperty ());
        clip.heightProperty ().bind (this.heightProperty ());
        clip.setArcWidth (70);
        clip.setArcHeight (70);
        this.setClip (clip);
    }

    private void fetchData ()
    {
        while (true)
        {
            ServerResponseObject res =  sceneController.getParkingFee (sceneController.getUsername ());
            if (res.getResponseStatus().equals (ResponseStatus.OK)) {
                AtomicInteger curRow = new AtomicInteger (0);
                res.getResult().forEach ((key, value) -> {
                    Text text1 = new Text (key + ":");
                    text1.getStyleClass().addAll (Styles.TEXT, Styles.TEXT_BOLD);
                    Text text2 = new Text ( String.format ("%,d đ", Integer.parseInt( (String) value) ));
                    text2.getStyleClass().addAll (Styles.TEXT);
                    con.add (text1, 0, curRow.get());
                    con.add (text2, 1, curRow.get());
                    curRow.set (curRow.get() + 1);
                });
                break;
            }
            else {
                sceneController.getClientInteractor ().showFailedWindow (res);
            }
        }
    }
    
}
