package com.upfx.result;

import javafx.stage.Stage;

import javax.inject.Singleton;

@Singleton
public class ClassRequest implements Cloneable{

    private Object[] parameters;
    private String path;
    private String title;
    private RequestType requestType;
    private Stage actualStage;

    public Object[] getParameters(){
        return parameters;
    }
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
    public RequestType getRequestType() {
        return requestType;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Stage getActualStage() {
        return actualStage;
    }

    public void setActualStage(Stage actualStage) {
        this.actualStage = actualStage;
    }

    @Override
    public ClassRequest clone(){
        try {
            return (ClassRequest) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
