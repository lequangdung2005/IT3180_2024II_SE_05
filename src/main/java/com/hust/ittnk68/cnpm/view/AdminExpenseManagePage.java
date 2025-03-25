package com.hust.ittnk68.cnpm.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.hust.ittnk68.cnpm.communication.AdminDeleteObject;
import com.hust.ittnk68.cnpm.communication.ServerDeleteObjectResponse;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.CreateExpenseModel;
import com.hust.ittnk68.cnpm.model.Expense;
import com.hust.ittnk68.cnpm.model.UpdateExpenseModel;

import atlantafx.base.controls.PasswordTextField;
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

public class AdminExpenseManagePage extends DuongFXTabPane {

    public AdminExpenseManagePage (ClientSceneController sceneController) {
        this.getTabs().add (new Tab ("Tạo khoản thu", createExpenseCreateForm(sceneController)));
        this.getTabs().add (new Tab ("Tìm kiếm, chỉnh sửa hoặc xóa", createExpenseSearchDeleteView(sceneController))); 
    }

    private VBox createExpenseSearchDeleteView (ClientSceneController sceneController) {

	CheckBox enableDayFilter = new CheckBox ();

        LocalDate today = LocalDate.now (ZoneId.systemDefault());
	DatePicker beginDatePicker = new DatePicker (today);
        beginDatePicker.setEditable (true);
	DatePicker endDatePicker = new DatePicker (today);
        endDatePicker.setEditable (true);

	beginDatePicker.getEditor().setDisable (true);
	endDatePicker.getEditor().setDisable (true);
	enableDayFilter.selectedProperty().addListener (new ChangeListener<Boolean>() {
	    @Override
	    public void changed (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		beginDatePicker.getEditor().setDisable (!newValue);
		endDatePicker.getEditor().setDisable (!newValue);
	    }
	});

	DuongFXIntegerField beginTotalCostIntField = new DuongFXIntegerField ();
	beginTotalCostIntField.setPromptText ("Min total cost");
	DuongFXIntegerField endTotalCostIntField = new DuongFXIntegerField ();
	endTotalCostIntField.setPromptText ("Max total cost");

        ComboBox<String> expenseTypeChoose = new ComboBox<>();
        expenseTypeChoose.setItems (FXCollections.observableArrayList(
	    "-- Tất cả --", "service", "electric", "water", "parking", "donation"
        ));
        expenseTypeChoose.getSelectionModel().selectFirst();

	ToggleSwitch requiredSwitch = new ToggleSwitch ();
	requiredSwitch.setSelected (false);

        sceneController.getFindExpenseModel().enableDayFilterProperty().bind (enableDayFilter.selectedProperty());
        sceneController.getFindExpenseModel().beginPublishedDateProperty().bind (beginDatePicker.valueProperty());
        sceneController.getFindExpenseModel().endPublishedDateProperty().bind (endDatePicker.valueProperty());
        sceneController.getFindExpenseModel().minTotalCostProperty().bind (beginTotalCostIntField.textProperty());
        sceneController.getFindExpenseModel().maxTotalCostProperty().bind (endTotalCostIntField.textProperty());
        sceneController.getFindExpenseModel().expenseTypeProperty().bind (expenseTypeChoose.valueProperty());
        sceneController.getFindExpenseModel().requiredProperty().bind (requiredSwitch.selectedProperty());

        IconButton resetFilterBtn = new IconButton (new FontIcon (Material2MZ.REPLAY));
        resetFilterBtn.setOnAction (e -> {
	    enableDayFilter.setSelected (false);
	    beginDatePicker.valueProperty().set (today);
	    endDatePicker.valueProperty().set (today);
	    beginTotalCostIntField.setText ("");
	    endTotalCostIntField.setText ("");
	    expenseTypeChoose.getSelectionModel().selectFirst();
	    requiredSwitch.setSelected (false);
        });

        FlowPane filters = new FlowPane ();
        filters.setVgap (8);
        filters.setHgap (4);
        filters.getChildren ().addAll (
	    new InputGroup (new Label("", enableDayFilter), beginDatePicker, new Label ("", new FontIcon (Material2OutlinedAL.KEYBOARD_ARROW_RIGHT)), endDatePicker),
	    new InputGroup (beginTotalCostIntField, new Label ("", new FontIcon (Material2OutlinedAL.KEYBOARD_ARROW_RIGHT)), endTotalCostIntField),
	    new InputGroup (new Label("expense type"), expenseTypeChoose, new Label("required", requiredSwitch)),

	    resetFilterBtn
        );

        Button searchBtn = new Button ("Tìm kiếm", new FontIcon (Material2MZ.PERSON_SEARCH));
        searchBtn.setOnAction (e -> sceneController.getClientInteractor().findExpenseHandler());

        TableView<Expense> tableView = new TableView<>();
        sceneController.getFindExpenseModel().setTableView (tableView);

        TableColumn<Expense, String> actionCol = new TableColumn<> ("Action");
        actionCol.setCellValueFactory (new PropertyValueFactory<> ("Dummy Value"));
        actionCol.setCellFactory (new Callback<TableColumn<Expense, String>, TableCell<Expense, String>>() {
            
            @Override
            public TableCell call(final TableColumn<Expense, String> param) {
                final TableCell<Expense, String> cell = new TableCell<>() {

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
                                Expense expense = getTableView().getItems().get(getIndex());
                                UpdateExpenseModel model = sceneController.getUpdateExpenseModel ();
                                model.setUpdateExpenseWindow (new Stage ());
                                sceneController.openSubStage (model.getUpdateExpenseWindow (), createExpenseUpdateForm (sceneController, expense));
                            });
                            deleteBtn.setOnAction (event -> {
                                Expense expense = getTableView().getItems().get(getIndex());

                                ObjectMapper mapper = new ObjectMapper ();
                                mapper.enable (SerializationFeature.INDENT_OUTPUT);
                                String json = null;
                                try {
                                    json = mapper.writeValueAsString (expense);
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

                                AdminDeleteObject request = new AdminDeleteObject (sceneController.getToken(), expense);
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

        TableColumn<Expense, String> expenseIdCol = new TableColumn<>("Expense Id");
        expenseIdCol.setCellValueFactory (new PropertyValueFactory<> ("expenseId"));

        TableColumn<Expense, String> expenseTitleCol = new TableColumn<>("Expense Title");
        expenseTitleCol.setCellValueFactory (new PropertyValueFactory<> ("expenseTitle"));

        TableColumn<Expense, String> expenseDescriptionCol = new TableColumn<>("Expense description");
        expenseDescriptionCol.setCellValueFactory (new PropertyValueFactory<>("expenseDescription"));

        TableColumn<Expense, String> publishedDateCol = new TableColumn<>("Published date");
        publishedDateCol.setCellValueFactory (new PropertyValueFactory<>("publishedDate"));

        TableColumn<Expense, String> totalCostCol = new TableColumn<>("Total cost");
        totalCostCol.setCellValueFactory (new PropertyValueFactory<>("totalCost"));

        TableColumn<Expense, String> expenseTypeCol = new TableColumn<>("Expense type");
        expenseTypeCol.setCellValueFactory (new PropertyValueFactory<>("expenseType"));

        TableColumn<Expense, String> requiredCol = new TableColumn<>("Required?");
        requiredCol.setCellValueFactory (new PropertyValueFactory<>("required"));

        tableView.getColumns().addAll (actionCol, expenseIdCol, expenseTitleCol,
						expenseDescriptionCol, publishedDateCol, totalCostCol, expenseTypeCol, requiredCol);

        VBox vb = new VBox (18,
            new Text ("FILTER"),
            filters,
            searchBtn,
            tableView
        );

        return vb;
    }

    private VBox createExpenseUpdateForm (ClientSceneController sceneController, Expense expense) {
        UpdateExpenseModel model = sceneController.getUpdateExpenseModel ();

        GridPane gridPane = new GridPane ();

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

	Text expenseId = new Text ("Expense id:");
	gridPane.add (expenseId, 0, 0, 2, 1);

	TextField expenseIdTextField = new TextField ();
	expenseIdTextField.setDisable (true);
	gridPane.add (expenseIdTextField, 2, 0, 3, 1);
	
        Text expenseTitle = new Text ("Expense Title:");
        gridPane.add (expenseTitle, 0, 1, 2, 1);

        TextField expenseTitleTextField = new TextField ();
        gridPane.add (expenseTitleTextField, 2, 1, 3, 1);

        Text expenseDescription = new Text ("Expense description:");
        gridPane.add (expenseDescription, 0, 2, 2, 1);

        TextArea expenseDescriptionTextArea = new TextArea ();
        gridPane.add (expenseDescriptionTextArea, 2, 2, 3, 1);

        Text publishedDate = new Text ("Published date:");
        gridPane.add (publishedDate, 0, 3, 2, 1);

        LocalDate today = LocalDate.now (ZoneId.systemDefault());
        DatePicker publishedDatePicker = new DatePicker (today);
        publishedDatePicker.setEditable (true);
	gridPane.add (publishedDatePicker, 2, 3, 3, 1);

        Text totalCost = new Text ("Total Cost:");
        gridPane.add (totalCost, 0, 4, 2, 1);

	DuongFXIntegerField totalCostIntField = new DuongFXIntegerField ();
	gridPane.add (totalCostIntField, 2, 4, 3, 1);

	Text expenseType = new Text ("Expense type:");
        gridPane.add (expenseType, 0, 5, 2, 1);

        ComboBox<String> expenseTypeChoose = new ComboBox<>();
        expenseTypeChoose.setItems (FXCollections.observableArrayList(
	    "service", "electric", "water", "parking", "donation"
        ));
	expenseTypeChoose.getSelectionModel().selectFirst ();
	gridPane.add (expenseTypeChoose, 2, 5, 3, 1);

	Text required = new Text ("Required:");
	gridPane.add (required, 0, 6, 2, 1);

	ToggleSwitch requiredSwitch = new ToggleSwitch ();
	requiredSwitch.setSelected (false);
	gridPane.add (requiredSwitch, 2, 6, 3, 1);


        Button updateBtn = new Button ("Cập nhật");
        updateBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateBtn.setOnAction (e -> sceneController.getClientInteractor().updateExpenseHandler ());

        Button updateAndCloseBtn = new Button ("Cập nhật và đóng");
        updateAndCloseBtn.getStyleClass().addAll (Styles.SUCCESS, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateAndCloseBtn.setOnAction (e -> sceneController.getClientInteractor().updateAndCloseExpenseHandler ());

        Button closeBtn = new Button ("Đóng");
        closeBtn.getStyleClass().addAll (Styles.DANGER, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        closeBtn.setOnAction (e -> sceneController.getClientInteractor().closeExpenseUpdateHandler());

	expenseIdTextField.setText (String.valueOf (expense.getExpenseId ()));
	expenseTitleTextField.setText (String.valueOf (expense.getExpenseTitle ()));
	expenseDescriptionTextArea.setText (String.valueOf (expense.getExpenseDescription ()));
	publishedDatePicker.setValue ( expense.getPublishedDate ().convertToLocalDate() );
	totalCostIntField.setText ( String.valueOf (expense.getTotalCost()) );
	requiredSwitch.setSelected ( expense.getRequired() );

	model.expenseIdProperty().bind(expenseIdTextField.textProperty());
	model.expenseTitleProperty().bind(expenseTitleTextField.textProperty());
	model.expenseDescriptionProperty().bind(expenseDescriptionTextArea.textProperty());
	model.publishedDateProperty().bind(publishedDatePicker.valueProperty());
	model.totalCostProperty().bind(totalCostIntField.textProperty());
	model.expenseTypeProperty().bind(expenseTypeChoose.valueProperty());
	model.requiredProperty().bind(requiredSwitch.selectedProperty());

        VBox vb = new VBox ( 18,
            gridPane,
            new HBox (18, updateBtn, updateAndCloseBtn, closeBtn)
        );
        vb.setPadding (new Insets (16));
        return vb;
    }

    private VBox createExpenseCreateForm (ClientSceneController sceneController) {

        CreateExpenseModel model = sceneController.getCreateExpenseModel ();

        GridPane gridPane = new GridPane ();

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

        Text expenseTitle = new Text ("Expense Title:");
        gridPane.add (expenseTitle, 0, 0, 2, 1);

        TextField expenseTitleTextField = new TextField ();
        gridPane.add (expenseTitleTextField, 2, 0, 3, 1);

        Text expenseDescription = new Text ("Expense description:");
        gridPane.add (expenseDescription, 0, 1, 2, 1);

        TextArea expenseDescriptionTextArea = new TextArea ();
        gridPane.add (expenseDescriptionTextArea, 2, 1, 3, 1);

        Text publishedDate = new Text ("Published date:");
        gridPane.add (publishedDate, 0, 2, 2, 1);

        LocalDate today = LocalDate.now (ZoneId.systemDefault());
        DatePicker publishedDatePicker = new DatePicker (today);
        publishedDatePicker.setEditable (true);
	gridPane.add (publishedDatePicker, 2, 2, 3, 1);

        Text totalCost = new Text ("Total Cost:");
        gridPane.add (totalCost, 0, 3, 2, 1);

	DuongFXIntegerField totalCostIntField = new DuongFXIntegerField ();
	gridPane.add (totalCostIntField, 2, 3, 3, 1);

	Text expenseType = new Text ("Expense type:");
        gridPane.add (expenseType, 0, 4, 2, 1);

        ComboBox<String> expenseTypeChoose = new ComboBox<>();
        expenseTypeChoose.setItems (FXCollections.observableArrayList(
	    "-- Chọn --", "service", "electric", "water", "parking", "donation"
        ));
	expenseTypeChoose.getSelectionModel().selectFirst ();
	gridPane.add (expenseTypeChoose, 2, 4, 3, 1);

	Text required = new Text ("Required:");
	gridPane.add (required, 0, 5, 2, 1);

	ToggleSwitch requiredSwitch = new ToggleSwitch ();
	requiredSwitch.setSelected (false);
	gridPane.add (requiredSwitch, 2, 5, 3, 1);

        Button submitBtn = new Button ("Tạo khoản thu");
        submitBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        submitBtn.setOnAction (e -> sceneController.getClientInteractor().createExpenseHandler());

        // bindings
        model.expenseTitleProperty().bind (expenseTitleTextField.textProperty());
        model.expenseDescriptionProperty().bind (expenseDescriptionTextArea.textProperty());
        model.publishedDateProperty().bind (publishedDatePicker.valueProperty());
        model.totalCostProperty().bind (totalCostIntField.textProperty());
        model.expenseTypeProperty().bind (expenseTypeChoose.valueProperty());
	model.requiredProperty().bind (requiredSwitch.selectedProperty ());

        VBox vb = new VBox ( 18,
            gridPane,
            submitBtn
        );
        return vb;
    } 
} 
