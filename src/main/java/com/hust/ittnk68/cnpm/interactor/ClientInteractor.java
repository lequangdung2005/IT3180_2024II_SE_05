package com.hust.ittnk68.cnpm.interactor;

import org.apache.commons.codec.digest.DigestUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hust.ittnk68.cnpm.communication.AdminCreateObject;
import com.hust.ittnk68.cnpm.communication.ClientMessageStartSession;
import com.hust.ittnk68.cnpm.communication.ServerCreateObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerResponseStartSession;
import com.hust.ittnk68.cnpm.controller.AdminHomeScreenController;
import com.hust.ittnk68.cnpm.controller.ClientHomeScreenController;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.controller.LoginController;
import com.hust.ittnk68.cnpm.database.GetSQLProperties;
import com.hust.ittnk68.cnpm.model.ClientModel;
import com.hust.ittnk68.cnpm.model.CreatePersonModel;
import com.hust.ittnk68.cnpm.model.Person;
import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.Date;
import com.hust.ittnk68.cnpm.type.Nation;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.ResponseStatus;
import com.hust.ittnk68.cnpm.type.Sex;

import javafx.scene.Parent;
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
    }
}

