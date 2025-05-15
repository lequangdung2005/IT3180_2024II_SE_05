package com.hust.ittnk68.cnpm.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hust.ittnk68.cnpm.communication.AdminDeleteObject;
import com.hust.ittnk68.cnpm.communication.ServerDeleteObjectResponse;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.CreateFamilyModel;
import com.hust.ittnk68.cnpm.model.Family;
import com.hust.ittnk68.cnpm.model.UpdateFamilyModel;
import com.hust.ittnk68.cnpm.type.Nation;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

public class AdminFamilyManagePage extends DuongFXTabPane {

    public AdminFamilyManagePage (ClientSceneController sceneController) {
        this.getTabs().add (new Tab ("Khai báo gia đình mới", createFamilyCreateForm(sceneController)));
        this.getTabs().add (new Tab ("Tìm kiếm, chỉnh sửa hoặc xóa", createFamilySearchDeleteView(sceneController))); 
    }

    private VBox createFamilySearchDeleteView (ClientSceneController sceneController) {

        DuongFXIntegerField familyIdTf = new DuongFXIntegerField ();
        familyIdTf.setPromptText ("Family Id");

        DuongFXIntegerField personIdTf = new DuongFXIntegerField ();
        personIdTf.setPromptText ("Person Id");

        TextField houseNumberTf = new TextField();
        houseNumberTf.setPromptText ("House Number");

        sceneController.getFindFamilyModel().familyIdProperty().bind (familyIdTf.textProperty());
        sceneController.getFindFamilyModel().personIdProperty().bind (personIdTf.textProperty());
        sceneController.getFindFamilyModel().houseNumberProperty().bind (houseNumberTf.textProperty());

        IconButton resetFilterBtn = new IconButton (new FontIcon (Material2MZ.REPLAY));
        resetFilterBtn.setOnAction (e -> {
            familyIdTf.setText ("");
            personIdTf.setText ("");
            houseNumberTf.setText ("");
        });

        FlowPane filters = new FlowPane ();
        filters.setVgap (8);
        filters.setHgap (4);
        filters.getChildren ().addAll (
            familyIdTf,
            personIdTf,
	    houseNumberTf,
            resetFilterBtn
        );

        Button searchBtn = new Button ("Tìm kiếm", new FontIcon (Material2MZ.PERSON_SEARCH));

        TableView<Family> tableView = new TableView<>();
        sceneController.getFindFamilyModel().setTableView (tableView);

        TableColumn<Family, String> actionCol = new TableColumn<> ("Action");
        actionCol.setCellValueFactory (new PropertyValueFactory<> ("Dummy Value"));
        actionCol.setCellFactory (new Callback<TableColumn<Family, String>, TableCell<Family, String>>() {
            
            @Override
            public TableCell call(final TableColumn<Family, String> param) {
                final TableCell<Family, String> cell = new TableCell<>() {

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
                                Family family = getTableView().getItems().get(getIndex());
                                UpdateFamilyModel model = sceneController.getUpdateFamilyModel ();
                                model.setUpdateFamilyWindow (new Stage ());
                                sceneController.openSubStage (model.getUpdateFamilyWindow (), createPersonUpdateForm (sceneController, family));
                            });
                            deleteBtn.setOnAction (event -> {
                                Family family = getTableView().getItems().get(getIndex());

                                ObjectMapper mapper = new ObjectMapper ();
                                mapper.enable (SerializationFeature.INDENT_OUTPUT);
                                String json = null;
                                try {
                                    json = mapper.writeValueAsString (family);
                                }
                                catch (Exception e) {
                                    e.printStackTrace ();
                                }

                                Optional<ButtonType> result = sceneController.getClientInteractor()
                                                        .showConfirmationWindow("Xác nhận xóa hộ gia đình",
                                                                                "Kiểm tra lại thông tin hộ gia đình trước khi xóa",
                                                                                json);
                                System.out.println (result.get());

                                if (result.get() != ButtonType.OK)
                                    return;

                                AdminDeleteObject request = new AdminDeleteObject (sceneController.getUsername(), family);
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

        TableColumn<Family, String> familyIdCol = new TableColumn<>("Family Id");
        familyIdCol.setCellValueFactory (new PropertyValueFactory<> ("familyId"));

        TableColumn<Family, String> personIdCol = new TableColumn<>("Person Id");
        personIdCol.setCellValueFactory (new PropertyValueFactory<> ("personId"));

        TableColumn<Family, String> houseNumberCol = new TableColumn<>("House Number");
        houseNumberCol.setCellValueFactory (new PropertyValueFactory<>("houseNumber"));


        // tableView.setEditable (true);
        tableView.getColumns().addAll (actionCol, familyIdCol, personIdCol, houseNumberCol);

        VBox vb = new VBox (18,
            new Text ("FILTER"),
            filters,
            searchBtn,
            tableView
        );

        searchBtn.setOnAction (e -> sceneController.getClientInteractor().findFamilyHandler());

        return vb;
    }

    private VBox createPersonUpdateForm (ClientSceneController sceneController, Family family) {
        UpdateFamilyModel model = sceneController.getUpdateFamilyModel ();

        GridPane gridPane = new GridPane ();

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

        Text familyId = new Text ("Family Id");
        gridPane.add (familyId, 0, 0, 2, 1);

        DuongFXIntegerField familyIdTextField = new DuongFXIntegerField ();
        familyIdTextField.setDisable (true);
        gridPane.add (familyIdTextField, 2, 0, 3, 1);

        Text personId = new Text ("Person Id (Chủ nhà):");
        gridPane.add (personId, 0, 1, 2, 1);

        DuongFXIntegerField personIdTextField = new DuongFXIntegerField ();
        gridPane.add (personIdTextField, 2, 1, 3, 1);

        Text houseNumber = new Text ("Số nhà:");
        gridPane.add (houseNumber, 0, 2, 2, 1);

        TextField houseNumberTextField = new TextField ();
        gridPane.add (houseNumberTextField, 2, 2, 3, 1);

        Button updateBtn = new Button ("Cập nhật");
        updateBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateBtn.setOnAction (e -> sceneController.getClientInteractor().updateFamilyHandler ());

        Button updateAndCloseBtn = new Button ("Cập nhật và đóng");
        updateAndCloseBtn.getStyleClass().addAll (Styles.SUCCESS, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateAndCloseBtn.setOnAction (e -> sceneController.getClientInteractor().updateAndCloseFamilyHandler ());

        Button closeBtn = new Button ("Đóng");
        closeBtn.getStyleClass().addAll (Styles.DANGER, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        closeBtn.setOnAction (e -> sceneController.getClientInteractor().closeFamilyUpdateHandler());

        familyIdTextField.setText (String.valueOf(family.getFamilyId ()));
        personIdTextField.setText (String.valueOf(family.getPersonId ()));
        houseNumberTextField.setText (family.getHouseNumber());

        model.personIdProperty().bind (personIdTextField.textProperty());
        model.familyIdProperty().bind (familyIdTextField.textProperty());
        model.houseNumberProperty().bind (houseNumberTextField.textProperty());
 
        VBox vb = new VBox ( 18,
            gridPane,
            new HBox (18, updateBtn, updateAndCloseBtn, closeBtn)
        );
        vb.setPadding (new Insets (16));
        return vb;
    }

    private VBox createFamilyCreateForm (ClientSceneController sceneController) {

        CreateFamilyModel model = sceneController.getCreateFamilyModel ();

        GridPane gridPane = new GridPane ();

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

        Text personId = new Text ("Person Id (Chủ nhà):");
        gridPane.add (personId, 0, 0, 2, 1);

        DuongFXIntegerField personIdTextField = new DuongFXIntegerField ();
        gridPane.add (personIdTextField, 2, 0, 3, 1);

        Text houseNumber = new Text ("Số nhà:");
        gridPane.add (houseNumber, 0, 1, 2, 1);

        TextField houseNumberTextField = new TextField ();
        gridPane.add (houseNumberTextField, 2, 1, 3, 1);

        Button submitBtn = new Button ("Tạo mới");
        submitBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        submitBtn.setOnAction (e -> sceneController.getClientInteractor().createFamilyHandler ());

        // bindings
        model.personIdProperty().bind (personIdTextField.textProperty());
        model.houseNumberProperty().bind (houseNumberTextField.textProperty());
 
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
