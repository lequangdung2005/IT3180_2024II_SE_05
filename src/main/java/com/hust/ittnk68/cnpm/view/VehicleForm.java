package com.hust.ittnk68.cnpm.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hust.ittnk68.cnpm.communication.PostTemporaryStayAbsentRequest;
import com.hust.ittnk68.cnpm.communication.PostVehicle;
import com.hust.ittnk68.cnpm.communication.ServerResponseBase;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.TemporaryStayAbsentRequest;
import com.hust.ittnk68.cnpm.model.Vehicle;
import com.hust.ittnk68.cnpm.type.Date;
import com.hust.ittnk68.cnpm.type.Nation;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.ResponseStatus;
import com.hust.ittnk68.cnpm.type.Sex;
import com.hust.ittnk68.cnpm.type.VehicleType;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import atlantafx.base.controls.MaskTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VehicleForm extends VBox {

    final Stage stage;

    final private ComboBox<String> vehicleTypeBox;
    final private TextField plateIdTf;
    final private ClientSceneController sceneController;

    private Text createText (String value)
    {
        Text text = new Text (value);
        text.getStyleClass ().addAll (Styles.TEXT_BOLD);
        return text;
    }

    private GridPane createForm ()
    {
        GridPane gp = new GridPane ();
        gp.add (createText ("Loại phương tiện"), 0, 0);
        gp.add (vehicleTypeBox, 1, 0);
        gp.add (createText ("Biển số"), 0, 1);
        gp.add (plateIdTf, 1, 1);
        gp.setHgap (15);
        gp.setVgap (15);
        return gp;
    }

    private ServerResponseBase sendRequest ()
    {
        String vehType = this.vehicleTypeBox.valueProperty ().get ();
        VehicleType vehicleType;
        if (vehType.equals ("Xe hơi")) {
            vehicleType = VehicleType.CAR;
        }
        else if (vehType.equals ("Xe đạp")) {
            vehicleType = VehicleType.BICYCLE;
        }
        else if (vehType.equals ("Xe máy")) {
            vehicleType = VehicleType.MOTORCYCLE;
        }
        else {
            vehicleType = VehicleType.ELECTRIC_BICYCLE;
        }
        String plateId = this.plateIdTf.textProperty ().get();
        plateId = plateId.trim ();
        plateId = plateId.replaceAll ("^ +| +$|( )+", "$1");

        Vehicle vehicle = new Vehicle (vehicleType, -1, plateId);
        PostVehicle req = new PostVehicle (sceneController.getUsername (), vehicle);
        return sceneController.postVehicle (req);
    }

    public VehicleForm (Stage stage, ClientSceneController sceneController, Runnable reloadCallback)
    {
        this.stage = stage;
        this.sceneController = sceneController;

        Button closeBtn = new Button (null, new FontIcon (Material2AL.CLOSE));
        closeBtn.getStyleClass ().addAll (Styles.BUTTON_ICON, Styles.DANGER);
        closeBtn.setOnAction (e -> stage.close ());
        HBox hb = new HBox(closeBtn);
        hb.setAlignment (Pos.TOP_RIGHT);

        vehicleTypeBox = new ComboBox<>( FXCollections.observableList(Arrays.asList("Xe hơi", "Xe đạp", "Xe máy", "Xe đạp điện")) );
        plateIdTf = new TextField ();

        Text title = new Text ("Đăng ký phương tiện");
        title.getStyleClass ().addAll (Styles.TEXT, Styles.TITLE_3, Styles.ACCENT);

        Button submitBtn = new Button ("Khai báo");
        submitBtn.getStyleClass ().addAll (Styles.SUCCESS);
        submitBtn.setOnAction (e -> {
            ServerResponseBase res =  sendRequest ();
            if (res.getResponseStatus ().equals (ResponseStatus.OK)) {
                stage.close ();
                sceneController.getClientInteractor ().notificate ("Thành công", "Khai báo thành công");
                reloadCallback.run ();
            }
            else {
                sceneController.getClientInteractor ().showFailedWindow (res);
            }
        });
        HBox btnBox = new HBox(submitBtn);
        btnBox.setAlignment (Pos.CENTER);

        this.setSpacing (5);
        this.getChildren ().addAll (
            hb,
            new Spacer (),
            title,
            new Separator (),
            createForm (),
            new Separator (),
            btnBox,
            new Spacer ()
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
    
}
