package com.hust.ittnk68.cnpm.controller;

import com.hust.ittnk68.cnpm.model.ClientModel;
import com.hust.ittnk68.cnpm.model.CreatePersonModel;
import com.hust.ittnk68.cnpm.model.FindPersonModel;
import com.hust.ittnk68.cnpm.model.UpdatePersonModel;
import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;

import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.web.client.RestClient;

import com.hust.ittnk68.cnpm.communication.AdminCreateObject;
import com.hust.ittnk68.cnpm.communication.AdminDeleteObject;
import com.hust.ittnk68.cnpm.communication.AdminFindObject;
import com.hust.ittnk68.cnpm.communication.AdminUpdateObject;
import com.hust.ittnk68.cnpm.communication.ApiMapping;
import com.hust.ittnk68.cnpm.communication.ClientMessageStartSession;
import com.hust.ittnk68.cnpm.communication.ServerCreateObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerDeleteObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerFindObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerResponseStartSession;
import com.hust.ittnk68.cnpm.communication.ServerUpdateObjectResponse;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.interactor.ClientInteractor;

import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ClientSceneController {
    private Stage stage;
    private Scene scene;
    private StackPane stackPane;

    private UpdatePersonModel updatePersonModel;
    private FindPersonModel findPersonModel;
    private CreatePersonModel createPersonModel;

    private ClientModel clientModel;

    private ClientInteractor clientInteractor;

    public ClientSceneController() {
        stackPane = new StackPane ();
        clientModel = new ClientModel ();
        clientInteractor = new ClientInteractor (this);
        createPersonModel = new CreatePersonModel ();
        findPersonModel = new FindPersonModel ();
        updatePersonModel = new UpdatePersonModel ();
    }

    public void start(Stage stage, String title, double width, double height) {

        // window shape
        Rectangle rect = new Rectangle (width, height);
        rect.setArcHeight (60.0);
        rect.setArcWidth (60.0);

        this.stage = stage;
        stage.setTitle(title);
        stage.initStyle (StageStyle.TRANSPARENT);

        scene = new Scene(this.stackPane, width, height);
        scene.setFill (Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/css/client.css").toExternalForm());
        stage.setScene(scene);

        this.stackPane.setClip (rect);
        this.stackPane.getChildren ().add (new LoginController (this).getView ());

	stage.show(); 
    }

    public ServerResponseStartSession startSession (ClientMessageStartSession message)
    {
        // loading screen ...

        ServerResponseStartSession res;
        try {
            RestClient restClient = clientModel.getRestClient ();
            res = restClient.post()
                    .uri (getUriBase() + ApiMapping.START_SESSION)
                    .body (message)
                    .retrieve ()
                    .body (ServerResponseStartSession.class);
        }
        catch (Exception e)
        {
            return new ServerResponseStartSession (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server", "", AccountType.UNVALID);
        }
        // stop loading screen ...
        return res;
    }

    public void endSession ()
    {
        try {
            RestClient restClient = clientModel.getRestClient ();
            restClient.post()
                    .uri (getUriBase() + ApiMapping.END_SESSION)
                    .body (getToken ())
                    .retrieve ()
                    .body (String.class);
        }
        catch (Exception e)
        {
            System.out.println ("end session: can't connect to server");
        }
    }

    public ServerCreateObjectResponse createObject (AdminCreateObject req) {


        try {
            RestClient restClient = clientModel.getRestClient ();
            ServerCreateObjectResponse res;
            res = restClient.post()
                        .uri (getUriBase() + ApiMapping.CREATE_OBJECT)
                        .body (req)
                        .retrieve ()
                        .body (ServerCreateObjectResponse.class);
            return res;
        }
        catch (Exception e)
        {
            return new ServerCreateObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }

    }

    public ServerFindObjectResponse findObject (AdminFindObject req) {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerFindObjectResponse res = restClient.post()
                                            .uri (getUriBase() + ApiMapping.FIND_OBJECT)
                                            .body (req)
                                            .retrieve ()
                                            .body (ServerFindObjectResponse.class);
            return res;
        }
        catch (Exception e) {
            return new ServerFindObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }

    public ServerDeleteObjectResponse deleteObject (AdminDeleteObject req) {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerDeleteObjectResponse res = restClient.post()
                                                .uri (getUriBase() + ApiMapping.DELETE_OBJECT)
                                                .body (req)
                                                .retrieve ()
                                                .body (ServerDeleteObjectResponse.class);
            return res;
        }
        catch (Exception e) {
            return new ServerDeleteObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }

    public ServerUpdateObjectResponse updateObject (AdminUpdateObject req) {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerUpdateObjectResponse res = restClient.post()
                                                .uri (getUriBase() + ApiMapping.UPDATE_OBJECT)
                                                .body (req)
                                                .retrieve ()
                                                .body (ServerUpdateObjectResponse.class);
            return res;
        }
        catch (Exception e) {
            return new ServerUpdateObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }

    public void openSubStage (Stage subStage, Region rg)
    {
        subStage.initModality (Modality.APPLICATION_MODAL);
        subStage.initOwner (this.stage);
        Scene subStageScene = new Scene (rg);
        subStage.setScene (subStageScene);
        subStage.show ();
    }

    public void openWarningNoti (String str, FontIcon icon)
    {
        final Notification msg = new Notification (
            str,
            icon
        ); 
        msg.getStyleClass ().addAll (
            Styles.WARNING, Styles.ELEVATED_1
        );
        msg.setPrefWidth (300);
        msg.setPrefHeight (80);
        msg.setMaxHeight (80);

        StackPane.setAlignment (msg, Pos.TOP_RIGHT);
        StackPane.setMargin (msg, new Insets (10, 10, 0, 0));

        msg.setOnClose (e -> {
            Timeline out = Animations.slideOutUp (msg, Duration.millis (250));
            out.setOnFinished (f -> getStackPane().getChildren ().remove (msg));
            out.playFromStart ();
        });

        Timeline in = Animations.slideInDown (msg, Duration.millis (250));
        if (!getStackPane().getChildren().contains (msg)) {
            getStackPane().getChildren ().add (msg);
        }
        in.playFromStart ();
    }

    public StackPane getStackPane ()
    {
        return stackPane;
    }

    public Scene getView() {
        return stage.getScene();
    }
    public void setTitle(String s) {
        stage.setTitle(s);
    }
    public void setRoot(Parent n) {
        // this.getView().setRoot(n);
        this.getStackPane ().getChildren ().clear ();
        this.getStackPane ().getChildren ().add (n);
    }

    public void setUriBase(String url) {
        clientModel.setUriBase ( url );
    }
    public void setUriBase(String ip, String port) {
        clientModel.setUriBase ( String.format("http://%s:%s", ip, port) );
    }
    public String getUriBase () {
        return clientModel.getUriBase ();
    }

    public String getToken () {
        return clientModel.getToken ();
    }
    public void setToken (String token) {
        clientModel.setToken (token);
    }

    public ClientModel getClientModel () {
        return clientModel;
    }

    public ClientInteractor getClientInteractor () {
        return clientInteractor;
    }

    public CreatePersonModel getCreatePersonModel () {
        return createPersonModel;
    }
    public FindPersonModel getFindPersonModel () {
        return findPersonModel;
    }
    public UpdatePersonModel getUpdatePersonModel () {
        return updatePersonModel;
    }

    public Scene getScene () {
        return scene;
    }

}
