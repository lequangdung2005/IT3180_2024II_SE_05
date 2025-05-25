package com.hust.ittnk68.cnpm.interactor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hust.ittnk68.cnpm.communication.AdminCreateObject;
import com.hust.ittnk68.cnpm.communication.AdminFindObject;
import com.hust.ittnk68.cnpm.communication.AdminUpdateObject;
import com.hust.ittnk68.cnpm.communication.ClientMessageStartSession;
import com.hust.ittnk68.cnpm.communication.ServerCreateObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerResponseBase;
import com.hust.ittnk68.cnpm.communication.ServerResponseStartSession;
import com.hust.ittnk68.cnpm.communication.ServerUpdateObjectResponse;
import com.hust.ittnk68.cnpm.controller.AdminHomeScreenController;
import com.hust.ittnk68.cnpm.controller.ClientHomeScreenController;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.controller.LoginController;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.model.*;
import com.hust.ittnk68.cnpm.type.*;
import com.hust.ittnk68.cnpm.view.LoadingView;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClientInteractor {
    ClientModel model;
    ClientSceneController sceneController;
    AnchorPane mainScreen;

    public ClientInteractor (ClientSceneController sceneController) {
        this.model = sceneController.getClientModel ();
        this.sceneController = sceneController;
        this.sceneController = sceneController;
        this.mainScreen = new AnchorPane ();
    }

    public void loginHandler() {
        String username = model.getUsername();
        String password = model.getPassword();
        if(username == null || username.trim().length() == 0) {
            System.out.println("no username");
            sceneController.openWarningNoti ("Quý khách vui lòng nhập tài khoản.",
                                            new FontIcon (Material2AL.FLAG));
        }
        else if(password == null || password.trim().length() == 0) {
            System.out.println("no password");
            sceneController.openWarningNoti ("Quý khách vui lòng nhập mật khẩu.",
                                            new FontIcon (Material2AL.FLAG));
        }
        else {

            Task<ServerResponseStartSession> startSessionTask = new Task<>() {
                @Override
                public ServerResponseStartSession call () {
                    String digestPassword = DigestUtils.sha256Hex(password);
                    ClientMessageStartSession message = new ClientMessageStartSession (username, digestPassword);
                    updateMessage ("Login ...");
                    return sceneController.startSession (message);
                }
            };

            LoadingView root = new LoadingView (startSessionTask);
            Stage stage = new Stage ();
            sceneController.openSubStage (stage, root);

            startSessionTask.setOnSucceeded (e -> {
                stage.close ();

                ServerResponseStartSession res = startSessionTask.getValue ();
                if (res.getResponseStatus ().equals (ResponseStatus.CANT_CONNECT_SERVER))
                {
                    System.out.println ("ko ket noi den server dc");
                    sceneController.openWarningNoti ("Không thể kết nối đến server.",
                                                    new FontIcon (Material2AL.FLAG));
                    return;
                } 
     
                if (res.getResponseStatus ().equals (ResponseStatus.SESSION_ERROR))
                {
                    System.out.println ("tai khoan dang dc dang nhap ...");
                    sceneController.openWarningNoti ("Tài khoản đang được đăng nhập trên thiết bị khác.",
                                                     new FontIcon (Material2AL.FLAG));
                    return;
                }

                sceneController.setToken (res.getToken ());
                System.out.println ("token = " + sceneController.getToken ());

                if(res.getAccountType().equals(AccountType.UNVALID)) {
                    System.out.println("dang nhap lai");
                }
                else if(res.getAccountType().equals(AccountType.ADMIN) || res.getAccountType().equals(AccountType.ROOT)) {
                    System.out.println("admin");
                    sceneController.setRoot(new AdminHomeScreenController(sceneController).getView());
                }
                else if(res.getAccountType().equals(AccountType.USER)) {
                    System.out.println("user");
                    sceneController.setRoot(new ClientHomeScreenController(sceneController).getView());
                }
            });

            new Thread (startSessionTask).start ();
        }
    }

    public void forgetPasswordButtonHandler() {
        System.out.println("forget");
    }

    public void logout () {
        // delete used token
        sceneController.getClientModel ().setToken (null);
        sceneController.setRoot (new LoginController (sceneController).getView ());
    }

    public void setRoot (Parent p) {
        this.mainScreen.getChildren().clear();
        this.mainScreen.getChildren().add (p);
        AnchorPane.setLeftAnchor (p, 0.0);
        AnchorPane.setTopAnchor (p, 0.0);
        AnchorPane.setRightAnchor (p, 0.0);
        AnchorPane.setBottomAnchor (p, 0.0);
    }

    public AnchorPane getMainScreen() {
        return mainScreen;
    }

    public Optional<ButtonType> showConfirmationWindow (String title, String headerText, String contentText) {
        Alert alert = new Alert (AlertType.CONFIRMATION);
        alert.setTitle (title);
        alert.setHeaderText (headerText);
        alert.setContentText (contentText);
        alert.initOwner (sceneController.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait ();
        return result;
    }

    public Optional<ButtonType> showFailedWindow (ServerResponseBase res) {
        Alert fail = new Alert (AlertType.ERROR);
        fail.setTitle ("Yêu cầu không thể thực hiện");
        fail.setHeaderText (res.getResponseStatus().toString());
        fail.setContentText (res.getResponseMessage());
        fail.initOwner (sceneController.getScene().getWindow());
        return fail.showAndWait ();
    }

    private void notificateUpdateSuccessfully () {
        notificate ("Cập nhật thành công", "Cập nhật thành công");
    }
    
    public void notificate (String title, String header) {
        Alert success = new Alert (AlertType.INFORMATION);
        success.setTitle (title);
        success.setHeaderText (header);
        success.initOwner (sceneController.getScene().getWindow());
        success.showAndWait ();
    }

    public void createPersonHandler () {
        CreatePersonModel model = sceneController.getCreatePersonModel ();

        String fullname = model.getFullnameProperty().get();
        fullname = fullname.trim ();
        fullname = fullname.replaceAll ("^ +| +$|( )+", "$1");

        String familyId = model.getFamilyIdProperty().get();

        Date dateOfBirth = Date.convertFromLocalDate( model.getDateOfBirthProperty().get() );

        String citizenId = model.getCitizenIdProperty().get();
        citizenId = citizenId.replace (" ", "");
        citizenId = citizenId.replace ("-", "");

        String phoneNumber = model.getPhoneNumberProperty().get();
        phoneNumber = phoneNumber.replace (" ", "");

        String sexString = model.getSexProperty().get();
        String nationString = model.getNationProperty().get();
        String residenceStatusString = model.getResidenceStatusProperty().get();

        if (fullname.isEmpty()) {
            System.out.println ("Chua nhap ten");
            return;
        }

        if (familyId.isEmpty()) {
            System.out.println ("Chua nhap family id");
            return;
        }

        if (citizenId.contains("_")) {
            System.out.println ("Ma so CCCD chua hop le");
            return;
        }

        if (phoneNumber.contains("_")) {
            System.out.println ("Sdt chua hop le");
            return;
        }

        Sex sex = null;
        if (sexString.equals("Nam"))
            sex = Sex.MALE;
        else if (sexString.equals("Nữ"))
            sex = Sex.FEMALE;
        else {
            System.out.println ("Chua chon gioi tinh");
            return;
        }

        if (Nation.matchByString(nationString).isEmpty()) {
            System.out.println ("Chua chon quoc tich");
            return;
        }
        Nation nation = Nation.matchByString (nationString).get();

        ResidenceStatus residenceStatus = null;
        if (residenceStatusString.equals("Đang cư trú"))
            residenceStatus = ResidenceStatus.PRESENT;
        else if (residenceStatusString.equals("Tạm vắng"))
            residenceStatus = ResidenceStatus.ABSENT;
        else {
            System.out.println ("Chua chon tinh trang cu tru");
            return;
        }

        Person object = new Person (Integer.parseInt(familyId), fullname,
                                        dateOfBirth, citizenId,
                                        phoneNumber, sex,
                                        nation, residenceStatus);
        AdminCreateObject req = new AdminCreateObject (sceneController.getUsername(), object);

        ObjectMapper mapper = new ObjectMapper ();
        try {
            String a = mapper.writeValueAsString (req.getObject ());
            System.out.println (a);
        }
        catch (Exception e) {
            e.printStackTrace ();
        }

        Task<ServerCreateObjectResponse> createPersonTask = new Task<>() {
            @Override
            public ServerCreateObjectResponse call () {
                updateMessage ("Create person ...");
                return sceneController.createObject (req);
            }
        };

        LoadingView root = new LoadingView (createPersonTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        createPersonTask.setOnSucceeded (e -> {
            stage.close ();

            ServerCreateObjectResponse res = createPersonTask.getValue ();
            System.out.println (res.getResponseStatus());
            System.out.println (res.getResponseMessage());
            System.out.println (res.getObjectId());
            if (res.getResponseStatus() == ResponseStatus.OK) {
                notificateUpdateSuccessfully ();
            }
            else {
                showFailedWindow (res);
            }
        });

        new Thread (createPersonTask).start ();
    }

    public void findPersonHandler () {
        FindPersonModel model = sceneController.getFindPersonModel ();

        String personId = model.getPersonIdProperty().get();
        String familyId = model.getFamilyIdProperty().get();
        String fullname = model.getFullnameProperty().get(); 
        boolean enableDateOfBirth = model.getEnableDateOfBirth().get();
        Date dateOfBirth = Date.convertFromLocalDate( model.getDateOfBirthProperty().get() );
        String citizenId = model.getCitizenIdProperty().get();
        String phoneNumber = model.getPhoneNumberProperty().get(); 
        String sexString = model.getSexProperty().get();
        String nationString = model.getNationProperty().get();
        String residenceStatusString  = model.getResidenceStatusProperty().get();

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
        if (enableDateOfBirth) {
            conditionBuilder.append (String.format (" AND (date_of_birth >= '%s 00:00:00' AND date_of_birth < '%s 00:00:00')",
                                                        dateOfBirth,
                                                        dateOfBirth.plusDays (1) ));
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

        Task<List<Map<String, Object>> > findPersonTask = new Task<>() {
            @Override
            public List<Map<String, Object>>  call () {
                updateMessage ("Executing find people query ...");
                AdminFindObject request = new AdminFindObject (sceneController.getUsername (),
                                                condition, new Person ());
                return sceneController.findObject (request).getRequestResult ();
            }
        };

        LoadingView root = new LoadingView (findPersonTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        findPersonTask.setOnSucceeded (e -> {
            stage.close();

            List<Map<String, Object>>  res = findPersonTask.getValue ();
            List<Person> data = new ArrayList<>();
            for (Map<String, Object> map : res) {
                data.add (Person.convertFromMap (map));
            }
            TableView<Person> tableView = model.getTableView ();
            tableView.setItems (FXCollections.observableList (data));
        });

        new Thread (findPersonTask).start ();
    }

    public void updatePersonHandler () {
        UpdatePersonModel model = sceneController.getUpdatePersonModel ();

        String personId = model.getPersonIdProperty().get();
        String familyId = model.getFamilyIdProperty().get();

        String fullname = model.getFullnameProperty().get(); 
        fullname = fullname.trim ();
        fullname = fullname.replaceAll ("^ +| +$|( )+", "$1");

        Date dateOfBirth = Date.convertFromLocalDate( model.getDateOfBirthProperty().get() );

        String citizenId = model.getCitizenIdProperty().get();
        citizenId = citizenId.replace (" ", "");
        citizenId = citizenId.replace ("-", "");

        String phoneNumber = model.getPhoneNumberProperty().get(); 
        phoneNumber = phoneNumber.replace (" ", "");

        String sexString = model.getSexProperty().get();
        String nationString = model.getNationProperty().get();
        String residenceStatusString  = model.getResidenceStatusProperty().get();

        if (familyId.isEmpty()) {
            System.out.println ("Chua nhap family id");
            return;
        }

        if (fullname.isEmpty()) {
            System.out.println ("Chua nhap ten");
            return;
        }

        if (citizenId.contains("_")) {
            System.out.println ("Ma so CCCD chua hop le");
            return;
        }

        if (phoneNumber.contains("_")) {
            System.out.println ("Sdt chua hop le");
            return;
        }

        Sex sex = null;
        if (sexString.equals("Nam"))
            sex = Sex.MALE;
        else if (sexString.equals("Nữ"))
            sex = Sex.FEMALE;
        else {
            System.out.println ("Chua chon gioi tinh");
            return;
        }

        if (Nation.matchByString(nationString).isEmpty()) {
            System.out.println ("Chua chon quoc tich");
            return;
        }
        Nation nation = Nation.matchByString (nationString).get();

        ResidenceStatus residenceStatus = null;
        if (residenceStatusString.equals("Đang cư trú"))
            residenceStatus = ResidenceStatus.PRESENT;
        else if (residenceStatusString.equals("Tạm vắng"))
            residenceStatus = ResidenceStatus.ABSENT;
        else {
            System.out.println ("Chua chon tinh trang cu tru");
            return;
        }

        GetSQLProperties object = new Person (Integer.parseInt(familyId), fullname,
                                        dateOfBirth, citizenId,
                                        phoneNumber, sex,
                                        nation, residenceStatus);
        object.setId(Integer.parseInt(personId));

        Task<ServerUpdateObjectResponse> updatePersonTask = new Task<>() {
            @Override
            public ServerUpdateObjectResponse call () {
                updateMessage ("Update person query ...");
                AdminUpdateObject request = new AdminUpdateObject (sceneController.getUsername(), object);
                return sceneController.updateObject (request);
            }
        };

        LoadingView root = new LoadingView (updatePersonTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        updatePersonTask.setOnSucceeded (e -> {
            stage.close();

            ServerUpdateObjectResponse res = updatePersonTask.getValue ();
            switch (res.getResponseStatus ()) {
                case OK:
                    TableView<Person> tableView = sceneController.getFindPersonModel().getTableView();
                    for(int i = 0; i < tableView.getItems().size(); ++i) {
                        if (tableView.getItems().get(i).getPersonId() == object.getId()) {
                            tableView.getItems().set(i, (Person) object);
                            break;
                        }
                    }

                    notificateUpdateSuccessfully ();
                    break;

                default:
                    showFailedWindow (res);
                    break;
            }
        });

        new Thread (updatePersonTask).start ();
    }

    public void closePersonUpdateHandler () {
        sceneController.getUpdatePersonModel()
                        .getUpdatePersonWindow()
                        .close();
    }

    public void updateAndClosePersonHandler () {
        updatePersonHandler ();
        closePersonUpdateHandler ();
    }

    public void createFamilyHandler () {
        CreateFamilyModel model = sceneController.getCreateFamilyModel ();

        String personId = model.personIdProperty().get();

        String houseNumber = model.houseNumberProperty().get();
        houseNumber = houseNumber.trim ();
        houseNumber = houseNumber.replaceAll ("^ +| +$|( )+", "$1");

        Family object = new Family (Integer.parseInt (personId),
                                    houseNumber);
        AdminCreateObject req = new AdminCreateObject (sceneController.getUsername(), object);

        ObjectMapper mapper = new ObjectMapper ();
        try {
            String a = mapper.writeValueAsString (req.getObject ());
            System.out.println (a);
        }
        catch (Exception e) {
            e.printStackTrace ();
        }

        Task<ServerCreateObjectResponse> createFamilyTask = new Task<>() {
            @Override
            public ServerCreateObjectResponse call () {
                updateMessage ("Create family query ...");
                return sceneController.createObject (req);
            }
        };

        LoadingView root = new LoadingView (createFamilyTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        createFamilyTask.setOnSucceeded (e -> {
            stage.close();

            ServerCreateObjectResponse res = createFamilyTask.getValue ();
            System.out.println (res.getResponseStatus());
            System.out.println (res.getResponseMessage());
            System.out.println (res.getObjectId());

            if (res.getResponseStatus() == ResponseStatus.OK) {
                notificateUpdateSuccessfully ();
            }
            else {
                showFailedWindow (res);
            }
        });

        new Thread (createFamilyTask).start ();
    }
 
    public void findFamilyHandler () {
        FindFamilyModel model = sceneController.getFindFamilyModel ();

        String familyId = model.familyIdProperty().get();
        String personId = model.personIdProperty().get();
        String houseNumber = model.houseNumberProperty().get();

        StringBuilder conditionBuilder = new StringBuilder ("(1=1)");
        if (!familyId.isEmpty()) {
            conditionBuilder.append (String.format (" AND (family_id='%s')", familyId));
        }
        if (!personId.isEmpty()) {
            conditionBuilder.append (String.format (" AND (person_id='%s')", personId));
        }
        if (!houseNumber.isEmpty()) {
            conditionBuilder.append (String.format (" AND (house_number LIKE '%%%s%%')", houseNumber));
        }
        String condition = conditionBuilder.toString ();

        Task<List<Map<String, Object>> > findFamilyTask = new Task<>() {
            @Override
            public List<Map<String, Object>>  call () {
                updateMessage ("Executing find family query ...");
                AdminFindObject request = new AdminFindObject (sceneController.getUsername (),
                                                condition, new Family ());
                return sceneController.findObject (request).getRequestResult ();
            }
        };

        LoadingView root = new LoadingView (findFamilyTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        findFamilyTask.setOnSucceeded (e -> {
            stage.close();

            List<Map<String, Object>> requestResult = findFamilyTask.getValue ();

            List<Family> data = new ArrayList<>();
            for (Map<String, Object> map : requestResult) {
                data.add (Family.convertFromMap (map));
            }

            TableView<Family> tableView = model.getTableView ();
            tableView.setItems (FXCollections.observableList (data));
        });

        new Thread (findFamilyTask).start ();
    }

    public void updateFamilyHandler () {
        UpdateFamilyModel model = sceneController.getUpdateFamilyModel ();

        String familyId = model.familyIdProperty().get();
        String personId = model.personIdProperty().get();

        String houseNumber = model.houseNumberProperty().get(); 
        houseNumber = houseNumber.trim ();
        houseNumber = houseNumber.replaceAll ("^ +| +$|( )+", "$1");

        if (personId.isEmpty()) {
            System.out.println ("Chua person id");
            return;
        }

        if (houseNumber.isEmpty()) {
            System.out.println ("Chua so nha");
            return;
        }

        GetSQLProperties object = new Family (Integer.parseInt(personId), houseNumber);
        object.setId(Integer.parseInt(familyId));

        Task<ServerUpdateObjectResponse> updateFamilyTask = new Task<>() {
            @Override
            public ServerUpdateObjectResponse call () {
                updateMessage ("Update family query ...");
                AdminUpdateObject request = new AdminUpdateObject (sceneController.getUsername(), object);
                return sceneController.updateObject (request);
            }
        };

        LoadingView root = new LoadingView (updateFamilyTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        updateFamilyTask.setOnSucceeded (e -> {
            stage.close();

            ServerUpdateObjectResponse res = updateFamilyTask.getValue ();
            switch (res.getResponseStatus ()) {
                case OK:
                    TableView<Family> tableView = sceneController.getFindFamilyModel().getTableView();
                    for(int i = 0; i < tableView.getItems().size(); ++i) {
                        if (tableView.getItems().get(i).getFamilyId() == object.getId()) {
                            tableView.getItems().set(i, (Family) object);
                            break;
                        }
                    }

                    notificateUpdateSuccessfully ();
                    break;

                default:
                    showFailedWindow (res);
                    break;
            }
        });

        new Thread (updateFamilyTask).start ();
    }

    public void closeFamilyUpdateHandler () {
        sceneController.getUpdateFamilyModel()
                        .getUpdateFamilyWindow()
                        .close();
    }

    public void updateAndCloseFamilyHandler () {
        updateFamilyHandler();
        closeFamilyUpdateHandler ();
    }

    public void createAccountHandler () {
        CreateAccountModel model = sceneController.getCreateAccountModel ();

        String familyId = model.familyIdProperty().get();
        String username = model.usernameProperty().get();
        String password = model.passwordProperty().get();
        String password2 = model.password2Property().get();

        String accountTypeString = model.accountTypeProperty().get();
        AccountType accountType = null; 
        if (!AccountType.matchByString(accountTypeString).isEmpty()) {
            accountType = AccountType.matchByString(accountTypeString).get();
        }

        String regex = "^[A-Za-z]\\w{5,29}$";
        Pattern p = Pattern.compile (regex);
        if (!p.matcher(username).matches()) {
            showConfirmationWindow ("Lỗi", "Tên tài khoản không hợp lệ", null);
            return;
        }

        if (!password.equals (password2)) {
            showConfirmationWindow ("Lỗi", "Không thể tạo tài khoản", "Mật khẩu không trùng lặp");
            return;
        }
        if (accountType == null) {
            showConfirmationWindow ("Lỗi", "Chưa chọn loại tài khoản", null);
            return;
        }

        String digestPassword = DigestUtils.sha256Hex (password);

        Account object = new Account (Integer.parseInt (familyId),
                                        username,
                                        digestPassword,
                                        accountType);
        AdminCreateObject req = new AdminCreateObject (sceneController.getUsername(), object);

        ObjectMapper mapper = new ObjectMapper ();
        try {
            String a = mapper.writeValueAsString (req.getObject ());
            System.out.println (a);
        }
        catch (Exception e) {
            e.printStackTrace ();
        }

        Task<ServerCreateObjectResponse> createAccountTask = new Task<>() {
            @Override
            public ServerCreateObjectResponse call () {
                updateMessage ("Create account ...");
                return sceneController.createObject (req);
            }
        };

        LoadingView root = new LoadingView (createAccountTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        createAccountTask.setOnSucceeded (e -> {
            stage.close ();

            ServerCreateObjectResponse res = createAccountTask.getValue ();
            System.out.println (res.getResponseStatus());
            System.out.println (res.getResponseMessage());
            System.out.println (res.getObjectId());
            if (res.getResponseStatus() == ResponseStatus.OK) {
                notificateUpdateSuccessfully ();
            }
            else {
                showFailedWindow (res);
            }
        });

        new Thread (createAccountTask).start ();
    }
 
    public void findAccountHandler () {
        FindAccountModel model = sceneController.getFindAccountModel ();

        String accountId = model.accountIdProperty().get();
        String familyId = model.familyIdProperty().get();

        String username = model.usernameProperty().get();
        username = username.trim();

        String accountTypeString = model.accountTypeProperty().get();
        AccountType accountType = null; 
        if (!AccountType.matchByString(accountTypeString).isEmpty()) {
            accountType = AccountType.matchByString(accountTypeString).get();
        }

        StringBuilder conditionBuilder = new StringBuilder ("(1=1)");
        if (!accountId.isEmpty()) {
            conditionBuilder.append (String.format (" AND (account_id='%s')", accountId));
        }
        if (!familyId.isEmpty()) {
            conditionBuilder.append (String.format (" AND (family_id='%s')", familyId));
        }
        if (!username.isEmpty()) {
            conditionBuilder.append (String.format (" AND (username LIKE '%%%s%%')", username));
        }
        if (accountType != null) {
            conditionBuilder.append (String.format (" AND (account_type LIKE '%%%s%%')", accountType));
        }
        String condition = conditionBuilder.toString ();

        System.out.printf("%s\n%s\n%s\n%s\n", accountId, familyId, username, condition);

        AdminFindObject request = new AdminFindObject (sceneController.getUsername (),
                                        condition, new Account ());

        Task<List<Map<String, Object>> > findAccountTask = new Task<>() {
            @Override
            public List<Map<String, Object>>  call () {
                updateMessage ("Executing find accounts query ...");
                return sceneController.findObject (request).getRequestResult ();
            }
        };

        LoadingView root = new LoadingView (findAccountTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        findAccountTask.setOnSucceeded (e -> {
            stage.close();

            List<Map<String, Object>>  res = findAccountTask.getValue ();
            List<Account> data = new ArrayList<>();
            for (Map<String, Object> map : res) {
                data.add (Account.convertFromMap (map));
            }
            TableView<Account> tableView = model.getTableView ();
            tableView.setItems (FXCollections.observableList (data));
        });

        new Thread (findAccountTask).start ();
    }

    public void updateAccountHandler () {
        UpdateAccountModel model = sceneController.getUpdateAccountModel ();

        String accountId = model.accountIdProperty().get();
        String familyId = model.familyIdProperty().get();
        String username = model.usernameProperty().get();
        String password = model.passwordProperty().get();
        String password2 = model.password2Property().get();

        String accountTypeString = model.accountTypeProperty().get();
        AccountType accountType = null; 
        if (!AccountType.matchByString(accountTypeString).isEmpty()) {
            accountType = AccountType.matchByString(accountTypeString).get();
        }

        String regex = "^[A-Za-z]\\w{5,29}$";
        Pattern p = Pattern.compile (regex);
        if (!p.matcher(username).matches()) {
            showConfirmationWindow ("Lỗi", "Tên tài khoản không hợp lệ", null);
            return;
        }

        if (!password.equals (password2)) {
            showConfirmationWindow ("Lỗi", "Không thể tạo tài khoản", "Mật khẩu không trùng lặp");
            return;
        }

        if (accountType == null) {
            showConfirmationWindow ("Lỗi", "Chưa chọn loại tài khoản", null);
            return;
        }

        String digestPassword = password.isEmpty() ? model.getDigestPassword() : DigestUtils.sha256Hex (password);

        GetSQLProperties object = new Account (Integer.parseInt(familyId), username, digestPassword, accountType);
        object.setId(Integer.parseInt(accountId));

        Task<ServerUpdateObjectResponse> updateAccountTask = new Task<>() {
            @Override
            public ServerUpdateObjectResponse call () {
                updateMessage ("Update account query ...");
                AdminUpdateObject request = new AdminUpdateObject (sceneController.getUsername(), object);
                return sceneController.updateObject (request);
            }
        };

        LoadingView root = new LoadingView (updateAccountTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        updateAccountTask.setOnSucceeded (e -> {
            stage.close();

            ServerUpdateObjectResponse res = updateAccountTask.getValue ();
            switch (res.getResponseStatus ()) {
                case OK:
                    TableView<Account> tableView = sceneController.getFindAccountModel().getTableView();
                    for(int i = 0; i < tableView.getItems().size(); ++i) {
                        if (tableView.getItems().get(i).getAccountId() == object.getId()) {
                            tableView.getItems().set(i, (Account) object);
                            break;
                        }
                    }

                    notificateUpdateSuccessfully ();
                    break;

                default:
                    showFailedWindow (res);
                    break;
            }
        });

        new Thread (updateAccountTask).start ();
    }

    public void closeAccountUpdateHandler () {
        sceneController.getUpdateAccountModel()
                        .getUpdateAccountWindow()
                        .close();
    }

    public void updateAndCloseAccountHandler () {
        updateAccountHandler();
        closeAccountUpdateHandler ();
    }

    public void createExpenseHandler () {
        CreateExpenseModel model = sceneController.getCreateExpenseModel ();

        String expenseTitle = model.expenseTitleProperty().get();
        String expenseDescription = model.expenseDescriptionProperty().get();
        LocalDate publishedDate = model.publishedDateProperty().get();
        String expenseTypeString = model.expenseTypeProperty().get();
        String totalCostString = model.totalCostProperty().get();
        boolean required = model.requiredProperty().get();

        if (expenseTitle.isEmpty ()) {
            showConfirmationWindow ("Lỗi", "Chưa có tiêu đề", null);
            return;
        }
        if (totalCostString.isEmpty ()) {
            showConfirmationWindow ("Lỗi", "Chưa có tổng chi phí", null);
            return;
        }
        if (ExpenseType.matchByString (expenseTypeString).isEmpty()) {
            showConfirmationWindow ("Lỗi", "Chưa chọn loại chi phí", null);
            return;
        }

        ExpenseType expenseType = ExpenseType.matchByString (expenseTypeString).get();

        Expense object = new Expense (expenseTitle,
                                        expenseDescription,
                                        Date.convertFromLocalDate(publishedDate),
                                        Integer.parseInt (totalCostString),
                                        expenseType,
                                        required);
        AdminCreateObject req = new AdminCreateObject (sceneController.getUsername(), object);

        ObjectMapper mapper = new ObjectMapper ();
        try {
            String a = mapper.writeValueAsString (req.getObject ());
            System.out.println (a);
        }
        catch (Exception e) {
            e.printStackTrace ();
        }

        Task<ServerCreateObjectResponse> createExpenseTask = new Task<>() {
            @Override
            public ServerCreateObjectResponse call () {
                updateMessage ("Create account ...");
                return sceneController.createObject (req);
            }
        };

        LoadingView root = new LoadingView (createExpenseTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        createExpenseTask.setOnSucceeded (e -> {
            stage.close ();

            ServerCreateObjectResponse res = createExpenseTask.getValue ();
            System.out.println (res.getResponseStatus());
            System.out.println (res.getResponseMessage());
            System.out.println (res.getObjectId());
            if (res.getResponseStatus() == ResponseStatus.OK) {
                notificateUpdateSuccessfully ();
            }
            else {
                showFailedWindow (res);
            }
        });

        new Thread (createExpenseTask).start ();
    }

    public void findExpenseHandler () {
        FindExpenseModel model = sceneController.getFindExpenseModel ();

        boolean enableDayFilter = model.enableDayFilterProperty().get();
        LocalDate beginPublishedDate = model.beginPublishedDateProperty().get();
        LocalDate endPublishedDate = model.endPublishedDateProperty().get();
        String minTotalCostString = model.minTotalCostProperty().get();
        String maxTotalCostString = model.maxTotalCostProperty().get();
        String expenseTypeString = model.expenseTypeProperty().get();
        boolean required = model.requiredProperty().get();

        ExpenseType expenseType = null;
        if (!ExpenseType.matchByString (expenseTypeString).isEmpty()) {
           expenseType = ExpenseType.matchByString (expenseTypeString).get();
        }


        StringBuilder conditionBuilder = new StringBuilder ("(1=1)");
        if (enableDayFilter) {
            conditionBuilder.append (String.format (" AND (published_date>='%s 00:00:00') AND (published_date<'%s 00:00:00')",
                                                    Date.convertFromLocalDate (beginPublishedDate),
                                                    Date.convertFromLocalDate (endPublishedDate).plusDays (1)) );
        }
        if (!minTotalCostString.isEmpty()) {
            conditionBuilder.append (String.format (" AND (total_cost>='%s')", Integer.parseInt (minTotalCostString)));
        }
        if (!maxTotalCostString.isEmpty()) {
            conditionBuilder.append (String.format (" AND (total_cost<='%s')", Integer.parseInt (maxTotalCostString)));
        }
        if (expenseType != null) {
            conditionBuilder.append (String.format (" AND (expense_type LIKE '%%%s%%')", expenseType));
        }

        conditionBuilder.append (String.format ("AND (required=%d)", required ? 1 : 0));

        String condition = conditionBuilder.toString ();

        AdminFindObject request = new AdminFindObject (sceneController.getUsername (),
                                        condition, new Expense ());

        Task<List<Map<String, Object>> > findExpenseTask = new Task<>() {
            @Override
            public List<Map<String, Object>>  call () {
                updateMessage ("Executing find expenses query ...");
                return sceneController.findObject (request).getRequestResult ();
            }
        };

        LoadingView root = new LoadingView (findExpenseTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        findExpenseTask.setOnSucceeded (e -> {
            stage.close();

            List<Map<String, Object>>  res = findExpenseTask.getValue ();
            List<Expense> data = new ArrayList<>();
            for (Map<String, Object> map : res) {
                data.add (Expense.convertFromMap (map));
            }
            TableView<Expense> tableView = model.getTableView ();
            tableView.setItems (FXCollections.observableList (data));
        });

        new Thread (findExpenseTask).start ();
    }

    public void updateExpenseHandler () {
        UpdateExpenseModel model = sceneController.getUpdateExpenseModel ();

        String expenseIdString = model.expenseIdProperty().get();
        String expenseTitle = model.expenseTitleProperty().get();
        String expenseDescription = model.expenseDescriptionProperty().get();
        LocalDate publishedDate = model.publishedDateProperty().get();
        String expenseTypeString = model.expenseTypeProperty().get();
        String totalCostString = model.totalCostProperty().get();
        boolean required = model.requiredProperty().get();

        if (expenseTitle.isEmpty ()) {
            showConfirmationWindow ("Lỗi", "Chưa có tiêu đề", null);
            return;
        }
        if (totalCostString.isEmpty ()) {
            showConfirmationWindow ("Lỗi", "Chưa có tổng chi phí", null);
            return;
        }
        if (ExpenseType.matchByString (expenseTypeString).isEmpty()) {
            showConfirmationWindow ("Lỗi", "Chưa chọn loại chi phí", null);
            return;
        }

        ExpenseType expenseType = ExpenseType.matchByString (expenseTypeString).get();

        GetSQLProperties object = new Expense (expenseTitle, expenseDescription,
                                                Date.convertFromLocalDate (publishedDate), Integer.parseInt(totalCostString),
                                                expenseType, required);
        object.setId(Integer.parseInt (expenseIdString));

        Task<ServerUpdateObjectResponse> updateExpenseTask = new Task<>() {
            @Override
            public ServerUpdateObjectResponse call () {
                updateMessage ("Update expense query ...");
                AdminUpdateObject request = new AdminUpdateObject (sceneController.getUsername(), object);
                return sceneController.updateObject (request);
            }
        };

        LoadingView root = new LoadingView (updateExpenseTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        updateExpenseTask.setOnSucceeded (e -> {
            stage.close();

            ServerUpdateObjectResponse res = updateExpenseTask.getValue ();
            switch (res.getResponseStatus ()) {
                case OK:
                    TableView<Expense> tableView = sceneController.getFindExpenseModel().getTableView();
                    for(int i = 0; i < tableView.getItems().size(); ++i) {
                        if (tableView.getItems().get(i).getExpenseId() == object.getId()) {
                            tableView.getItems().set(i, (Expense) object);
                            break;
                        }
                    }

                    notificateUpdateSuccessfully ();
                    break;

                default:
                    showFailedWindow (res);
                    break;
            }
        });

        new Thread (updateExpenseTask).start ();
    }

    public void closeExpenseUpdateHandler () {
        sceneController.getUpdateExpenseModel()
                        .getUpdateExpenseWindow()
                        .close();
    }

    public void updateAndCloseExpenseHandler () {
        updateExpenseHandler();
        closeExpenseUpdateHandler ();
    }

    public void createPaymentStatusHandler () {
        CreatePaymentStatusModel model = sceneController.getCreatePaymentStatusModel ();

        String expenseIdString = model.expenseIdProperty().get();
        String familyIdString = model.familyIdProperty().get();
        String totalPayString = model.totalPayProperty().get();
        LocalDate publishedDate = model.publishedDateProperty().get();

        PaymentStatus object = new PaymentStatus (Integer.parseInt(expenseIdString),
                                                    Integer.parseInt(familyIdString),
                                                    Integer.parseInt(totalPayString),
                                                    Date.convertFromLocalDate(publishedDate));
        AdminCreateObject req = new AdminCreateObject (sceneController.getUsername(), object);

        System.out.println (req.getObject().getSQLInsertCommand());

        ObjectMapper mapper = new ObjectMapper ();
        try {
            String a = mapper.writeValueAsString (req.getObject ());
            System.out.println (a);
        }
        catch (Exception e) {
            e.printStackTrace ();
        }

        Task<ServerCreateObjectResponse> createPaymentStatusTask = new Task<>() {
            @Override
            public ServerCreateObjectResponse call () {
                updateMessage ("Create account ...");
                return sceneController.createObject (req);
            }
        };

        LoadingView root = new LoadingView (createPaymentStatusTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        createPaymentStatusTask.setOnSucceeded (e -> {
            stage.close ();

            ServerCreateObjectResponse res = createPaymentStatusTask.getValue ();
            System.out.println (res.getResponseStatus());
            System.out.println (res.getResponseMessage());
            System.out.println (res.getObjectId());
            if (res.getResponseStatus() == ResponseStatus.OK) {
                notificateUpdateSuccessfully ();
            }
            else {
                showFailedWindow (res);
            }
        });

        new Thread (createPaymentStatusTask).start ();
    }

    public void findPaymentStatusHandler () {
        FindPaymentStatusModel model = sceneController.getFindPaymentStatusModel ();

        String paymentStatusIdString = model.paymentStatusIdProperty().get();
        String expenseIdString = model.expenseIdProperty().get();
        String familyIdString = model.familyIdProperty().get();
        // String totalPayString = model.totalPayProperty().get();
        boolean enableDayFilter = model.enableDateFilterProperty().get();
        LocalDate beginPublishedDate = model.beginPublishedDateProperty().get();
        LocalDate endPublishedDate = model.endPublishedDateProperty().get();

        StringBuilder conditionBuilder = new StringBuilder ("(1=1)");

        if (!paymentStatusIdString.isEmpty()) {
            conditionBuilder.append (String.format (" AND (payment_status_id='%s')", paymentStatusIdString));
        }
        if (!expenseIdString.isEmpty()) {
            conditionBuilder.append (String.format (" AND (expense_id='%s')", expenseIdString));
        }
        if (!familyIdString.isEmpty()) {
            conditionBuilder.append (String.format (" AND (family_id='%s')", familyIdString));
        }
        if (enableDayFilter) {
            conditionBuilder.append (String.format (" AND (published_date>='%s 00:00:00') AND (published_date<'%s 00:00:00')",
                                                    Date.convertFromLocalDate (beginPublishedDate),
                                                    Date.convertFromLocalDate (endPublishedDate).plusDays (1)) );
        }

        String condition = conditionBuilder.toString ();

        AdminFindObject request = new AdminFindObject (sceneController.getUsername (),
                                        condition, new PaymentStatus ());

        Task<List<Map<String, Object>> > findPaymentStatusTask = new Task<>() {
            @Override
            public List<Map<String, Object>>  call () {
                updateMessage ("Executing find payment statuses query ...");
                return sceneController.findObject (request).getRequestResult ();
            }
        };

        LoadingView root = new LoadingView (findPaymentStatusTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        findPaymentStatusTask.setOnSucceeded (e -> {
            stage.close();

            List<Map<String, Object>>  res = findPaymentStatusTask.getValue ();
            List<PaymentStatus> data = new ArrayList<>();
            for (Map<String, Object> map : res) {
                data.add (PaymentStatus.convertFromMap (map));
            }
            TableView<PaymentStatus> tableView = model.getTableView ();
            tableView.setItems (FXCollections.observableList (data));
        });

        new Thread (findPaymentStatusTask).start ();
    }

    public void updatePaymentStatusHandler () {
        UpdatePaymentStatusModel model = sceneController.getUpdatePaymentStatusModel ();

        String paymentStatusIdString = model.paymentStatusIdProperty().get();
        String expenseIdString = model.expenseIdProperty().get();
        String familyIdString = model.familyIdProperty().get();
        String totalPayString = model.totalPayProperty().get();
        LocalDate publishedDate = model.publishedDateProperty().get();

        GetSQLProperties object = new PaymentStatus (   Integer.parseInt (expenseIdString), 
                                                        Integer.parseInt (familyIdString),
                                                        Integer.parseInt (totalPayString),
                                                        Date.convertFromLocalDate(publishedDate) );
        object.setId(Integer.parseInt (paymentStatusIdString));

        Task<ServerUpdateObjectResponse> updatePaymentStatusTask = new Task<>() {
            @Override
            public ServerUpdateObjectResponse call () {
                updateMessage ("Update payment status query ...");
                AdminUpdateObject request = new AdminUpdateObject (sceneController.getUsername(), object);
                return sceneController.updateObject (request);
            }
        };

        LoadingView root = new LoadingView (updatePaymentStatusTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        updatePaymentStatusTask.setOnSucceeded (e -> {
            stage.close();

            ServerUpdateObjectResponse res = updatePaymentStatusTask.getValue ();
            switch (res.getResponseStatus ()) {
                case OK:
                    TableView<PaymentStatus> tableView = sceneController.getFindPaymentStatusModel().getTableView();
                    for(int i = 0; i < tableView.getItems().size(); ++i) {
                        if (tableView.getItems().get(i).getPaymentStatusId() == object.getId()) {
                            tableView.getItems().set(i, (PaymentStatus) object);
                            break;
                        }
                    }

                    notificateUpdateSuccessfully ();
                    break;

                default:
                    showFailedWindow (res);
                    break;
            }
        });

        new Thread (updatePaymentStatusTask).start ();
    }

    public void closePaymentStatusUpdateHandler () {
        sceneController.getUpdatePaymentStatusModel()
                        .getUpdatePaymentStatusWindow()
                        .close();
    }

    public void updateAndClosePaymentStatusHandler () {
        updatePaymentStatusHandler();
        closePaymentStatusUpdateHandler ();
    }

    public void findVehicleHandler () {
        FindVehicleModel model = sceneController.getFindVehicleModel();

        String familyIdString = model.getFamilyId().get();
        String vehicleTypeString = model.getVehicleType().get();
        VehicleType vehicleType = null;
        if (vehicleTypeString.equals ("Xe hơi")) {
            vehicleType = VehicleType.CAR;
        }
        else if (vehicleTypeString.equals ("Xe đạp")) {
            vehicleType = VehicleType.BICYCLE;
        }
        else if (vehicleTypeString.equals ("Xe máy")) {
            vehicleType = VehicleType.MOTORCYCLE;
        }
        else if (vehicleTypeString.equals ("Xe đạp điện")) {
            vehicleType = VehicleType.ELECTRIC_BICYCLE;
        }

        String plateId = model.getPlateId().get();
        plateId = plateId.trim ();
        plateId = plateId.replaceAll ("^ +| +$|( )+", "$1");

        StringBuilder conditionBuilder = new StringBuilder ("(1=1)");
        if (!familyIdString.isEmpty()) {
            conditionBuilder.append (String.format (" AND (family_id='%s')", familyIdString));
        }
        if (vehicleType != null) {
            conditionBuilder.append (String.format (" AND (vehicle_type='%s')", vehicleType));
        }
        if (!plateId.isEmpty()) {
            conditionBuilder.append (String.format (" AND (plate_id='%s')", plateId));
        }

        String condition = conditionBuilder.toString ();

        AdminFindObject request = new AdminFindObject (sceneController.getUsername (),
                                        condition, new Vehicle());

        Task<List<Map<String, Object>> > findVehicleTask = new Task<>() {
            @Override
            public List<Map<String, Object>>  call () {
                updateMessage ("Executing find vehicle query ...");
                return sceneController.findObject (request).getRequestResult ();
            }
        };

        LoadingView root = new LoadingView (findVehicleTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        findVehicleTask.setOnSucceeded (e -> {
            stage.close();

            List<Map<String, Object>>  res = findVehicleTask.getValue ();
            List<Vehicle> data = new ArrayList<>();
            for (Map<String, Object> map : res) {
                data.add (Vehicle.convertFromMap (map));
            }
            TableView<Vehicle> tableView = model.getTableView ();
            tableView.setItems (FXCollections.observableList (data));
        });

        new Thread (findVehicleTask).start ();
    }

    public void createVehicleHandler () {
        CreateVehicleModel model = sceneController.getCreateVehicleModel ();

        String familyIdString = model.getFamilyId().get();
        String vehicleTypeString = model.getVehicleType().get();
        VehicleType vehicleType;
        if (vehicleTypeString.equals ("Xe hơi")) {
            vehicleType = VehicleType.CAR;
        }
        else if (vehicleTypeString.equals ("Xe đạp")) {
            vehicleType = VehicleType.BICYCLE;
        }
        else if (vehicleTypeString.equals ("Xe máy")) {
            vehicleType = VehicleType.MOTORCYCLE;
        }
        else {
            vehicleType = VehicleType.ELECTRIC_BICYCLE;
        }
        String plateId = model.getPlateId().get();
        plateId = plateId.trim ();
        plateId = plateId.replaceAll ("^ +| +$|( )+", "$1");

        if (familyIdString.isEmpty ()) {
            notificate ("Thất bại", "Chưa điền family id");
            return ;
        }
        if (plateId.isEmpty ()) {
            notificate ("Thất bại", "Chưa điền plate id");
            return ;
        }

        Vehicle vehicle = new Vehicle(vehicleType, Integer.parseInt(familyIdString), plateId);

        AdminCreateObject req = new AdminCreateObject (sceneController.getUsername(), vehicle);

        ObjectMapper mapper = new ObjectMapper ();
        try {
            String a = mapper.writeValueAsString (req.getObject ());
            System.out.println (a);
        }
        catch (Exception e) {
            e.printStackTrace ();
        }

        Task<ServerCreateObjectResponse> createPersonTask = new Task<>() {
            @Override
            public ServerCreateObjectResponse call () {
                updateMessage ("Create vehicle...");
                return sceneController.createObject (req);
            }
        };

        LoadingView root = new LoadingView (createPersonTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        createPersonTask.setOnSucceeded (e -> {
            stage.close ();

            ServerCreateObjectResponse res = createPersonTask.getValue ();
            System.out.println (res.getResponseStatus());
            System.out.println (res.getResponseMessage());
            System.out.println (res.getObjectId());
            if (res.getResponseStatus() == ResponseStatus.OK) {
                notificateUpdateSuccessfully ();
            }
            else {
                showFailedWindow (res);
            }
        });

        new Thread (createPersonTask).start ();
    }

    public void updateVehicleHandler () {
        UpdateVehicleModel model = sceneController.getUpdateVehicleModel ();

        String vehicleId = model.getVehicleId().get ();
        String familyIdString = model.getFamilyId().get();
        String vehicleTypeString = model.getVehicleType().get();
        VehicleType vehicleType;
        if (vehicleTypeString.equals ("Xe hơi")) {
            vehicleType = VehicleType.CAR;
        }
        else if (vehicleTypeString.equals ("Xe đạp")) {
            vehicleType = VehicleType.BICYCLE;
        }
        else if (vehicleTypeString.equals ("Xe máy")) {
            vehicleType = VehicleType.MOTORCYCLE;
        }
        else {
            vehicleType = VehicleType.ELECTRIC_BICYCLE;
        }
        String plateId = model.getPlateId().get();
        plateId = plateId.trim ();
        plateId = plateId.replaceAll ("^ +| +$|( )+", "$1");

        if (familyIdString.isEmpty ()) {
            notificate ("Thất bại", "Chưa điền family id");
            return ;
        }
        if (plateId.isEmpty ()) {
            notificate ("Thất bại", "Chưa điền plate id");
            return ;
        }

        Vehicle object = new Vehicle(vehicleType, Integer.valueOf(familyIdString), plateId);
        object.setId(Integer.parseInt(vehicleId));

        Task<ServerUpdateObjectResponse> updatePersonTask = new Task<>() {
            @Override
            public ServerUpdateObjectResponse call () {
                updateMessage ("Update vehicle query ...");
                AdminUpdateObject request = new AdminUpdateObject (sceneController.getUsername(), object);
                return sceneController.updateObject (request);
            }
        };

        LoadingView root = new LoadingView (updatePersonTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        updatePersonTask.setOnSucceeded (e -> {
            stage.close();

            ServerUpdateObjectResponse res = updatePersonTask.getValue ();
            switch (res.getResponseStatus ()) {
                case OK:
                    TableView<Vehicle> tableView = sceneController.getFindVehicleModel().getTableView();
                    for(int i = 0; i < tableView.getItems().size(); ++i) {
                        if (tableView.getItems().get(i).getVehicleId() == object.getId()) {
                            tableView.getItems().set(i, (Vehicle) object);
                            break;
                        }
                    }

                    notificateUpdateSuccessfully ();
                    break;

                default:
                    showFailedWindow (res);
                    break;
            }
        });

        new Thread (updatePersonTask).start ();
    }

    public void closeVehicleUpdateHandler () {
        sceneController.getUpdateVehicleModel()
                        .getUpdateVehicleWindow()
                        .close();
    }

    public void updateAndCloseVehicleHandler () {
        updateVehicleHandler ();
        closeVehicleUpdateHandler ();
    }

}
