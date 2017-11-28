package server.connect;

import java.io.IOException;
import java.net.ServerSocket;

public class FileServer {
    public static void main(String[] args){
        try {
            ServerSocket serverSocket = new ServerSocket(2000);
            try {
                while(true){
                    new FileHandler(serverSocket.accept()); //lägg till forkjoinpool
                }
            }catch (IOException error){
                error.printStackTrace();
            }
        }catch (IOException error){
            error.printStackTrace();
        }
    }
}
