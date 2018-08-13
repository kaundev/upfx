package com.upfx.result;

import com.upfx.controller.Controller;
import javafx.fxml.FXMLLoader;
import org.jboss.weld.exceptions.CreationException;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

public class FXMLProducer {

    @Inject
    private Instance<Controller> instance;

    @Inject
    private ControllerListProducer controllerListProducer;

    @Produces @FXMLProducerQualifier
    public FXMLLoader createLoader(InjectionPoint injectionPoint) {

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(param -> {
            try {
                Class<Controller> param1 = (Class<Controller>) param;
                Controller o = instance.select(param1).get();
                controllerListProducer.addController(o);
                return o;
            } catch (CreationException e){
                return null;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        });
        return loader;
    }



}
