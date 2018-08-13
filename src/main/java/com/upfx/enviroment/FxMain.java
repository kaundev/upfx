package com.upfx.enviroment;

import com.upfx.screens.Stages;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;

@Singleton
public abstract class FxMain extends Application implements Serializable {

    @Inject
    protected Stages stages;

    protected Stage primaryStage;


    private Weld weld;

    protected static Class<? extends FxMain> startApplicationClass;
    protected static Class<? extends Preloader> clazzPreLoader;

    public static void initiate(Class<? extends FxMain> startApplicationClass, Class<? extends Preloader> clazzPreLoader, String[] args){
        FxMain.startApplicationClass = startApplicationClass;
        FxMain.clazzPreLoader = clazzPreLoader;

        LauncherImpl.launchApplication(startApplicationClass, clazzPreLoader, args);
    }


    public static void initiate(Class<? extends FxMain> startApplicationClass, String[] args){
        FxMain.startApplicationClass = startApplicationClass;
        LauncherImpl.launchApplication(startApplicationClass, args);
    }

    @Override
    public void init() throws Exception {
        weld = new Weld();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        weld.initialize().select(startApplicationClass).get().start(primaryStage, getParameters());
    }

    @Override
    public void stop()
    {
        weld.shutdown();
    }


    public void start(Stage primaryStage, Parameters parameters){
        this.primaryStage = primaryStage;

        stages.setPrimaryStage(primaryStage);
        beforeStart();

        setPrimaryStage();

        firstScreen();

        stages.getPrimaryStage().show();

        afterStart();

    }

    protected void beforeStart(){}

    protected abstract void firstScreen();

    protected void afterStart(){}

    protected void setPrimaryStage(){

        stages.getPrimaryStage().setWidth(800.0);
        stages.getPrimaryStage().setHeight(600.0);

    }
}
