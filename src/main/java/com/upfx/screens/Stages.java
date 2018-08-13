package com.upfx.screens;

import javafx.stage.Stage;

import javax.inject.Singleton;

@Singleton
public class Stages {

    private Stage primaryStage;
    private Stage popupStage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPopupStage() {
        return popupStage;
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

}
