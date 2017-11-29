package common;

import server.model.File;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface FileServer extends Remote {

    public static final String SERVER_NAME = "FILE_SERVER";

    void register(String username, String password) throws RemoteException;

    void unregister(String username, String password)throws RemoteException;

    boolean login(String username, String password)throws RemoteException;

    void upload(String username,String filename)throws RemoteException;

    File get(String Filename) throws RemoteException;

    void delete(String Filename)throws RemoteException;

    void update()throws RemoteException;

    public ArrayList<File> listfiles() throws RemoteException;

    public void track(String filename) throws RemoteException;

}
