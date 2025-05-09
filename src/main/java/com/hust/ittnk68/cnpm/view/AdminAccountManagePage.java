package com.hust.ittnk68.cnpm.view;

import java.util.Optional;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.hust.ittnk68.cnpm.communication.AdminDeleteObject;
import com.hust.ittnk68.cnpm.communication.ServerDeleteObjectResponse;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.CreateAccountModel;
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.UpdateAccountModel;

import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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

public class AdminAccountManagePage extends DuongFXTabPane {

    public AdminAccountManagePage (ClientSceneController sceneController) {
        this.getTabs().add (new Tab ("Đăng ký tài khoản", createAccountCreateForm(sceneController)));
        this.getTabs().add (new Tab ("Tìm kiếm, chỉnh sửa hoặc xóa", createAccountSearchDeleteView(sceneController))); 
    }

    private VBox createAccountSearchDeleteView (ClientSceneController sceneController) {

        DuongFXIntegerField accountIdTf = new DuongFXIntegerField ();
        accountIdTf.setPromptText ("Account Id");

        DuongFXIntegerField familyIdTf = new DuongFXIntegerField ();
        familyIdTf.setPromptText ("Family Id");

        TextField usernameTf = new TextField ();
	usernameTf.setPromptText ("Username");

        ComboBox<String> accountTypeChoose = new ComboBox<>();
        accountTypeChoose.setItems (FXCollections.observableArrayList(
	    "-- Tất cả --", "user", "admin", "root"
        ));
        accountTypeChoose.getSelectionModel().selectFirst();

        sceneController.getFindAccountModel().accountIdProperty().bind (accountIdTf.textProperty());
        sceneController.getFindAccountModel().familyIdProperty().bind (familyIdTf.textProperty());
        sceneController.getFindAccountModel().usernameProperty().bind (usernameTf.textProperty());
        sceneController.getFindAccountModel().accountTypeProperty().bind (accountTypeChoose.valueProperty());

        IconButton resetFilterBtn = new IconButton (new FontIcon (Material2MZ.REPLAY));
        resetFilterBtn.setOnAction (e -> {
            accountIdTf.setText ("");
            familyIdTf.setText ("");
            usernameTf.setText ("");
	    accountTypeChoose.getSelectionModel().selectFirst ();
        });

        FlowPane filters = new FlowPane ();
        filters.setVgap (8);
        filters.setHgap (4);
        filters.getChildren ().addAll (
	    accountIdTf,
	    familyIdTf,
	    usernameTf,
	    accountTypeChoose,
            resetFilterBtn
        );

        Button searchBtn = new Button ("Tìm kiếm", new FontIcon (Material2MZ.PERSON_SEARCH));
        searchBtn.setOnAction (e -> sceneController.getClientInteractor().findAccountHandler());

        TableView<Account> tableView = new TableView<>();
        sceneController.getFindAccountModel().setTableView (tableView);

        TableColumn<Account, String> actionCol = new TableColumn<> ("Action");
        actionCol.setCellValueFactory (new PropertyValueFactory<> ("Dummy Value"));
        actionCol.setCellFactory (new Callback<TableColumn<Account, String>, TableCell<Account, String>>() {
            
            @Override
            public TableCell call(final TableColumn<Account, String> param) {
                final TableCell<Account, String> cell = new TableCell<>() {

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
                                Account account = getTableView().getItems().get(getIndex());
                                UpdateAccountModel model = sceneController.getUpdateAccountModel ();
                                model.setUpdateAccountWindow (new Stage ());
                                sceneController.openSubStage (model.getUpdateAccountWindow (), createAccountUpdateForm (sceneController, account));
                            });
                            deleteBtn.setOnAction (event -> {
                                Account account = getTableView().getItems().get(getIndex());

                                ObjectMapper mapper = new ObjectMapper ();
                                mapper.enable (SerializationFeature.INDENT_OUTPUT);
                                String json = null;
                                try {
                                    json = mapper.writeValueAsString (account);
                                }
                                catch (Exception e) {
                                    e.printStackTrace ();
                                }

                                Optional<ButtonType> result = sceneController.getClientInteractor()
                                                        .showConfirmationWindow("Xác nhận xóa tài khoản",
                                                                                "Kiểm tra lại thông tin tài khoản trước khi xóa",
                                                                                json);
                                System.out.println (result.get());

                                if (result.get() != ButtonType.OK)
                                    return;

                                AdminDeleteObject request = new AdminDeleteObject (sceneController.getUsername(), account);
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

        TableColumn<Account, String> accountIdCol = new TableColumn<>("Account Id");
        accountIdCol.setCellValueFactory (new PropertyValueFactory<> ("accountId"));

        TableColumn<Account, String> familyIdCol = new TableColumn<>("Family Id");
        familyIdCol.setCellValueFactory (new PropertyValueFactory<> ("familyId"));

        TableColumn<Account, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory (new PropertyValueFactory<>("username"));

        TableColumn<Account, String> digestPasswordCol = new TableColumn<>("Digest password");
        digestPasswordCol.setCellValueFactory (new PropertyValueFactory<>("digestPassword"));

        TableColumn<Account, String> accountTypeCol = new TableColumn<>("Account type");
        accountTypeCol.setCellValueFactory (new PropertyValueFactory<>("accountType"));

        tableView.getColumns().addAll (actionCol, accountIdCol, familyIdCol, usernameCol, digestPasswordCol, accountTypeCol);

        VBox vb = new VBox (18,
            new Text ("FILTER"),
            filters,
            searchBtn,
            tableView
        );

        return vb;
    }

    private VBox createAccountUpdateForm (ClientSceneController sceneController, Account account) {
        UpdateAccountModel model = sceneController.getUpdateAccountModel ();

        GridPane gridPane = new GridPane ();

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

        Text accountId = new Text ("Account Id:");
        gridPane.add (accountId, 0, 0, 2, 1);

        DuongFXIntegerField accountIdTextField = new DuongFXIntegerField ();
        accountIdTextField.setDisable (true);
        gridPane.add (accountIdTextField, 2, 0, 3, 1);

        Text familyId = new Text ("Family Id:");
        gridPane.add (familyId, 0, 1, 2, 1);

        DuongFXIntegerField familyIdTextField = new DuongFXIntegerField ();
        gridPane.add (familyIdTextField, 2, 1, 3, 1);

        Text username = new Text ("Username:");
        gridPane.add (username, 0, 2, 2, 1);

        TextField usernameTextField = new TextField ();
        gridPane.add (usernameTextField, 2, 2, 3, 1);

        Text password = new Text ("Password:");
        gridPane.add (password, 0, 3, 2, 1);

	PasswordTextField passwordTextField = new PasswordTextField ();
	gridPane.add (passwordTextField, 2, 3, 3, 1);

        Text reTypePassword = new Text ("Re-type Password:");
        gridPane.add (reTypePassword, 0, 4, 2, 1);

	PasswordTextField reTypePasswordTextField = new PasswordTextField ();
	gridPane.add (reTypePasswordTextField, 2, 4, 3, 1);

	Text accountType = new Text ("Account type:");
        gridPane.add (accountType, 0, 5, 2, 1);

        ComboBox<String> accountTypeChoose = new ComboBox<>();
        accountTypeChoose.setItems (FXCollections.observableArrayList(
	    "user", "admin", "root"
        ));
	gridPane.add (accountTypeChoose, 2, 5, 3, 1);

        Button updateBtn = new Button ("Cập nhật");
        updateBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateBtn.setOnAction (e -> sceneController.getClientInteractor().updateAccountHandler ());

        Button updateAndCloseBtn = new Button ("Cập nhật và đóng");
        updateAndCloseBtn.getStyleClass().addAll (Styles.SUCCESS, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        updateAndCloseBtn.setOnAction (e -> sceneController.getClientInteractor().updateAndCloseAccountHandler ());

        Button closeBtn = new Button ("Đóng");
        closeBtn.getStyleClass().addAll (Styles.DANGER, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        closeBtn.setOnAction (e -> sceneController.getClientInteractor().closeAccountUpdateHandler());

	accountIdTextField.setText (String.valueOf (account.getAccountId()));
	familyIdTextField.setText (String.valueOf (account.getFamilyId()));
	usernameTextField.setText (account.getUsername ());
	passwordTextField.setText ("");
	reTypePasswordTextField.setText ("");
	accountTypeChoose.getItems().stream()
				    .filter(accountTypeString -> accountTypeString.equals(account.getAccountType().toString()))
				    .findAny()
				    .ifPresent(accountTypeChoose.getSelectionModel()::select);

	model.accountIdProperty().bind(accountIdTextField.textProperty());
	model.familyIdProperty().bind(familyIdTextField.textProperty());
	model.usernameProperty().bind(usernameTextField.textProperty());
	model.passwordProperty().bind(passwordTextField.passwordProperty());
	model.password2Property().bind(reTypePasswordTextField.passwordProperty());
	model.accountTypeProperty().bind(accountTypeChoose.valueProperty());
	model.setDigestPassword (account.getDigestPassword ());

        VBox vb = new VBox ( 18,
            gridPane,
            new HBox (18, updateBtn, updateAndCloseBtn, closeBtn)
        );
        vb.setPadding (new Insets (16));
        return vb;
    }

    private VBox createAccountCreateForm (ClientSceneController sceneController) {

        CreateAccountModel model = sceneController.getCreateAccountModel ();

        GridPane gridPane = new GridPane ();

        gridPane.setVgap (10);
        gridPane.setHgap (10);
        // gridPane.setGridLinesVisible (true);

        Text familyId = new Text ("Family Id:");
        gridPane.add (familyId, 0, 0, 2, 1);

        DuongFXIntegerField familyIdTextField = new DuongFXIntegerField ();
        gridPane.add (familyIdTextField, 2, 0, 3, 1);

        Text username = new Text ("Username:");
        gridPane.add (username, 0, 1, 2, 1);

        TextField usernameTextField = new TextField ();
        gridPane.add (usernameTextField, 2, 1, 3, 1);

        Text password = new Text ("Password:");
        gridPane.add (password, 0, 2, 2, 1);

	PasswordTextField passwordTextField = new PasswordTextField ();
	gridPane.add (passwordTextField, 2, 2, 3, 1);

        Text reTypePassword = new Text ("Re-type Password:");
        gridPane.add (reTypePassword, 0, 3, 2, 1);

	PasswordTextField reTypePasswordTextField = new PasswordTextField ();
	gridPane.add (reTypePasswordTextField, 2, 3, 3, 1);

	Text accountType = new Text ("Account type:");
        gridPane.add (accountType, 0, 4, 2, 1);

        ComboBox<String> accountTypeChoose = new ComboBox<>();
        accountTypeChoose.setItems (FXCollections.observableArrayList(
	    "user", "admin", "root"
        ));
	gridPane.add (accountTypeChoose, 2, 4, 3, 1);

        Button submitBtn = new Button ("Đăng ký");
        submitBtn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED, Styles.ROUNDED);
        submitBtn.setOnAction (e -> sceneController.getClientInteractor().createAccountHandler());

        // bindings
        model.familyIdProperty().bind (familyIdTextField.textProperty());
        model.usernameProperty().bind (usernameTextField.textProperty());
        model.passwordProperty().bind (passwordTextField.passwordProperty());
        model.password2Property().bind (reTypePasswordTextField.passwordProperty());
        model.accountTypeProperty().bind (accountTypeChoose.valueProperty());

        VBox vb = new VBox ( 18,
            gridPane,
            submitBtn
        );
        return vb;
    } 
} 
