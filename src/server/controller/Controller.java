package server.controller;

import common.Client;
import common.FileServer;
import server.connect.FileHandler;
import server.connect.ServerConnect;
import server.integration.FileDAO;
import server.model.Account;
import server.model.AccountException;
import server.model.FileObj;
import server.model.FileObjException;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;

public class Controller extends UnicastRemoteObject implements FileServer{
    private final FileDAO fileDb; //database that keeps the accaounts and records of the files
    private ServerConnect server; //the serversocket where we will accept connections to send files to clients
    private HashMap<String,Client> clientHashMap;//hashmap to keep client object refrence to be able to do call by refrence when a file is tracked.
    public Controller()throws RemoteException{
        super();
        fileDb = new FileDAO();
        clientHashMap = new HashMap<>();
        try {
            server = new ServerConnect();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //user wants to register and account
    @Override //checks if usernmae already exist throw exeption  otherwise add an acount to the database
    public synchronized void register(String username, String password) throws AccountException{
        if (fileDb.findAccount(username) != null){
            throw new AccountException("Account "+username+" allready exist");
        }
        else {
            fileDb.createAccount(new Account(username,password));
        }
    }
    //user want to unregister an account
    @Override
    //check if the name and password is correct and then delete the account from the DB otherwise throw a exec
    public synchronized void unregister(String username, String password) throws AccountException{
        if (fileDb.tryLogin(username,password)){
            fileDb.deleteAccount(username);
        }
        else {
            throw new AccountException("Wrong username or password");
        }
        //kolla om namn i databas
        //sen kör sql med delte för det kontot
    }
    //user wants to login
    @Override//see if acount is in database then add the user and the client object refrence to the hashmap oterhwsie throw exec
    public synchronized void login(String username, String password, Client remoteClient)throws AccountException {
        if(fileDb.tryLogin(username,password)){
            clientHashMap.put(username,remoteClient);
        }
        else {
            throw new AccountException("Wrong username or password");
        }
    }

    //user wants to uppload a file
    @Override//throw exec if file exist otherwise call recive to get the file thourgh tcp and create the file in DB
    public synchronized void upload(String fileowner, String filename,Boolean premision, Boolean write, Boolean read)throws FileObjException{
        if(fileDb.findFile(filename) != null){
            throw new FileObjException("File: "+filename+" allready exist");
        }
        else {
            int Size = reciveFile(filename);
            fileDb.createFile(new FileObj(filename,Size,fileowner,premision,read,write,false));
        }
    }

    //sends the requested file to the client
    @Override
    public synchronized void download(String accountName,String filename) throws FileObjException{
        String owner;
        //check if the user has permission to read the file
        if(fileDb.checkAcess(accountName,filename)){
            if((owner = fileDb.checkIfTracked(filename,accountName)) != null){ //if the file is tracked send a message that it is downloaded to the owner
                System.out.println("tracked");
                Client c = clientHashMap.get(owner);
                System.out.println(owner);
                try{
                    c.getTrackerInfo(accountName+" downloaded "+ filename);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
            //send the file to the requester
            ForkJoinPool.commonPool().execute(() -> sendFile(filename));
        }
        //throw exec if the client don´t have acess to the file
        else {
            throw new FileObjException("No access to file");
        }
    }

    //deletes the requested file
    @Override
    public synchronized void delete(String accountName, String filename) throws FileObjException {
        String owner;
        //check if file is public and if we are allowed to delete
        if(fileDb.checkWritePrem(accountName,filename)){
            if((owner = fileDb.checkIfTracked(filename,accountName)) != null){
                Client c = clientHashMap.get(owner);
                try{//if file tracked send message to owner
                    c.getTrackerInfo(accountName+" deleted "+ filename);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
            //delte file info from DB and the file from the directory
            fileDb.deleteFile(filename);
            File f = new File("C:/Users/Andreas/IdeaProjects/Fileserver/src/server/serverFiles/"+filename);
            f.delete();
        }
        else {
            throw new FileObjException("No access to file");
        }
    }

    //updates the file
    @Override
    public synchronized void update(String accountName,String filename,Boolean premision, Boolean write, Boolean read)throws FileObjException {
        String owner;
        //check if we are allowed to write
        if(fileDb.checkWritePrem(accountName,filename)){
            if((owner = fileDb.checkIfTracked(filename,accountName)) != null){ //if file is tracked send msg to the owner of the file
                Client c = clientHashMap.get(owner);
                try{
                    c.getTrackerInfo(accountName+" updated "+ filename);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
            int Size = reciveFile(filename); //get the updated file from the client
            fileDb.updateFile(filename,Size,premision,read,write);//update the entry in the DB
        }
        else { //if no the owner and we are not allowed to write throw exec
            throw new FileObjException("No access to file");
        }
    }

    @Override
    public synchronized ArrayList<String> listfiles(String accountName)  {
        //get list of files on the server and send to client
        return fileDb.getFiles(accountName);
    }

    //set a file to be tracked has to be the owner of the file that call the function
    @Override
    public synchronized void track(String owner, String filename)throws FileObjException {
        if(fileDb.checkIfFileOwner(owner,filename)){
            fileDb.setTracker(filename,true);
        }
        else { //if not the file owner throw exec
            throw new FileObjException("Not file owner");
        }
    }

    public void sendFile(String filename){
        try{
            //get the connection to the client and call the filehandler to send the file
            Socket client = server.getConnection();
            System.out.println("got connection");
            new FileHandler().senFile(client,filename);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    //set up connection to the client and get the file
    public int reciveFile(String filename){
        try{
            Socket client = server.getConnection();
            //retirve the size to save in the DB
            int Size = new FileHandler().reciveFile(client,filename);
            return Size;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
