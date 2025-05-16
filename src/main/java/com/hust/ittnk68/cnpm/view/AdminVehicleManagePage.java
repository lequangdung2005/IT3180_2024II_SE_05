package com.hust.ittnk68.cnpm.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hust.ittnk68.cnpm.communication.AdminDeleteObject;
import com.hust.ittnk68.cnpm.communication.ServerDeleteObjectResponse;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.CreateVehicleModel;
import com.hust.ittnk68.cnpm.model.UpdateVehicleModel;
import com.hust.ittnk68.cnpm.model.FindVehicleModel;
import com.hust.ittnk68.cnpm.model.Vehicle;
import com.hust.ittnk68.cnpm.type.VehicleType;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import atlantafx.base.controls.Spacer;
import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AdminVehicleManagePage extends DuongFXTabPane {

    public AdminVehicleManagePage (ClientSceneController sceneController) {
        this.getTabs().add (new Tab ("Thêm phương tiện", createVehicleCreateForm (sceneController)));
        this.getTabs().add (new Tab ("Tìm kiếm, chỉnh sửa hoặc xóa", createVehicleSearchDeleteView(sceneController))); 
    }

    private VBox createVehicleSearchDeleteView(ClientSceneController sceneController) {

	DuongFXIntegerField familyId = new DuongFXIntegerField ();
	familyId.setPromptText ("Family ID");

        final ComboBox<String> vehicleTypeChoose = new ComboBox<>( FXCollections.observableList(Arrays.asList("-- Tất cả --", "Xe hơi", "Xe đạp", "Xe máy", "Xe đạp điện")) );
	vehicleTypeChoose.getSelectionModel().selectFirst ();

        TextField plateId = new TextField();
        plateId.setPromptText("Plate Id");

        sceneController.getFindVehicleModel().getFamilyId().bind (familyId.textProperty());
        sceneController.getFindVehicleModel().getVehicleType().bind (vehicleTypeChoose.valueProperty());
        sceneController.getFindVehicleModel().getPlateId().bind (plateId.textProperty());

        IconButton resetFilterBtn = new IconButton (new FontIcon (Material2MZ.REPLAY));
        resetFilterBtn.setOnAction (e -> {
	    familyId.setText ("");
            vehicleTypeChoose.getSelectionModel().selectFirst ();
	    plateId.setText ("");
        });

        FlowPane filters = new FlowPane ();
        filters.setVgap (8);
        filters.setHgap (4);
        filters.getChildren ().addAll (
            familyId,
            vehicleTypeChoose,
            plateId,
	    resetFilterBtn
        );

        Button searchBtn = new Button ("Tìm kiếm", new FontIcon (Material2MZ.PERSON_SEARCH));
        searchBtn.setOnAction (e -> sceneController.getClientInteractor().findVehicleHandler ());

        TableView<Vehicle> tableView = new TableView<>();
        sceneController.getFindVehicleModel().setTableView (tableView);

        TableColumn<Vehicle, String> actionCol = new TableColumn<> ("Action");
        actionCol.setCellValueFactory (new PropertyValueFactory<> ("Dummy Value"));
        actionCol.setCellFactory (new Callback<TableColumn<Vehicle, String>, TableCell<Vehicle, String>>() {
            
            @Override
            public TableCell call(final TableColumn<Vehicle, String> param) {
                final TableCell<Vehicle, String> cell = new TableCell<>() {

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
                                Vehicle vehicle = getTableView().getItems().get(getIndex());
                                UpdateVehicleModel model = sceneController.getUpdateVehicleModel();
                                model.setUpdateVehicleWindow (new Stage ());
                                sceneController.openSubStage (model.getUpdateVehicleWindow (), createVehicleUpdateForm (sceneController, vehicle));
                            });
                            deleteBtn.setOnAction (event -> {
                                Vehicle vehicle = getTableView().getItems().get(getIndex());

                                ObjectMapper mapper = new ObjectMapper ();
                                mapper.enable (SerializationFeature.INDENT_OUTPUT);
                                String json = null;
                                try {
                                    json = mapper.writeValueAsString (vehicle);
                                }
                                catch (Exception e) {
                                    e.printStackTrace ();
                                }

                                Optional<ButtonType> result = sceneController.getClientInteractor()
                                                        .showConfirmationWindow("Xác nhận xóa khoản thu",
                                                                                "Kiểm tra lại thông tin khoản thu trước khi xóa",
                                                                                json);
                                System.out.println (result.get());

                                if (result.get() != ButtonType.OK)
                                    return;

                                AdminDeleteObject request = new AdminDeleteObject (sceneController.getUsername(), vehicle);
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

        TableColumn<Vehicle, String> vehicleIdCol = new TableColumn<>("Vehicle Id");
        vehicleIdCol.setCellValueFactory (new PropertyValueFactory<> ("vehicleId"));

        TableColumn<Vehicle, String> vehicleTypeCol = new TableColumn<>("Vehicle Type");
        vehicleTypeCol.setCellValueFactory (new PropertyValueFactory<> ("vehicleType"));

        TableColumn<Vehicle, String> familyIdCol = new TableColumn<>("Family Id");
        familyIdCol.setCellValueFactory (new PropertyValueFactory<> ("familyId"));

        TableColumn<Vehicle, String> plateIdCol = new TableColumn<>("Plate Id");
        plateIdCol.setCellValueFactory (new PropertyValueFactory<> ("plateId"));

        tableView.getColumns().addAll (actionCol, vehicleIdCol, vehicleTypeCol, familyIdCol, plateIdCol);

        VBox vb = new VBox (18,
            new Text ("FILTER"),
            filters,
            searchBtn,
            tableView
        );

        return vb;
    }

    private VBox createVehicleUpdateForm (ClientSceneController sceneController, Vehicle vehicle) {
        GridPane gridPane = new GridPane ();

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

        Text vehicleId = new Text ("Vehicle ID:");
        gridPane.add (vehicleId, 0, 0, 2, 1);
        DuongFXIntegerField vehicleIdTf = new DuongFXIntegerField();
        vehicleIdTf.setEditable(false);
        gridPane.add (vehicleIdTf, 2, 0, 3, 1);

        Text familyId = new Text ("Family ID:");
        gridPane.add (familyId, 0, 1, 2, 1);

        DuongFXIntegerField familyIdTf = new DuongFXIntegerField();
        gridPane.add (familyIdTf, 2, 1, 3, 1);

        Text expenseDescription = new Text ("Vehicle Type:");
        gridPane.add (expenseDescription, 0, 2, 2, 1);

        // ComboBox<String> vehicleTypeChoose = new ComboBox<>( FXCollections.observableList(Arrays.asList("Xe hơi", "Xe đạp", "Xe máy", "Xe đạp điện")) );
        ComboBox<String> vehicleTypeChoose = new ComboBox<>();
        vehicleTypeChoose.setItems(FXCollections.observableList(   Arrays.asList (VehicleType.values()).stream().map (a -> a.toString()).toList()   ));
	vehicleTypeChoose.getSelectionModel().selectFirst ();
        gridPane.add (vehicleTypeChoose, 2, 2, 3, 1);

        Text publishedDate = new Text ("Plate ID:");
        gridPane.add (publishedDate, 0, 3, 2, 1);

        TextField plateIdTf = new TextField();
	gridPane.add (plateIdTf, 2, 3, 3, 1);

        Button updateBtn = new Button ("Cập nhật");
        updateBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateBtn.setOnAction (e -> sceneController.getClientInteractor().updateVehicleHandler());

        Button updateAndCloseBtn = new Button ("Cập nhật và đóng");
        updateAndCloseBtn.getStyleClass().addAll (Styles.SUCCESS, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateAndCloseBtn.setOnAction (e -> sceneController.getClientInteractor().updateAndCloseVehicleHandler ());

        Button closeBtn = new Button ("Đóng");
        closeBtn.getStyleClass().addAll (Styles.DANGER, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        closeBtn.setOnAction (e -> sceneController.getClientInteractor().closeVehicleUpdateHandler());

        vehicleIdTf.setText (String.valueOf(vehicle.getVehicleId ()));
        familyIdTf.setText (String.valueOf(vehicle.getFamilyId()));
	vehicleTypeChoose.getItems().stream()
				    .filter(string -> string.equals(vehicle.getVehicleType().toString()))
				    .findAny()
				    .ifPresent(vehicleTypeChoose.getSelectionModel()::select);
        plateIdTf.setText (vehicle.getPlateId());

        sceneController.getUpdateVehicleModel().getVehicleId().bind (vehicleIdTf.textProperty());
        sceneController.getUpdateVehicleModel().getFamilyId().bind (familyIdTf.textProperty());
        sceneController.getUpdateVehicleModel().getVehicleType().bind (vehicleTypeChoose.valueProperty());
        sceneController.getUpdateVehicleModel().getPlateId().bind (plateIdTf.textProperty());

        VBox vb = new VBox ( 18,
            gridPane,
            new HBox (18, updateBtn, updateAndCloseBtn, closeBtn)
        );
        vb.setPadding (new Insets (16));
        return vb;
    }


    private VBox createVehicleCreateForm (ClientSceneController sceneController) {

        CreateVehicleModel model = sceneController.getCreateVehicleModel ();

        GridPane gridPane = new GridPane ();

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

        Text familyId = new Text ("Family ID:");
        gridPane.add (familyId, 0, 0, 2, 1);

        DuongFXIntegerField familyIdTf = new DuongFXIntegerField();
        gridPane.add (familyIdTf, 2, 0, 3, 1);

        Text expenseDescription = new Text ("Vehicle Type:");
        gridPane.add (expenseDescription, 0, 1, 2, 1);

        ComboBox<String> vehicleTypeChoose = new ComboBox<>();
        vehicleTypeChoose = new ComboBox<>( FXCollections.observableList(Arrays.asList("Xe hơi", "Xe đạp", "Xe máy", "Xe đạp điện")) );
	vehicleTypeChoose.getSelectionModel().selectFirst ();
        gridPane.add (vehicleTypeChoose, 2, 1, 3, 1);

        Text publishedDate = new Text ("Plate ID:");
        gridPane.add (publishedDate, 0, 2, 2, 1);

        TextField plateIdTf = new TextField();
	gridPane.add (plateIdTf, 2, 2, 3, 1);

        Button submitBtn = new Button ("Thêm phương tiện");
        submitBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        submitBtn.setOnAction (e -> sceneController.getClientInteractor().createVehicleHandler ());

        model.getFamilyId().bind (familyIdTf.textProperty ());
        model.getVehicleType().bind (vehicleTypeChoose.valueProperty());
        model.getPlateId().bind (plateIdTf.textProperty());

        VBox vb = new VBox ( 18,
            gridPane,
            submitBtn
        );
        return vb;
    } 
} 
