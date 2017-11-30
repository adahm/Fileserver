package client.view;

import client.connect.Clientconnect;
import common.Client;
import common.FileServer;
import server.model.AccountException;
import server.model.FileObj;
import server.model.FileObjException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
public class Ui extends UnicastRemoteObject implements Runnable, Client {
        private final Scanner in = new Scanner(System.in);
        private Boolean go = true;
        private Boolean loggedin = false;
        private String input;
        private String cmd;
        private FileServer server;
        private String accountName;
        public Ui() throws RemoteException{
        }
        public void start(){
            new Thread(this).start();
        }
        @Override
        public void run() {
            try {
                setServer();
            }catch (Exception e){
                e.printStackTrace();
            }
            //first set of commands
            System.out.println("Log in to do file operations");
            do {
                try {
                    System.out.println("Write CMDS to see commands");
                    input = in.nextLine();
                    String[] splitinput = input.split(" ");
                    String cmd = splitinput[0];
                    switch(cmd){
                        case "REGISTER": //take the input and try to register at server
                            server.register(splitinput[1],splitinput[2]);
                            break;
                        case "UNREGISTER":
                            server.unregister(splitinput[1],splitinput[2]);
                            break;
                        case "LOGIN": //when logged in send the object so the server can do call by refrence when we want to track a file
                            server.login(splitinput[1],splitinput[2],this);
                            loggedin = true;
                            System.out.println("logged in");
                            accountName = splitinput[1];
                            loginview();//move to logged in set of commands
                            break;
                        case "CMDS":
                            printCMDS();
                            break;
                        case "QUIT":
                            go = false;
                            break;
                        default:
                            System.out.println("not known command");
                     }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }while(go);
            System.exit(1);
        }
        public void loginview() throws RemoteException{
            do{
                try {
                    String filename;
                    System.out.println("Write CMDS to see commands");
                    input = in.nextLine();
                    String[] splitinput = input.split(" ");
                    String cmd = splitinput[0];
                    switch (cmd) {
                        case "UPLOAD": //upload a file
                            System.out.println("Filename:");
                            filename = in.nextLine();
                            System.out.println("Private y/n:");
                            String prem = in.nextLine();
                            if (prem.equals("n")) {
                                System.out.println("Write y/n:");
                                String write = in.nextLine();
                                Boolean writeprem;
                                if (write.equals("y"))
                                    writeprem = true;
                                else {
                                    writeprem = false;
                                }
                                System.out.println("Read y/n:");
                                String read = in.nextLine();
                                Boolean readprem;
                                if (read.equals("y"))
                                    readprem = true;
                                else {
                                    readprem = false;
                                }
                                //call the upploadfile method to connect to server
                                upploadFile(filename);
                                //call the upload method to add the file enrty in DB and to say to the server to accept connection
                                server.upload(accountName, filename, false, writeprem, readprem);
                            } else {
                                //if we choosed private file to true set write and read to false
                                upploadFile(filename);
                                server.upload(accountName, filename, true, false, false);
                            }
                            break;
                        case "UPDATE"://update a file
                            System.out.println("Filename:");
                            filename = in.nextLine();
                            System.out.println("Private y/n:");
                            prem = in.nextLine();
                            if (prem.equals("n")) {
                                System.out.println("Write y/n:");
                                String write = in.nextLine();
                                Boolean writeprem;
                                if (write.equals("y"))
                                    writeprem = true;
                                else {
                                    writeprem = false;
                                }
                                System.out.println("Read y/n:");
                                String read = in.nextLine();
                                Boolean readprem;
                                if (read.equals("y"))
                                    readprem = true;
                                else {
                                    readprem = false;
                                }
                                //call the upploadfile method to connect to server
                                upploadFile(filename);
                                //call the upload method to add the file enrty in DB and to say to the server to accept connection
                                server.update(accountName,filename, false,writeprem,readprem);
                            } else {
                                //if set to private set write and read to false
                                upploadFile(filename);
                                server.update(accountName, filename, true, false, false);
                            }
                            break;
                        case "DELETE"://delete file on the server
                            System.out.println("Filename:");
                            filename = in.nextLine();
                            //get the filename and send it to server
                            server.delete(accountName, filename);
                            break;
                        case "RETREIVE":
                            System.out.println("Filename:");
                            filename = in.nextLine();
                            server.download(accountName, filename);
                            //send the message to server that we want to dowlaod and then connect to get the file
                            downloadFile(filename);
                            break;
                        case "LIST"://list files on server we can se
                            ArrayList<String> filelist = server.listfiles(accountName);
                            for (String f : filelist) {
                                System.out.println(f);
                            }
                            break;
                        case "TRACK"://track a file set tracker atribute to file so when someone update or delete or get the file we will get snet a message
                            System.out.println("Filename:");
                            filename = in.nextLine();
                            server.track(accountName, filename);
                            break;
                        case "CMDS":
                            printCMDS();
                            break;
                        case "LOGOUT":
                            loggedin = false;
                            break;

                        default:
                            System.out.println("not known command");

                    }
                }catch (FileObjException e){
                    System.out.println(e.getMessage());
                }
            }while(loggedin);
            System.out.println("logged out");
        }

        public void upploadFile(String filename){
            try {
                Clientconnect c = new Clientconnect();
                c.connect(); //call connection method i clientconnect
                System.out.println("got connection");
                URL url = getClass().getResource(filename);
                File file = new File(url.getPath());//get the path to the file it is assumed that the file is in the view folder
                c.sendFile(file); //call the sendfile in clientconnect
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        public void downloadFile(String filename){
            try {
                Clientconnect c = new Clientconnect();
                c.connect(); //connect to the server
                System.out.println("go connection");
                File f = new File("C:/Users/Andreas/IdeaProjects/Fileserver/src/client/view/"+filename); //set path to where the dowload file wil be saved
                f.createNewFile(); //create the file if it doen´t exist
                c.downloadFile(f);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        public void setServer() throws NotBoundException, MalformedURLException,RemoteException{
            server = (FileServer) Naming.lookup("//localhost/"+ FileServer.SERVER_NAME);

        }
    public void getTrackerInfo(String msg){
            System.out.println(msg);
    }
    public void printCMDS(){
        System.out.println("REGISTER name password");
        System.out.println("UNREGISTER name password");
        System.out.println("LOGIN name password");
        System.out.println("LOGOUT");
        System.out.println("LIST");
        System.out.println("QUIT");
        System.out.println("UPLOAD");
        System.out.println("UPDATE");
        System.out.println("RETREIVE");
        System.out.println("DELETE");
        System.out.println("TRACK");
    }
}
