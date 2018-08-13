package com.upfx.controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {

    protected URL location;
    protected ResourceBundle resourceBundle;
    protected Stage actualStage;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        this.location = location;
        this.resourceBundle = resourceBundle;

    }

    protected void requestFocus(Node node){
        Platform.runLater(() -> node.requestFocus());
    }

    public Stage getActualStage() {
        return actualStage;
    }

    public void setActualStage(Stage actualStage) {
        this.actualStage = actualStage;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}

