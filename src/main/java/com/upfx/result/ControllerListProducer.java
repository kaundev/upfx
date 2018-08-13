package com.upfx.result;


import com.upfx.controller.Controller;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ControllerListProducer {

    private List<Controller> controllerList = new ArrayList<>();

    void addController(Controller controller){
        try {
            this.controllerList.add(controller);
        }catch (NullPointerException e){
            this.controllerList = new ArrayList<>();
            this.controllerList.add(controller);
        }
    }

    @Produces
    List<Controller> getControllers(){
        List<Controller> returnControllers = new ArrayList<>();
        try {
            returnControllers.addAll(controllerList);
            controllerList = null;
        }catch (NullPointerException e){}

        return returnControllers;
    }
}
