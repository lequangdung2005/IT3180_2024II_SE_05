package com.hust.ittnk68.cnpm.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.hust.ittnk68.cnpm.communication.AdminDeleteObject;
import com.hust.ittnk68.cnpm.communication.AdminFindObject;
import com.hust.ittnk68.cnpm.communication.ServerDeleteObjectResponse;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.CreatePersonModel;
import com.hust.ittnk68.cnpm.model.Person;
import com.hust.ittnk68.cnpm.type.Date;
import com.hust.ittnk68.cnpm.type.Nation;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.Sex;

import atlantafx.base.controls.MaskTextField;
import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class AdminPersonManagePage extends DuongFXTabPane {

    public AdminPersonManagePage (ClientSceneController sceneController) {
        this.getTabs().add (new Tab ("Khai báo thêm người", createPersonCreateForm(sceneController)));
        this.getTabs().add (new Tab ("Tìm kiếm, chỉnh sửa hoặc xóa", createPersonSearchDeleteView(sceneController))); 
    }

    private VBox createPersonSearchDeleteView (ClientSceneController sceneController) {

        DuongFXIntegerField personIdTf = new DuongFXIntegerField ();
        personIdTf.setPromptText ("Person Id");

        DuongFXIntegerField familyIdTf = new DuongFXIntegerField ();
        familyIdTf.setPromptText ("Family Id");

        TextField fullnameTf = new TextField ();
        fullnameTf.setPromptText ("Full name");

        // date of birth picker

        DuongFXIntegerField cidTf = new DuongFXIntegerField ();
        cidTf.setPromptText ("CCCD");

        DuongFXIntegerField phoneNumberTf = new DuongFXIntegerField ();
        phoneNumberTf.setPromptText ("SDT");

        ComboBox<String> genderChoose = new ComboBox<>();
        genderChoose.setItems (FXCollections.observableArrayList(
            "-- Chọn --", "Nam", "Nữ"
        ));
        genderChoose.getSelectionModel().selectFirst();

        ComboBox<String> nationChoose = new ComboBox<>();
        nationChoose.setItems (FXCollections.observableArrayList( getAllNation ()) );
        nationChoose.getSelectionModel().selectFirst();

        ComboBox<String> residenceStatusChoose = new ComboBox<> ();
        residenceStatusChoose.setItems (FXCollections.observableArrayList(
            "-- Chọn --",
            "Đang cư trú",
            "Tạm vắng"
        ));
        residenceStatusChoose.getSelectionModel().selectFirst();

        IconButton resetFilterBtn = new IconButton (new FontIcon (Material2MZ.REPLAY));
        resetFilterBtn.setOnAction (e -> {
            personIdTf.setText ("");
            familyIdTf.setText ("");
            fullnameTf.setText ("");
            cidTf.setText ("");
            phoneNumberTf.setText ("");
            genderChoose.getSelectionModel().selectFirst();
            nationChoose.getSelectionModel().selectFirst();
            residenceStatusChoose.getSelectionModel().selectFirst();
        });

        FlowPane filters = new FlowPane ();
        filters.setVgap (8);
        filters.setHgap (4);
        filters.getChildren ().addAll (
            new Text ("Bộ lọc"),
            personIdTf,
            familyIdTf,
            fullnameTf,
            cidTf,
            phoneNumberTf,
            genderChoose,
            nationChoose,
            residenceStatusChoose,
            resetFilterBtn
        );

        Button searchBtn = new Button ("Tìm kiếm", new FontIcon (Material2MZ.PERSON_SEARCH));

        TableView<Person> tableView = new TableView<>();

        TableColumn<Person, String> actionCol = new TableColumn<> ("Action");
        actionCol.setCellValueFactory (new PropertyValueFactory<> ("ActionDummy"));
        actionCol.setCellFactory (new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
            
            @Override
            public TableCell call(final TableColumn<Person, String> param) {
                final TableCell<Person, String> cell = new TableCell<>() {

                    final IconButton editBtn = new IconButton (new FontIcon (Material2OutlinedAL.CREATE));
                    final IconButton deleteBtn = new IconButton (new FontIcon (Material2OutlinedAL.DELETE));
                    final HBox hb = new HBox (1, editBtn, deleteBtn);

                    @Override
                    public void updateItem (String item, boolean empty) {
                        super.updateItem (item, empty);
                        setText (null);
                        if (empty) {
                            setGraphic (null);
                        }
                        else {
                            editBtn.setOnAction (event -> {
                                Person person = getTableView().getItems().get(getIndex());
                            });
                            deleteBtn.setOnAction (event -> {
                                Person person = getTableView().getItems().get(getIndex());

                                Alert alert = new Alert (AlertType.CONFIRMATION);
                                alert.setTitle ("Xác nhận xóa thông tin cư dân");
                                alert.setHeaderText ("Kiểm tra lại thông tin cư dân trước khi xóa");

                                ObjectMapper mapper = new ObjectMapper ();
                                mapper.enable (SerializationFeature.INDENT_OUTPUT);
                                try {
                                    alert.setContentText (mapper.writeValueAsString (person));
                                }
                                catch (Exception e) {
                                    e.printStackTrace ();
                                }

                                alert.initOwner (sceneController.getScene().getWindow());

                                Optional<ButtonType> result = alert.showAndWait ();
                                System.out.println (result.get());

                                if (result.get() != ButtonType.OK)
                                    return;

                                AdminDeleteObject request = new AdminDeleteObject (sceneController.getToken(), person);
                                ServerDeleteObjectResponse res = sceneController.deleteObject (request);
                                switch (res.getResponseStatus()) {
                                    case OK:
                                        getTableView().getItems().remove(getIndex());
                                        System.out.println ("xoa thanh cong");
                                        break;
                                    default:
                                        Alert fail = new Alert (AlertType.ERROR);
                                        fail.setTitle ("Thất bại");
                                        fail.setHeaderText (res.getResponseStatus().toString());
                                        fail.setContentText (res.getResponseMessage());
                                        fail.initOwner (sceneController.getScene().getWindow());
                                        fail.showAndWait ();

                                        System.out.println (res.getResponseStatus());
                                        System.out.println (res.getResponseMessage());
                                        break;
                                }
                            });
                            setGraphic (hb);
                        }
                    }
                };
                return cell;
            }

        });

        TableColumn<Person, String> personIdCol = new TableColumn<>("Person Id");
        personIdCol.setCellValueFactory (new PropertyValueFactory<> ("personId"));

        TableColumn<Person, String> familyIdCol = new TableColumn<>("Family Id");
        familyIdCol.setCellValueFactory (new PropertyValueFactory<> ("familyId"));

        TableColumn<Person, String> fullnameCol = new TableColumn<>("Full name");
        fullnameCol.setCellValueFactory (new PropertyValueFactory<>("fullname"));

        TableColumn<Person, String> dateOfBirthCol = new TableColumn<>("Date Of Birth (YYYY-MM-DD)");
        dateOfBirthCol.setCellValueFactory (new PropertyValueFactory<>("dateOfBirth"));

        TableColumn<Person, String> cidCol = new TableColumn<>("CCCD");
        cidCol.setCellValueFactory (new PropertyValueFactory<>("citizenIdentificationNumber"));

        TableColumn<Person, String> phoneNumberCol = new TableColumn<>("Phone Number");
        phoneNumberCol.setCellValueFactory (new PropertyValueFactory<>("phoneNumber"));

        TableColumn<Person, Sex> sexCol = new TableColumn<>("Sex");
        sexCol.setCellValueFactory (new PropertyValueFactory<>("sex"));

        TableColumn<Person, Nation> nationalityCol = new TableColumn<>("Nationality");
        nationalityCol.setCellValueFactory (new PropertyValueFactory<>("nationality"));

        TableColumn<Person, ResidenceStatus> residenceStatusCol = new TableColumn<>("Residence Status");
        residenceStatusCol.setCellValueFactory (new PropertyValueFactory<>("residenceStatus"));

        // tableView.setEditable (true);
        tableView.getColumns().addAll (actionCol, personIdCol, familyIdCol, fullnameCol, dateOfBirthCol, cidCol,
                                        phoneNumberCol, nationalityCol, residenceStatusCol);

        VBox vb = new VBox (18, filters, searchBtn, tableView);

        searchBtn.setOnAction (e -> {
            String personId = personIdTf.getText ();
            String familyId = familyIdTf.getText ();
            String fullname = fullnameTf.getText ();
            String citizenId = cidTf.getText ();
            String phoneNumber = phoneNumberTf.getText ();

            String sexString = genderChoose.getValue ();
            String nationString = nationChoose.getValue ();
            String residenceStatusString  = residenceStatusChoose.getValue ();

            Sex sex = null;
            if (sexString.equals("Nam"))
                sex = Sex.MALE;
            else if (sexString.equals("Nữ"))
                sex = Sex.FEMALE;

            Nation nation = null; 
            if (! Nation.matchByString(nationString).isEmpty()) {
                nation = Nation.matchByString (nationString).get();
            }

            ResidenceStatus residenceStatus = null;
            if (residenceStatusString.equals("Đang cư trú"))
                residenceStatus = ResidenceStatus.PRESENT;
            else if (residenceStatusString.equals("Tạm vắng"))
                residenceStatus = ResidenceStatus.ABSENT;

            StringBuilder conditionBuilder = new StringBuilder ("(1=1)");
            if (!personId.isEmpty()) {
                conditionBuilder.append (String.format (" AND (person_id='%s')", personId));
            }
            if (!familyId.isEmpty()) {
                conditionBuilder.append (String.format (" AND (family_id='%s')", familyId));
            }
            if (!fullname.isEmpty()) {
                conditionBuilder.append (String.format (" AND (fullname LIKE '%%%s%%')", fullname));
            }
            if (!citizenId.isEmpty()) {
                conditionBuilder.append (String.format (" AND (citizen_identification_number LIKE '%%%s%%')", citizenId));
            }
            if (!phoneNumber.isEmpty()) {
                conditionBuilder.append (String.format (" AND (phone_number LIKE '%%%s%%')", phoneNumber));
            }
            if (sex != null) {
                conditionBuilder.append (String.format (" AND (sex LIKE '%%%s%%')", sex));
            }
            if (nation != null) { 
                conditionBuilder.append (String.format (" AND (nationality LIKE '%%%s%%')", nation));
            }
            if (residenceStatus != null) { 
                conditionBuilder.append (String.format (" AND (residence_status LIKE '%%%s%%')", residenceStatus));
            }

            String condition = conditionBuilder.toString ();

            AdminFindObject request = new AdminFindObject (sceneController.getToken (),
                                            condition, new Person ());

            List<Map<String, Object>> requestResult = sceneController.findObject (request). getRequestResult ();

            List<Person> data = new ArrayList<>();
            for (Map<String, Object> map : requestResult) {
                data.add (Person.convertFromMap (map));
            }

            tableView.setItems (FXCollections.observableList (data));

        });

        return vb;
    }

    private VBox createPersonCreateForm (ClientSceneController sceneController) {

        CreatePersonModel model = sceneController.getCreatePersonModel ();

        GridPane gridPane = new GridPane ();

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
        dateOfBirthPicker.setEditable (true);
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
