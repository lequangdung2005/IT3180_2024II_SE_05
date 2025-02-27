package com.hust.ittnk68.cnpm.controller;

import com.hust.ittnk68.cnpm.model.Account;
import com.hust.ittnk68.cnpm.model.SceneModel;

import org.springframework.web.client.RestClient;

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
    // public Stage getStage() {
    //     return stage;
    // }
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

    public RestClient getRestClient() {
        return restClient;
    }
}
