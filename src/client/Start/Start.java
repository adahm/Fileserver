package client.Start;
import client.view.Ui;

import java.rmi.RemoteException;

public class Start {
        public static void main(String[] args){
            try {
                new Ui().start();
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
}
