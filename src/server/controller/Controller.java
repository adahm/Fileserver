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
    private final FileDAO fileDb;
    private ServerConnect server;
    private HashMap<String,Client> clientHashMap;
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

    @Override
    public synchronized void register(String username, String password) throws AccountException{
        if (fileDb.findAccount(username) != null){
            throw new AccountException("Account "+username+" allready exist");
        }
        else {
            fileDb.createAccount(new Account(username,password,fileDb));
        }
        //anropa create account i DOA
        //får sen tillbaka om det lyckades namn är primärnyckel så msåte ju avra unikt.
        //kan kolla först om namn finns i databas sen lägga till det
    }

    @Override
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

    @Override
    public synchronized void login(String username, String password, Client remoteClient)throws AccountException {
        if(fileDb.tryLogin(username,password)){
            clientHashMap.put(username,remoteClient);
        }
        else {
            throw new AccountException("Wrong username or password");
        }
    }

    public synchronized boolean FileExist(String filename){
        if (fileDb.findFile(filename) != null){
            return true;
        }
        return false;
    }// ta nog bort

    @Override
    public synchronized void upload(String fileowner, String filename,Boolean premision, Boolean write, Boolean read)throws FileObjException{
        if(fileDb.findFile(filename) != null){
            throw new FileObjException("File: "+filename+" allready exist");
        }
        else {
            int Size = reciveFile(filename);
            fileDb.createFile(new FileObj(filename,Size,fileowner,premision,read,write,false));
        }
        //behöver användarens namn
        //behöver filensnamn
        //skapa en metod för att öppna upp en socket mot client
        //ta emot connection där och ta emot filen
        //klienten får kolla om filen finns
        //returnera en boolean för om finns redan
        //gå sen i recive
    }


    public synchronized boolean fileacess(String accountName,String filename){
        if(fileDb.checkAcess(accountName,filename)){
            return true;
        }
        return false;
    }

    @Override
    public synchronized void download(String accountName,String filename) throws FileObjException{
        if(fileDb.checkAcess(accountName,filename)){
            if(fileDb.checkIfTracked(filename,accountName)){
                Client c = clientHashMap.get(accountName);
                try{
                    c.getTrackerInfo(accountName+" downloaded "+ filename);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
            ForkJoinPool.commonPool().execute(() -> sendFile(filename));
        }
        else {
            throw new FileObjException("No access to file");
        }
        //ska nog skicka filen endast så behöver inte skicka file objektet
        //ta emot namn på person
        //ha en metod där en serversocket sätts upp.
        //skicka till clienten som har kopplat upp sig

        //fil namn och sen anropa handler för att skicka filen

    }

    @Override
    public synchronized void delete(String accountName, String filename) throws FileObjException {
        if(fileDb.checkAcess(accountName,filename)){
            if(fileDb.checkIfTracked(filename,accountName)){
                Client c = clientHashMap.get(accountName);
                try{
                    c.getTrackerInfo(accountName+" deleted "+ filename);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
            fileDb.deleteFile(filename);
            File f = new File("C:/Users/Andreas/IdeaProjects/Fileserver/serverFiles/"+filename);
            f.delete();
        }
        else {
            throw new FileObjException("No access to file");
        }

        //kolla permissions
        //kör sen delete på filen
        //måste även gå in i db söka på namn ochh ta bort
    }

    @Override
    public synchronized void update(String accountName,String filename,Boolean premision, Boolean write, Boolean read)throws FileObjException {
        if(fileDb.checkAcess(accountName,filename)){
            if(fileDb.checkIfTracked(filename,accountName)){
                Client c = clientHashMap.get(accountName);
                try{
                    c.getTrackerInfo(accountName+" updated "+ filename);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
            int Size = reciveFile(filename);
            fileDb.updateFile(filename,Size,premision,read,write);
        }
        else {
            throw new FileObjException("No access to file");
        }
        //samma som send fast här måste filensnamn redan finnas för att få skicka
    }

    @Override
    public synchronized ArrayList<String> listfiles(String accountName)  {
        //skicka lista med filer vi har kolla permissions = private = false
        //och om read är true
        //får sen printas i klienten
        return fileDb.getFiles(accountName);
    }

    @Override
    public synchronized void track(String owner, String filename)throws FileObjException {
        if(fileDb.checkIfFileOwner(owner,filename)){
            fileDb.setTracker(filename,true);
        }
        else {
            throw new FileObjException("Not file owner");
        }
        //lägg till en bollean i db och file för track
        //så kolla den varje gång en fil ska updateras eller deltas eller om get och skicka en notis
        //behöver då spara en referns till den klienten
        //kala på en metod i klienten med rmi.
    }

    public void sendFile(String filename){
        try{
            Socket client = server.getConnection();
            System.out.println("go connection");
            new FileHandler().senFile(client,filename);
        }catch (IOException e) {
            e.printStackTrace();
        }
        //sätt upp serversocket och acceptera connection
        //läs från filen och skriv till outputstream
    }

    public int reciveFile(String filename){
        try{
            Socket client = server.getConnection();
            int Size = new FileHandler().reciveFile(client,filename);
            return Size;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
        //sätt upp en serversocket
        //läs från inpoutstream och skriv till en fil
    }

}
