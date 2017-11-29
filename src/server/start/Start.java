package server.start;

import server.controller.Controller;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Start {
    public static void main(String[] args) {
        try{
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.rebind(Controller.SERVER_NAME,new Controller());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
