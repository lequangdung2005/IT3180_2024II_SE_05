package com.hust.ittnk68.cnpm.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.hust.ittnk68.cnpm.communication.AdminDeleteObject;
import com.hust.ittnk68.cnpm.communication.ServerDeleteObjectResponse;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.CreatePersonModel;
import com.hust.ittnk68.cnpm.model.Person;
import com.hust.ittnk68.cnpm.model.UpdatePersonModel;
import com.hust.ittnk68.cnpm.type.Nation;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.Sex;

import atlantafx.base.controls.MaskTextField;
import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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

        DuongFXToggleDatePicker dateOfBirthPicker = DuongFXToggleDatePicker.create ();

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

        sceneController.getFindPersonModel().getPersonIdProperty().bind (personIdTf.textProperty());
        sceneController.getFindPersonModel().getFamilyIdProperty().bind (familyIdTf.textProperty());
        sceneController.getFindPersonModel().getFullnameProperty().bind (fullnameTf.textProperty());
        sceneController.getFindPersonModel().getEnableDateOfBirth().bind (dateOfBirthPicker.enableProperty());
        sceneController.getFindPersonModel().getDateOfBirthProperty().bind (dateOfBirthPicker.valueProperty());
        sceneController.getFindPersonModel().getCitizenIdProperty().bind (cidTf.textProperty());
        sceneController.getFindPersonModel().getPhoneNumberProperty().bind (phoneNumberTf.textProperty());
        sceneController.getFindPersonModel().getSexProperty().bind (genderChoose.valueProperty());
        sceneController.getFindPersonModel().getNationProperty().bind (nationChoose.valueProperty());
        sceneController.getFindPersonModel().getResidenceStatusProperty().bind (residenceStatusChoose.valueProperty());

        IconButton resetFilterBtn = new IconButton (new FontIcon (Material2MZ.REPLAY));
        resetFilterBtn.setOnAction (e -> {
            personIdTf.setText ("");
            familyIdTf.setText ("");
            fullnameTf.setText ("");
            dateOfBirthPicker.enableProperty().set (false);
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
            personIdTf,
            familyIdTf,
            fullnameTf,
            dateOfBirthPicker,
            cidTf,
            phoneNumberTf,
            genderChoose,
            nationChoose,
            residenceStatusChoose,
            resetFilterBtn
        );

        Button searchBtn = new Button ("Tìm kiếm", new FontIcon (Material2MZ.PERSON_SEARCH));

        TableView<Person> tableView = new TableView<>();
        sceneController.getFindPersonModel().setTableView (tableView);

        TableColumn<Person, String> actionCol = new TableColumn<> ("Action");
        actionCol.setCellValueFactory (new PropertyValueFactory<> ("Dummy Value"));
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

                            editBtn.getStyleClass().addAll(Styles.ACCENT);
                            deleteBtn.getStyleClass().addAll(Styles.DANGER);

                            editBtn.setOnAction (event -> {
                                Person person = getTableView().getItems().get(getIndex());
                                UpdatePersonModel model = sceneController.getUpdatePersonModel ();
                                model.setUpdatePersonWindow (new Stage ());
                                sceneController.openSubStage (model.getUpdatePersonWindow (), createPersonUpdateForm (sceneController, person));
                            });
                            deleteBtn.setOnAction (event -> {
                                Person person = getTableView().getItems().get(getIndex());

                                ObjectMapper mapper = new ObjectMapper ();
                                mapper.enable (SerializationFeature.INDENT_OUTPUT);
                                String json = null;
                                try {
                                    json = mapper.writeValueAsString (person);
                                }
                                catch (Exception e) {
                                    e.printStackTrace ();
                                }

                                Optional<ButtonType> result = sceneController.getClientInteractor()
                                                        .showConfirmationWindow("Xác nhận xóa cư dân",
                                                                                "Kiểm tra lại thông tin cư dân trước khi xóa",
                                                                                json);
                                System.out.println (result.get());

                                if (result.get() != ButtonType.OK)
                                    return;

                                AdminDeleteObject request = new AdminDeleteObject (sceneController.getUsername(), person);
                                ServerDeleteObjectResponse res = sceneController.deleteObject (request);
                                switch (res.getResponseStatus()) {
                                    case OK:
                                        getTableView().getItems().remove(getIndex());
                                        System.out.println ("xoa thanh cong");
                                        break;
                                    default:
                                        sceneController.getClientInteractor().showFailedWindow (res);

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

        VBox vb = new VBox (18,
            new Text ("FILTER"),
            filters,
            searchBtn,
            tableView
        );

        searchBtn.setOnAction (e -> sceneController.getClientInteractor().findPersonHandler());

        return vb;
    }

    private String stringToMaskCID (String text) {
        return String.format ("%s - %s - %s", text.subSequence (0, 4),
                                                text.subSequence (4, 8),
                                                text.subSequence (8, 12));
    }
    private String stringToMaskPhoneNumber (String text) {
        return String.format ("%s %s %s %s", text.subSequence (0, 3),
                                                text.subSequence (3, 6),
                                                text.subSequence (6, 8),
                                                text.subSequence (8, 10));
    }

    private VBox createPersonUpdateForm (ClientSceneController sceneController, Person person) {
        UpdatePersonModel model = sceneController.getUpdatePersonModel ();

        GridPane gridPane = new GridPane ();

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

        Text personId = new Text ("Person Id");
        gridPane.add (personId, 0, 0, 2, 1);

        DuongFXIntegerField personIdTextField = new DuongFXIntegerField ();
        personIdTextField.setDisable (true);
        gridPane.add (personIdTextField, 2, 0, 3, 1);

        Text familyId = new Text ("Family Id");
        gridPane.add (familyId, 0, 1, 2, 1);

        DuongFXIntegerField familyIdTextField = new DuongFXIntegerField ();
        gridPane.add (familyIdTextField, 2, 1, 3, 1);

        Text hoVaTen = new Text ("Họ và tên:");
        gridPane.add (hoVaTen, 0, 2, 2, 1);

        TextField fullNameTextField = new TextField ();
        gridPane.add (fullNameTextField, 2, 2, 3, 1);

        Text ngaySinh = new Text ("Ngày sinh:");
        gridPane.add (ngaySinh, 0, 3, 2, 1);

        LocalDate today = LocalDate.now (ZoneId.systemDefault());
        DatePicker dateOfBirthPicker = new DatePicker (today);
        dateOfBirthPicker.setEditable (true);
        gridPane.add (dateOfBirthPicker, 2, 3);

        Text canCuocCongDan = new Text ("Căn cước công dân:");
        gridPane.add (canCuocCongDan, 0, 4, 2, 1);

        MaskTextField CIDTextField = new MaskTextField("9999 - 9999 - 9999");
        CIDTextField.setLeft (new FontIcon (Material2MZ.PAYMENT));
        gridPane.add (CIDTextField, 2, 4, 3, 1);

        Text soDienThoai = new Text ("Số điện thoại:");
        gridPane.add (soDienThoai, 0, 5, 2, 1);

        MaskTextField phoneNumberTextField = new MaskTextField("999 999 99 99");
        phoneNumberTextField.setLeft (new FontIcon (Material2OutlinedMZ.PHONE));
        gridPane.add (phoneNumberTextField, 2, 5, 3, 1);

        Text gioiTinh = new Text ("Giới tính:");
        gridPane.add (gioiTinh, 0, 6, 2, 1);

        ComboBox<String> genderChoose = new ComboBox<>();
        genderChoose.setItems (FXCollections.observableArrayList(
            "Nam", "Nữ"
        ));
        genderChoose.getSelectionModel().selectFirst();
        gridPane.add (genderChoose, 2, 6, 3, 1);

        Text quocTich = new Text ("Quốc tịch:");
        gridPane.add (quocTich, 0, 7, 2, 1);

        ComboBox<String> nationChoose = new ComboBox<>();
        nationChoose.setItems (FXCollections.observableArrayList( getAllNation ()) );
        nationChoose.getSelectionModel().selectFirst();
        gridPane.add (nationChoose, 2, 7, 3, 1);

        Text tinhTrangCuTru = new Text ("Tình trạng cư trú:");
        gridPane.add (tinhTrangCuTru, 0, 8, 2, 1);

        ComboBox<String> residenceStatusChoose = new ComboBox<> ();
        residenceStatusChoose.setItems (FXCollections.observableArrayList(
            "Đang cư trú",
            "Tạm vắng"
        ));
        residenceStatusChoose.getSelectionModel().selectFirst();
        gridPane.add (residenceStatusChoose, 2, 8, 3, 1);

        Button updateBtn = new Button ("Cập nhật");
        updateBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateBtn.setOnAction (e -> sceneController.getClientInteractor().updatePersonHandler ());

        Button updateAndCloseBtn = new Button ("Cập nhật và đóng");
        updateAndCloseBtn.getStyleClass().addAll (Styles.SUCCESS, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateAndCloseBtn.setOnAction (e -> sceneController.getClientInteractor().updateAndClosePersonHandler ());

        Button closeBtn = new Button ("Đóng");
        closeBtn.getStyleClass().addAll (Styles.DANGER, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        closeBtn.setOnAction (e -> sceneController.getClientInteractor().closePersonUpdateHandler());

        personIdTextField.setText (String.valueOf(person.getPersonId ()));
        familyIdTextField.setText (String.valueOf(person.getFamilyId ()));
        fullNameTextField.setText (person.getFullname ());
        dateOfBirthPicker.setValue (person.getDateOfBirth().convertToLocalDate());
        CIDTextField.setText ( stringToMaskCID(person.getCitizenIdentificationNumber ()) );
        phoneNumberTextField.setText ( stringToMaskPhoneNumber(person.getPhoneNumber ()) );
        genderChoose.getSelectionModel().select ( person.getSex() == Sex.MALE ? 0 : 1 );
        nationChoose.getItems().stream()
                            .filter(nation -> nation.equals(person.getNationality().toString()))
                            .findAny()
                            .ifPresent(nationChoose.getSelectionModel()::select);
        residenceStatusChoose.getSelectionModel().select ( person.getResidenceStatus() == ResidenceStatus.PRESENT ? 0 : 1 );

        model.getPersonIdProperty().bind (personIdTextField.textProperty());
        model.getFamilyIdProperty().bind (familyIdTextField.textProperty());
        model.getFullnameProperty().bind (fullNameTextField.textProperty());
        model.getDateOfBirthProperty().bind (dateOfBirthPicker.valueProperty());
        model.getCitizenIdProperty().bind (CIDTextField.textProperty());
        model.getPhoneNumberProperty().bind (phoneNumberTextField.textProperty());
        model.getSexProperty().bind (genderChoose.valueProperty());
        model.getNationProperty().bind (nationChoose.valueProperty());
        model.getResidenceStatusProperty().bind (residenceStatusChoose.valueProperty());
 
        VBox vb = new VBox ( 18,
            gridPane,
            new HBox (18, updateBtn, updateAndCloseBtn, closeBtn)
        );
        vb.setPadding (new Insets (16));
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
