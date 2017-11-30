package common;

import server.model.AccountException;
import server.model.FileObj;
import server.model.FileObjException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface FileServer extends Remote {

    public static final String SERVER_NAME = "FILE_SERVER";

    void register(String username, String password) throws RemoteException,AccountException;

    void unregister(String username, String password)throws RemoteException,AccountException;

    void login(String username, String password,Client remoteClient)throws RemoteException,AccountException;

    boolean FileExist(String filename)throws RemoteException;

    void upload(String fileowner, String filename,Boolean premision, Boolean write, Boolean read)throws RemoteException,FileObjException;

    boolean fileacess(String accountName, String filename)throws RemoteException;

    void download(String accountName,String Filename) throws RemoteException,FileObjException;

    void delete(String acountname, String filename)throws RemoteException,FileObjException;

    void update(String accountName, String fileowner,Boolean premision, Boolean write, Boolean read)throws RemoteException,FileObjException;

     ArrayList<String> listfiles(String accountName) throws RemoteException;

     void track(String owner,String filename) throws RemoteException,FileObjException;

}
