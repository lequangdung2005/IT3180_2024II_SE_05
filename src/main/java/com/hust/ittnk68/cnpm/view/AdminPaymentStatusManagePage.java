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
import com.hust.ittnk68.cnpm.model.CreatePaymentStatusModel;
import com.hust.ittnk68.cnpm.model.FindPaymentStatusModel;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.model.UpdatePaymentStatusModel;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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

public class AdminPaymentStatusManagePage extends DuongFXTabPane {

    public AdminPaymentStatusManagePage (ClientSceneController sceneController) {
        this.getTabs().add (new Tab ("Khai báo khoản nộp mới", createPaymentStatusCreateForm(sceneController)));
        this.getTabs().add (new Tab ("Tìm kiếm, chỉnh sửa hoặc xóa", createPaymentStatusSearchDeleteView(sceneController))); 
    }

    private VBox createPaymentStatusSearchDeleteView (ClientSceneController sceneController) {

        DuongFXIntegerField paymentStatusIdTf = new DuongFXIntegerField ();
        paymentStatusIdTf.setPromptText ("Payment status Id");

        DuongFXIntegerField expenseIdTf = new DuongFXIntegerField ();
        expenseIdTf.setPromptText ("Expense Id");

        DuongFXIntegerField familyIdTf = new DuongFXIntegerField ();
        familyIdTf.setPromptText ("Family Id");

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

        IconButton resetFilterBtn = new IconButton (new FontIcon (Material2MZ.REPLAY));
        resetFilterBtn.setOnAction (e -> {
            paymentStatusIdTf.setText("");
            expenseIdTf.setText("");
            familyIdTf.setText("");
            enableDayFilter.setSelected (false);
	    beginDatePicker.valueProperty().set (today);
	    endDatePicker.valueProperty().set (today);
        });

        FlowPane filters = new FlowPane ();
        filters.setVgap (8);
        filters.setHgap (4);
        filters.getChildren ().addAll (
            paymentStatusIdTf,
            expenseIdTf,
            familyIdTf,
	    new InputGroup (new Label("", enableDayFilter), beginDatePicker, new Label ("", new FontIcon (Material2OutlinedAL.KEYBOARD_ARROW_RIGHT)), endDatePicker),
            resetFilterBtn
        );

        Button searchBtn = new Button ("Tìm kiếm", new FontIcon (Material2MZ.PERSON_SEARCH));

        FindPaymentStatusModel model = sceneController.getFindPaymentStatusModel();
        model.paymentStatusIdProperty().bind (paymentStatusIdTf.textProperty ());
        model.expenseIdProperty().bind (expenseIdTf.textProperty ());
        model.familyIdProperty().bind (familyIdTf.textProperty ());
        model.enableDateFilterProperty().bind (enableDayFilter.selectedProperty());
        model.beginPublishedDateProperty().bind (beginDatePicker.valueProperty ());
        model.endPublishedDateProperty().bind (endDatePicker.valueProperty ());

        TableView<PaymentStatus> tableView = new TableView<>();
        sceneController.getFindPaymentStatusModel().setTableView (tableView);

        TableColumn<PaymentStatus, String> actionCol = new TableColumn<> ("Action");
        actionCol.setCellValueFactory (new PropertyValueFactory<> ("Dummy Value"));
        actionCol.setCellFactory (new Callback<TableColumn<PaymentStatus, String>, TableCell<PaymentStatus, String>>() {
            
            @Override
            public TableCell call(final TableColumn<PaymentStatus, String> param) {
                final TableCell<PaymentStatus, String> cell = new TableCell<>() {

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
                                PaymentStatus paymentStatus = getTableView().getItems().get(getIndex());
                                UpdatePaymentStatusModel model = sceneController.getUpdatePaymentStatusModel ();
                                model.setUpdatePaymentStatusWindow (new Stage ());
                                sceneController.openSubStage (model.getUpdatePaymentStatusWindow (), createPersonUpdateForm (sceneController, paymentStatus));
                            });
                            deleteBtn.setOnAction (event -> {
                                PaymentStatus paymentStatus = getTableView().getItems().get(getIndex());

                                ObjectMapper mapper = new ObjectMapper ();
                                mapper.enable (SerializationFeature.INDENT_OUTPUT);
                                String json = null;
                                try {
                                    json = mapper.writeValueAsString (paymentStatus);
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

                                AdminDeleteObject request = new AdminDeleteObject (sceneController.getToken(), paymentStatus);
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

        TableColumn<PaymentStatus, String> paymentStatusIdCol = new TableColumn<>("PaymentStatus Id");
        paymentStatusIdCol.setCellValueFactory (new PropertyValueFactory<> ("paymentStatusId"));

        TableColumn<PaymentStatus, String> expenseIdCol = new TableColumn<>("Expense Id");
        expenseIdCol.setCellValueFactory (new PropertyValueFactory<> ("expenseId"));

        TableColumn<PaymentStatus, String> familyIdCol = new TableColumn<>("Family Id");
        familyIdCol.setCellValueFactory (new PropertyValueFactory<> ("familyId"));

        TableColumn<PaymentStatus, String> totalPayCol = new TableColumn<>("Total Pay");
        totalPayCol.setCellValueFactory (new PropertyValueFactory<> ("totalPay"));

        TableColumn<PaymentStatus, String> publishedDateCol = new TableColumn<>("Published Date");
        publishedDateCol.setCellValueFactory (new PropertyValueFactory<> ("publishedDate"));

        tableView.getColumns().addAll (actionCol, paymentStatusIdCol, expenseIdCol, familyIdCol, totalPayCol, publishedDateCol);

        VBox vb = new VBox (18,
            new Text ("FILTER"),
            filters,
            searchBtn,
            tableView
        );

        searchBtn.setOnAction (e -> sceneController.getClientInteractor().findPaymentStatusHandler());

        return vb;
    }

    private VBox createPersonUpdateForm (ClientSceneController sceneController, PaymentStatus paymentStatus) {
        UpdatePaymentStatusModel model = sceneController.getUpdatePaymentStatusModel ();

        GridPane gridPane = new GridPane ();

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

        Text paymentStatusId = new Text ("Payment status Id:");
        gridPane.add (paymentStatusId, 0, 0, 2, 1);

        DuongFXIntegerField paymentStatusIdTextField = new DuongFXIntegerField ();
        paymentStatusIdTextField.setDisable (true);
        gridPane.add (paymentStatusIdTextField, 2, 0, 3, 1);

        Text expenseId = new Text ("Expense Id:");
        gridPane.add (expenseId, 0, 1, 2, 1);

        DuongFXIntegerField expenseIdTextField = new DuongFXIntegerField ();
        gridPane.add (expenseIdTextField, 2, 1, 3, 1);

        Text familyId = new Text ("Family Id:");
        gridPane.add (familyId, 0, 2, 2, 1);

        DuongFXIntegerField familyIdTextField = new DuongFXIntegerField ();
        gridPane.add (familyIdTextField, 2, 2, 3, 1);

        Text totalPay = new Text ("Total Pay:");
        gridPane.add (totalPay, 0, 3, 2, 1);

        DuongFXIntegerField totalPayTextField = new DuongFXIntegerField ();
        gridPane.add (totalPayTextField, 2, 3, 3, 1);

        Text publishedDate = new Text ("Published Date:");
        gridPane.add (publishedDate, 0, 4, 2, 1);

        LocalDate today = LocalDate.now (ZoneId.systemDefault());
	DatePicker publishedDatePicker = new DatePicker (today);
        gridPane.add (publishedDatePicker, 2, 4, 3, 1);

        Button submitBtn = new Button ("Tạo mới");
        submitBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        submitBtn.setOnAction (e -> sceneController.getClientInteractor().createPaymentStatusHandler ());

        Button updateBtn = new Button ("Cập nhật");
        updateBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateBtn.setOnAction (e -> sceneController.getClientInteractor().updatePaymentStatusHandler ());

        Button updateAndCloseBtn = new Button ("Cập nhật và đóng");
        updateAndCloseBtn.getStyleClass().addAll (Styles.SUCCESS, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateAndCloseBtn.setOnAction (e -> sceneController.getClientInteractor().updateAndClosePaymentStatusHandler ());

        Button closeBtn = new Button ("Đóng");
        closeBtn.getStyleClass().addAll (Styles.DANGER, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        closeBtn.setOnAction (e -> sceneController.getClientInteractor().closePaymentStatusUpdateHandler());

        paymentStatusIdTextField.setText (String.valueOf (paymentStatus.getPaymentStatusId()));
        expenseIdTextField.setText (String.valueOf (paymentStatus.getExpenseId()));
        familyIdTextField.setText (String.valueOf (paymentStatus.getFamilyId()));
        totalPayTextField.setText (String.valueOf (paymentStatus.getTotalPay()));
        publishedDatePicker.setValue(paymentStatus.getPublishedDate().convertToLocalDate());

        model.paymentStatusIdProperty().bind (paymentStatusIdTextField.textProperty ());
        model.expenseIdProperty().bind (expenseIdTextField.textProperty ());
        model.familyIdProperty().bind (familyIdTextField.textProperty ());
        model.totalPayProperty().bind (totalPayTextField.textProperty ());
        model.publishedDateProperty().bind (publishedDatePicker.valueProperty ());
 
        VBox vb = new VBox ( 18,
            gridPane,
            new HBox (18, updateBtn, updateAndCloseBtn, closeBtn)
        );
        vb.setPadding (new Insets (16));
        return vb;
    }

    private VBox createPaymentStatusCreateForm (ClientSceneController sceneController) {
        CreatePaymentStatusModel model = sceneController.getCreatePaymentStatusModel ();

        GridPane gridPane = new GridPane ();

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

        Text expenseId = new Text ("Expense Id:");
        gridPane.add (expenseId, 0, 0, 2, 1);

        DuongFXIntegerField expenseIdTextField = new DuongFXIntegerField ();
        gridPane.add (expenseIdTextField, 2, 0, 3, 1);

        Text familyId = new Text ("Family Id:");
        gridPane.add (familyId, 0, 1, 2, 1);

        DuongFXIntegerField familyIdTextField = new DuongFXIntegerField ();
        gridPane.add (familyIdTextField, 2, 1, 3, 1);

        Text totalPay = new Text ("Total Pay:");
        gridPane.add (totalPay, 0, 2, 2, 1);

        DuongFXIntegerField totalPayTextField = new DuongFXIntegerField ();
        gridPane.add (totalPayTextField, 2, 2, 3, 1);

        Text publishedDate = new Text ("Published Date:");
        gridPane.add (publishedDate, 0, 3, 2, 1);

        LocalDate today = LocalDate.now (ZoneId.systemDefault());
	DatePicker publishedDatePicker = new DatePicker (today);
        gridPane.add (publishedDatePicker, 2, 3, 3, 1);

        Button submitBtn = new Button ("Tạo mới");
        submitBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        submitBtn.setOnAction (e -> sceneController.getClientInteractor().createPaymentStatusHandler ());

        // bindings
        model.expenseIdProperty().bind (expenseIdTextField.textProperty ());
        model.familyIdProperty().bind (familyIdTextField.textProperty ());
        model.totalPayProperty().bind (totalPayTextField.textProperty ());
        model.publishedDateProperty().bind (publishedDatePicker.valueProperty ());
 
        VBox vb = new VBox ( 18,
            gridPane,
            submitBtn
        );
        return vb;
    } 
} 
