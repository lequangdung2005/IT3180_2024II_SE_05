package com.hust.ittnk68.cnpm.controller;

import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.SceneModel;
import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

import org.springframework.web.client.RestClient;

import com.hust.ittnk68.cnpm.communication.ApiMapping;
import com.hust.ittnk68.cnpm.communication.ClientMessageStartSession;
import com.hust.ittnk68.cnpm.communication.ServerResponseBase;
import com.hust.ittnk68.cnpm.communication.ServerResponseStartSession;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientSceneController {
    private Stage stage;
    private SceneModel sceneModel;
    private RestClient restClient;

    public ClientSceneController() {
        stage = null;
        sceneModel = new SceneModel();
        restClient = RestClient.create();
    }

    public void start(Stage stage, String title, double width, double height) {
        this.stage = stage;
        stage.setTitle(title);
        Scene scene = new Scene(new LoginController(this).getView(), width, height);
        stage.setScene(scene);
	stage.show(); 
    }

    public ServerResponseStartSession startSession (ClientMessageStartSession message)
    {
        // loading screen ...

        ServerResponseStartSession res;
        try {
            res = restClient.post()
                    .uri (getUriBase() + ApiMapping.START_SESSION)
                    .body (message)
                    .retrieve ()
                    .body (ServerResponseStartSession.class);
        }
        catch (Exception e)
        {
            return new ServerResponseStartSession (ResponseStatus.NOT_OK, "can't connect to server", "", AccountType.UNVALID);
        }
        // stop loading screen ...
        return res;
    }

    public void endSession ()
    {
        try {
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

    public Scene getView() {
        return stage.getScene();
    }
    public void setTitle(String s) {
        stage.setTitle(s);
    }
    public void setRoot(Parent n) {
        this.getView().setRoot(n);
    }

    public void setUriBase(String url) {
        sceneModel.setUriBase(url);
    }
    public void setUriBase(String ip, String port) {
        sceneModel.setUriBase(ip, port);
    }
    public String getUriBase() {
        return sceneModel.getUriBase();
    }

    public String getToken ()
    {
        return sceneModel.getToken ();
    }
    public void setToken (String token)
    {
        sceneModel.setToken (token);
    }
}
