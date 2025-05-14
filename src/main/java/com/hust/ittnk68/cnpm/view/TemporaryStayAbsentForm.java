package com.hust.ittnk68.cnpm.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.hust.ittnk68.cnpm.type.Nation;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import atlantafx.base.controls.MaskTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TemporaryStayAbsentForm extends VBox {

    final private TextField fullnameTf;
    final private MaskTextField CIDTextField;
    final private DatePicker dateOfBirthPicker;
    final private MaskTextField phoneNumberTextField;
    final private ComboBox<String> genderChoose;
    final private ComboBox<String> nationChoose;
    final private ComboBox<String> residenceStatusChoose;

    private List <String> getAllNation () {
        List<String> list = new ArrayList<>();
        list.add ("-- Chọn --");
        for (Nation nation : Nation.values()) {
            list.add (nation.toString());
        }
        return list;
    }

    private Text createText (String value)
    {
        Text text = new Text (value);
        text.getStyleClass ().addAll (Styles.TEXT_BOLD);
        return text;
    }

    private GridPane createForm ()
    {
        GridPane gp = new GridPane ();
        gp.add (createText ("Họ và tên"), 0, 0);
        gp.add (fullnameTf, 1, 0);
        gp.add (createText ("CCCD"), 0, 1);
        gp.add (CIDTextField, 1, 1);
        gp.add (createText ("Ngày sinh"), 0, 2);
        gp.add (dateOfBirthPicker, 1, 2);
        gp.add (createText ("Số điện thoại"), 0, 3);
        gp.add (phoneNumberTextField, 1, 3);
        gp.add (createText ("Giới tính"), 0, 4);
        gp.add (genderChoose, 1, 4);
        gp.add (createText ("Quốc tịch"), 0, 5);
        gp.add (nationChoose, 1, 5);
        gp.add (createText ("Tình trạng cư trú"), 0, 6);
        gp.add (residenceStatusChoose, 1, 6);
        gp.setHgap (15);
        gp.setVgap (15);
        return gp;
    }

    public TemporaryStayAbsentForm (Stage stage)
    {
        Button closeBtn = new Button (null, new FontIcon (Material2AL.CLOSE));
        closeBtn.getStyleClass ().addAll (Styles.BUTTON_ICON, Styles.BUTTON_OUTLINED, Styles.DANGER);
        closeBtn.setOnAction (e -> stage.close ());
        HBox hb = new HBox(closeBtn);
        hb.setAlignment (Pos.TOP_RIGHT);

        fullnameTf = new TextField ();
        fullnameTf.requestFocus ();

        CIDTextField = new MaskTextField("9999 - 9999 - 9999");
        CIDTextField.setLeft (new FontIcon (Material2MZ.PAYMENT));

        phoneNumberTextField = new MaskTextField("999 999 99 99");
        phoneNumberTextField.setLeft (new FontIcon (Material2OutlinedMZ.PHONE));

        LocalDate today = LocalDate.now (ZoneId.systemDefault());
        dateOfBirthPicker = new DatePicker (today);
        dateOfBirthPicker.setEditable (true);

        genderChoose = new ComboBox<>();
        genderChoose.setItems (FXCollections.observableArrayList(
            "Nam", "Nữ"
        ));
        genderChoose.getSelectionModel().selectFirst();

        nationChoose = new ComboBox<>();
        nationChoose.setItems (FXCollections.observableArrayList( getAllNation ()) );
        nationChoose.getSelectionModel().selectFirst();

        residenceStatusChoose = new ComboBox<> ();
        residenceStatusChoose.setItems (FXCollections.observableArrayList(
            "Đang cư trú",
            "Tạm vắng"
        ));
        residenceStatusChoose.getSelectionModel().selectFirst();

        Text title = new Text ("Khai báo tạm trú / tạm vắng");
        title.getStyleClass ().addAll (Styles.TEXT, Styles.TITLE_3, Styles.ACCENT);

        Button submitBtn = new Button ("Khai báo");
        submitBtn.getStyleClass ().addAll (Styles.BUTTON_OUTLINED, Styles.SUCCESS);
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
