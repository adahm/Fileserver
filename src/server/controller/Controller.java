package server.controller;

import common.FileServer;
import server.integration.FileDAO;
import server.model.Account;
import server.model.File;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Controller extends UnicastRemoteObject implements FileServer{
    private final FileDAO fileDb;

    public Controller()throws RemoteException{
        super();
        fileDb = new FileDAO();
    }

    @Override
    public synchronized void register(String username, String password) {
        if (fileDb.findAccount(username) != null){

        }
        else {
            fileDb.createAccount(new Account(username,password,fileDb));
        }
        //anropa create account i DOA
        //får sen tillbaka om det lyckades namn är primärnyckel så msåte ju avra unikt.
        //kan kolla först om namn finns i databas sen lägga till det
    }

    @Override
    public synchronized void unregister(String username, String password) {
        if (fileDb.tryLogin(username,password)){
            fileDb.deleteAccount(username);
        }
        //kolla om namn i databas
        //sen kör sql med delte för det kontot
    }

    @Override
    public synchronized boolean login(String username, String password) {
        return fileDb.tryLogin(username,password);
    }

    @Override
    public synchronized void upload(String fileowner, String filename){
        //behöver användarens namn
        //behöver filensnamn
        //skapa en metod för att öppna upp en socket mot client
        //ta emot connection där och ta emot filen

        //returnera en boolean för om finns redan
        //gå sen i recive
    }

    @Override
    public synchronized File get(String Filename)  {
        //ska nog skicka filen endast så behöver inte skicka file objektet
        //ta emot namn på person
        //ha en metod där en serversocket sätts upp.
        //skicka till clienten som har kopplat upp sig

        //fil namn och sen anropa handler för att skicka filen
        return null;
    }

    @Override
    public synchronized void delete(String Filename)  {
        //kolla permissions
        //kör sen delete på filen
        //måste även gå in i db söka på namn ochh ta bort
    }

    @Override
    public synchronized void update()  {
        //samma som send fast här måste filensnamn redan finnas för att få skicka
    }

    @Override
    public synchronized ArrayList<File> listfiles()  {
        //skicka lista med filer vi har kolla permissions = private = false
        //och om read är true
        //får sen printas i klienten
        return null;
    }

    @Override
    public synchronized void track(String filename)  {
        //lägg till en bollean i db och file för track
        //så kolla den varje gång en fil ska updateras eller deltas eller om get och skicka en notis
        //behöver då spara en referns till den klienten
        //kala på en metod i klienten med rmi.
    }

    public void sendFile(String filename){
        //sätt upp serversocket och acceptera connection
        //läs från filen och skriv till outputstream
    }

    public void reciveFile(String filename){
        //sätt upp en serversocket
        //läs från inpoutstream och skriv till en fil
    }

}
