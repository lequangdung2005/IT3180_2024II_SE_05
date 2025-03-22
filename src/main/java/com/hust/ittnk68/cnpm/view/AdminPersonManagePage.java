package com.hust.ittnk68.cnpm.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.CreatePersonModel;
import com.hust.ittnk68.cnpm.type.Nation;

import atlantafx.base.controls.MaskTextField;
import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AdminPersonManagePage extends DuongFXTabPane {

    public AdminPersonManagePage (ClientSceneController sceneController) {
        this.getTabs().add (new Tab ("Khai báo thêm người", createPersonCreateForm(sceneController)));
        this.getTabs().add (new Tab ("Tìm kiếm, chỉnh sửa hoặc xóa", null)); 
    }

    // private int personId;
    // private int familyId;
    // private String fullname;
    // private Date dateOfBirth;
    // private String citizenIdentificationNumber;
    // private String phoneNumber;
    // private Sex sex;
    // private Nation nationality;
    // private ResidenceStatus residenceStatus;
    private VBox createPersonCreateForm (ClientSceneController sceneController) {

        CreatePersonModel model = sceneController.getCreatePersonModel ();

        GridPane gridPane = new GridPane ();
        gridPane.setPadding (new Insets (16, 0, 0, 0));

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

        Text hoVaTen = new Text ("Họ và tên:");
        gridPane.add (hoVaTen, 0, 0, 2, 1);

        TextField fullNameTextField = new TextField ();
        gridPane.add (fullNameTextField, 2, 0, 3, 1);

        Text ngaySinh = new Text ("Ngày sinh:");
        gridPane.add (ngaySinh, 0, 1, 2, 1);

        LocalDate today = LocalDate.now (ZoneId.systemDefault());
        DatePicker dateOfBirthPicker = new DatePicker (today);
        dateOfBirthPicker.setEditable (false);
        gridPane.add (dateOfBirthPicker, 2, 1);

        Text canCuocCongDan = new Text ("Căn cước công dân:");
        gridPane.add (canCuocCongDan, 0, 2, 2, 1);

        MaskTextField CIDTextField = new MaskTextField("9999 - 9999 - 9999");
        CIDTextField.setLeft (new FontIcon (Material2MZ.PAYMENT));
        gridPane.add (CIDTextField, 2, 2, 3, 1);

        Text soDienThoai = new Text ("Số điện thoại:");
        gridPane.add (soDienThoai, 0, 3, 2, 1);

        MaskTextField phoneNumberTextField = new MaskTextField("999 999 99 99");
        phoneNumberTextField.setLeft (new FontIcon (Material2OutlinedMZ.PHONE));
        gridPane.add (phoneNumberTextField, 2, 3, 3, 1);

        Text gioiTinh = new Text ("Giới tính:");
        gridPane.add (gioiTinh, 0, 4, 2, 1);

        ComboBox<String> genderChoose = new ComboBox<>();
        genderChoose.setItems (FXCollections.observableArrayList(
            "-- Chọn --", "Nam", "Nữ"
        ));
        genderChoose.getSelectionModel().selectFirst();
        gridPane.add (genderChoose, 2, 4, 3, 1);

        Text quocTich = new Text ("Quốc tịch:");
        gridPane.add (quocTich, 0, 5, 2, 1);

        ComboBox<String> nationChoose = new ComboBox<>();
        nationChoose.setItems (FXCollections.observableArrayList( getAllNation ()) );
        nationChoose.getSelectionModel().selectFirst();
        gridPane.add (nationChoose, 2, 5, 3, 1);

        Text tinhTrangCuTru = new Text ("Tình trạng cư trú:");
        gridPane.add (tinhTrangCuTru, 0, 6, 2, 1);

        ComboBox<String> residenceStatusChoose = new ComboBox<> ();
        residenceStatusChoose.setItems (FXCollections.observableArrayList(
            "-- Chọn --",
            "Đang cư trú",
            "Tạm vắng"
        ));
        residenceStatusChoose.getSelectionModel().selectFirst();
        gridPane.add (residenceStatusChoose, 2, 6, 3, 1);

        Button submitBtn = new Button ("Khai báo");
        submitBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        submitBtn.setOnAction (e -> sceneController.getClientInteractor().createPersonHandler());

        // bindings
        model.getFullnameProperty().bind (fullNameTextField.textProperty());
        model.getDateOfBirthProperty().bind (dateOfBirthPicker.valueProperty());
        model.getCitizenIdProperty().bind (CIDTextField.textProperty());
        model.getPhoneNumberProperty().bind (phoneNumberTextField.textProperty());
        model.getSexProperty().bind (genderChoose.valueProperty());
        model.getNationProperty().bind (nationChoose.valueProperty());
        model.getResidenceStatusProperty().bind (residenceStatusChoose.valueProperty());
 
        VBox vb = new VBox ( 18,
            gridPane,
            submitBtn
        );
        return vb;
    } 

    private List <String> getAllNation () {
        List<String> list = new ArrayList<>();
        list.add ("-- Chọn --");
        for (Nation nation : Nation.values()) {
            list.add (nation.toString());
        }
        return list;
    }
} 
