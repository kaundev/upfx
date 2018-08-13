package com.upfx.result;

import com.upfx.controller.Controller;
import com.upfx.screens.Stages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jboss.weld.exceptions.UnsatisfiedResolutionException;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Result {

    private ClassRequest classRequest;
    private Instance<FXMLLoader> fxmlLoaderInstance;
    private Instance<List<Controller>> controllerListInstance;
    private Stages stages;
    private Instance<ResourceBundle> resourceBundleInstance;

    @Inject
    public Result(ClassRequest classRequest, @FXMLProducerQualifier Instance<FXMLLoader> fxmlLoaderInstance,
                  Instance<List<Controller>> controllerListInstance, Stages stages,
                  Instance<ResourceBundle> resourceBundleInstance) {
        this.classRequest = classRequest;
        this.fxmlLoaderInstance = fxmlLoaderInstance;
        this.controllerListInstance = controllerListInstance;
        this.stages = stages;
        this.resourceBundleInstance = resourceBundleInstance;
    }

    public <T extends Controller> T render(String path, String title, Object... objects){
        classRequest.setParameters(objects);
        classRequest.setPath(path);
        classRequest.setTitle(title);
        classRequest.setRequestType(RequestType.RENDER);

        FXMLLoader fxmlLoader = fxmlLoaderInstance.get();
        ResourceBundle resourceBundle=null;
        try{
            resourceBundle = resourceBundleInstance.get();
        }catch (UnsatisfiedResolutionException e){ }

        try {
            fxmlLoader.setLocation(getClass().getResource(path));
            fxmlLoader.setResources(resourceBundle);
            Parent load = fxmlLoader.load();

            Controller controller = fxmlLoader.getController();
            if (controller==null)
                return null;

            if (stages.getPrimaryStage().getScene()==null)
                stages.getPrimaryStage().setScene(new Scene(load));
            else
                stages.getPrimaryStage().getScene().setRoot(load);

            stages.getPrimaryStage().setTitle(title);

            List<Controller> controllers = controllerListInstance.get();
            for (int j = controllers.size() - 1; j >= 0; j--) {
                Controller ctrl = controllers.get(j);
                ctrl.setActualStage(stages.getPrimaryStage());
                for (Method method : ctrl.getClass().getMethods()) {
                    if (method.getName().equals("initiate")) {
                        try {
                            method.invoke(ctrl, objects);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            try {
                                method.invoke(ctrl);
                            } catch (IllegalAccessException e1) {
                                e1.printStackTrace();
                            } catch (InvocationTargetException e1) {
                                e1.printStackTrace();
                            } catch (IllegalArgumentException el) {
                            }
                        }
                    }
                }
            }

            return (T) controller;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public <T extends Controller> T openPopup(String path, String title, Object... parameters){
        classRequest.setParameters(parameters);
        classRequest.setPath(path);
        classRequest.setRequestType(RequestType.OPEN_POPUP);

        FXMLLoader fxmlLoader = fxmlLoaderInstance.get();
        ResourceBundle resourceBundle=null;
        try{
            resourceBundle = resourceBundleInstance.get();
        }catch (UnsatisfiedResolutionException e){ }


        try {
            fxmlLoader.setLocation(getClass().getResource(path));
            fxmlLoader.setResources(resourceBundle);

            Parent load = fxmlLoader.load();
            Controller controller = fxmlLoader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setScene(new Scene(load));

            List<Controller> controllers = controllerListInstance.get();
            for (Controller ctrl : controllers) {
                for (Method method : ctrl.getClass().getMethods()) {
                    if (method.getName().equals("initiate")){
                        try {
                            method.invoke(ctrl, parameters);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e){

                        }
                    }
                    ctrl.setActualStage(stage);
                }
            }

            stages.setPopupStage(stage);
            stages.getPopupStage().setOnCloseRequest(we -> stages.setPopupStage(null));

            stage.show();
            return (T) controller;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T extends Controller> T getPane(String path, Object... parameters){
        try {
            FXMLLoader fxmlLoader = fxmlLoaderInstance.get();
            fxmlLoader.setLocation(getClass().getResource(path));

            ResourceBundle resourceBundle=null;
            try{
                resourceBundle = resourceBundleInstance.get();
            }catch (UnsatisfiedResolutionException e){ }
            if (resourceBundle!=null)
                fxmlLoader.setResources(resourceBundle);

            fxmlLoader.load();
            Controller controller = fxmlLoader.getController();

            List<Controller> controllers = controllerListInstance.get();
            for(int j = controllers.size() - 1; j >= 0; j--){
                Controller ctrl = controllers.get(j);
                List<Class<?>> parametersTypes = new ArrayList<>();
                for (Object parameter : parameters) {
                    parametersTypes.add(parameter.getClass());
                }
                try {
                    Method initiate = ctrl.getClass().getMethod("initiate", parametersTypes.toArray(new Class<?>[parametersTypes.size()]));
                    initiate.invoke(ctrl, parameters);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }

            return (T) controller;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void openNewWindow(String path, String title){
        /*// Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setMinHeight(window.getMinHeight());
        stage.setMinWidth(window.getMinWidth());
        stage.setTitle(title);
        //stage.setScene(new Scene(screens.getRoot()));
        stage.show();

        // this.fxmlLoader = fxmlLoader.getController();*/
    }
}
