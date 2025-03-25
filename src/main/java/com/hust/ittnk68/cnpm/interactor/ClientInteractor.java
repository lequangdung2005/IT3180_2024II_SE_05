package com.hust.ittnk68.cnpm.interactor;

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
import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.ClientModel;
import com.hust.ittnk68.cnpm.model.CreateAccountModel;
import com.hust.ittnk68.cnpm.model.CreateFamilyModel;
import com.hust.ittnk68.cnpm.model.CreatePersonModel;
import com.hust.ittnk68.cnpm.model.Family;
import com.hust.ittnk68.cnpm.model.FindAccountModel;
import com.hust.ittnk68.cnpm.model.FindFamilyModel;
import com.hust.ittnk68.cnpm.model.FindPersonModel;
import com.hust.ittnk68.cnpm.model.Person;
import com.hust.ittnk68.cnpm.model.UpdateAccountModel;
import com.hust.ittnk68.cnpm.model.UpdateFamilyModel;
import com.hust.ittnk68.cnpm.model.UpdatePersonModel;
import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.Date;
import com.hust.ittnk68.cnpm.type.Nation;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.ResponseStatus;
import com.hust.ittnk68.cnpm.type.Sex;

import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

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
            String digestPassword = DigestUtils.sha256Hex(password);

            ClientMessageStartSession message = new ClientMessageStartSession (username, digestPassword);
            ServerResponseStartSession res = sceneController.startSession (message);

            // xu ly exception
            // khong ket noi dc => thong bao

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
        }
    }

    public void forgetPasswordButtonHandler() {
        System.out.println("forget");
    }

    public void logout () {
        sceneController.endSession ();
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

    public Optional<ButtonType> showComfirmationWindow (String title, String headerText, String contentText) {
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
        fail.setTitle ("Thất bại");
        fail.setHeaderText (res.getResponseStatus().toString());
        fail.setContentText (res.getResponseMessage());
        fail.initOwner (sceneController.getScene().getWindow());
        return fail.showAndWait ();
    }

    private void notificateUpdateSuccessfully () {
        Alert success = new Alert (AlertType.INFORMATION);
        success.setTitle ("Cập nhật thành công");
        success.setHeaderText ("Cập nhật thành công");
        success.initOwner (sceneController.getScene().getWindow());
        success.showAndWait ();
    }

    public void createPersonHandler () {
        CreatePersonModel model = sceneController.getCreatePersonModel ();

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
        String residenceStatusString = model.getResidenceStatusProperty().get();

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


        Person object = new Person (-1, fullname,
                                        dateOfBirth, citizenId,
                                        phoneNumber, sex,
                                        nation, residenceStatus);
        AdminCreateObject req = new AdminCreateObject (sceneController.getToken(), object);

        ObjectMapper mapper = new ObjectMapper ();
        try {
            String a = mapper.writeValueAsString (req.getObject ());
            System.out.println (a);
        }
        catch (Exception e) {
            e.printStackTrace ();
        }

        ServerCreateObjectResponse res = sceneController.createObject (req);

        System.out.println (res.getResponseStatus());
        System.out.println (res.getResponseMessage());
        System.out.println (res.getObjectId());

        if (res.getResponseStatus() == ResponseStatus.OK)
            notificateUpdateSuccessfully ();
        else
            ;
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

        AdminFindObject request = new AdminFindObject (sceneController.getToken (),
                                        condition, new Person ());

        List<Map<String, Object>> requestResult = sceneController.findObject (request). getRequestResult ();

        List<Person> data = new ArrayList<>();
        for (Map<String, Object> map : requestResult) {
            data.add (Person.convertFromMap (map));
        }

        TableView<Person> tableView = model.getTableView ();
        tableView.setItems (FXCollections.observableList (data));
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

        AdminUpdateObject request = new AdminUpdateObject (sceneController.getToken(), object);
        ServerUpdateObjectResponse res = sceneController.updateObject (request);

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
        AdminCreateObject req = new AdminCreateObject (sceneController.getToken(), object);

        ObjectMapper mapper = new ObjectMapper ();
        try {
            String a = mapper.writeValueAsString (req.getObject ());
            System.out.println (a);
        }
        catch (Exception e) {
            e.printStackTrace ();
        }

        ServerCreateObjectResponse res = sceneController.createObject (req);
        System.out.println (res.getResponseStatus());
        System.out.println (res.getResponseMessage());
        System.out.println (res.getObjectId());
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

        AdminFindObject request = new AdminFindObject (sceneController.getToken (),
                                        condition, new Family ());

        List<Map<String, Object>> requestResult = sceneController.findObject (request). getRequestResult ();

        List<Family> data = new ArrayList<>();
        for (Map<String, Object> map : requestResult) {
            data.add (Family.convertFromMap (map));
        }

        TableView<Family> tableView = model.getTableView ();
        tableView.setItems (FXCollections.observableList (data));
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

        AdminUpdateObject request = new AdminUpdateObject (sceneController.getToken(), object);
        ServerUpdateObjectResponse res = sceneController.updateObject (request);

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
            showComfirmationWindow ("Lỗi", "Tên tài khoản không hợp lệ", null);
            return;
        }

        if (!password.equals (password2)) {
            showComfirmationWindow ("Lỗi", "Không thể tạo tài khoản", "Mật khẩu không trùng lặp");
            return;
        }
        if (accountType == null) {
            showComfirmationWindow ("Lỗi", "Chưa chọn loại tài khoản", null);
            return;
        }

        String digestPassword = DigestUtils.sha256Hex (password);

        Account object = new Account (Integer.parseInt (familyId),
                                        username,
                                        digestPassword,
                                        accountType);
        AdminCreateObject req = new AdminCreateObject (sceneController.getToken(), object);

        ObjectMapper mapper = new ObjectMapper ();
        try {
            String a = mapper.writeValueAsString (req.getObject ());
            System.out.println (a);
        }
        catch (Exception e) {
            e.printStackTrace ();
        }

        ServerCreateObjectResponse res = sceneController.createObject (req);
        System.out.println (res.getResponseStatus());
        System.out.println (res.getResponseMessage());
        System.out.println (res.getObjectId());
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

        AdminFindObject request = new AdminFindObject (sceneController.getToken (),
                                        condition, new Account ());

        List<Map<String, Object>> requestResult = sceneController.findObject (request). getRequestResult ();

        List<Account> data = new ArrayList<>();
        for (Map<String, Object> map : requestResult) {
            data.add (Account.convertFromMap (map));
        }

        TableView<Account> tableView = model.getTableView ();
        tableView.setItems (FXCollections.observableList (data));
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
            showComfirmationWindow ("Lỗi", "Tên tài khoản không hợp lệ", null);
            return;
        }

        if (!password.equals (password2)) {
            showComfirmationWindow ("Lỗi", "Không thể tạo tài khoản", "Mật khẩu không trùng lặp");
            return;
        }

        if (accountType == null) {
            showComfirmationWindow ("Lỗi", "Chưa chọn loại tài khoản", null);
            return;
        }

        String digestPassword = password.isEmpty() ? model.getDigestPassword() : DigestUtils.sha256Hex (password);

        GetSQLProperties object = new Account (Integer.parseInt(familyId), username, digestPassword, accountType);
        object.setId(Integer.parseInt(accountId));

        AdminUpdateObject request = new AdminUpdateObject (sceneController.getToken(), object);
        ServerUpdateObjectResponse res = sceneController.updateObject (request);

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

}

