package client.view;

import common.FileServer;
import server.model.File;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
public class Ui implements Runnable{
        private final Scanner in = new Scanner(System.in);
        private Boolean go = true;
        private String input;
        private String cmd;
        private FileServer server;

        public Ui() throws RemoteException{

        }
        public void start(){
            new Thread(this).start();
        }
        @Override
        public void run() {
            try {
                setServer();
                do {
                    System.out.println("AT any time write QUIT to quit the app:" +
                            "or Write START to start a new game");
                    input = in.nextLine();
                    String[] splitinput = input.split(" ");
                    String cmd = splitinput[0];

                    switch(cmd){
                        case "REGISTER":
                            server.register(splitinput[1],splitinput[2]);
                            break;
                        case "UNREGISTER":
                            server.unregister(splitinput[1],splitinput[2]);
                            break;
                        case "LOGIN":
                            boolean login = server.login(splitinput[1],splitinput[2]);
                            if(login){
                                loginview();
                            }
                            break;
                        case "QUIT":
                            go = false;
                            break;


                        default:
                            System.out.println("not known command");

                    }

                }while(go);
            }catch (RemoteException  e){
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            catch (MalformedURLException e){}
            catch (NotBoundException e){}

        }
        public void loginview() throws RemoteException{
            do {
                System.out.println("what to do");
                input = in.nextLine();
                String[] splitinput = input.split(" ");
                String cmd = splitinput[0];
                switch(cmd){ //se till att skicka med acount för att om private file
                    //kanske lägg till oprtions när fille lddas upp om
                    case "UPLOAD": //upload a file
                        server.upload();
                        break;
                    case "UPDATE"://update a file
                        server.update();
                        break;
                    case "DELETE"://deleta file on the server
                        server.delete(splitinput[1]);
                        break;
                    case "RETREIVE":
                        server.get(splitinput[1]);
                        break;
                    case "LIST"://list files on server we can se
                        ArrayList<File> filelist = server.listfiles();
                        System.out.println(filelist); //fix print
                        break;
                    case "TRACK"://track a file set tracker atribute to file so when someone update or delete or get the file we will get snet a message
                        server.track(splitinput[1]);
                        break;
                    case "LOGOUT":
                        go = false;
                        break;

                    default:
                        System.out.println("not known command");

                }

            }while(go);
        }
        public void setServer() throws NotBoundException, MalformedURLException,RemoteException{
            server = (FileServer) Naming.lookup("//localhost/"+ FileServer.SERVER_NAME);

        }
}
