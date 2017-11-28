package server.controller;

import common.FileServer;
import server.integration.FileDAO;
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
    public synchronized void register(String username, String password) throws RemoteException {
        //anropa create account i DOA
        //får sen tillbaka om det lyckades namn är primärnyckel så msåte ju avra unikt.
        //kan kolla först om namn finns i databas sen lägga till det
    }

    @Override
    public synchronized void unregister(String username, String password) throws RemoteException {
        //kolla om namn i databas
        //sen kör sql med delte för det kontot
    }

    @Override
    public synchronized boolean login(String username, String password) throws RemoteException {
        //kolla om account stämmer överäns med någon rad i db namn och pass
        //om ej returneras false till klienten
        return false;
    }

    @Override
    public synchronized void upload() throws RemoteException {
        //behöver användarens namn
        //behöver filensnamn
        //returnera en boolean för om finns redan
        //gå sen i recive
    }

    @Override
    public synchronized File get(String Filename) throws RemoteException {
        //ska nog skicka filen endast så behöver inte skicka file objektet
        //ta emot namn på person
        //fil namn och sen anropa handler för att skicka filen
        return null;
    }

    @Override
    public synchronized void delete(String Filename) throws RemoteException {
        //kolla permissions
        //kör sen delete på filen
        //måste även gå in i db söka på namn ochh ta bort
    }

    @Override
    public synchronized void update() throws RemoteException {
        //samma som send fast här måste filensnamn redan finnas för att få skicka
    }

    @Override
    public synchronized ArrayList<File> listfiles() throws RemoteException {
        //skicka lista med filer vi har kolla permissions = private = false
        //och om read är true
        //får sen printas i klienten
        return null;
    }

    @Override
    public synchronized void track(String filename) throws RemoteException {
        //lägg till en bollean i db och file för track
        //så kolla den varje gång en fil ska updateras eller deltas eller om get och skicka en notis
        //behöver då spara en referns till den klienten
        //kala på en metod i klienten med rmi.
    }


}
