package server.connect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnect {
    ServerSocket serverSocket;
    public ServerConnect() throws IOException{
        serverSocket = new ServerSocket(2000);
    }
    public Socket getConnection() {
        try {
            Socket s =  serverSocket.accept();
            return s;
        } catch (IOException error) {
            error.printStackTrace();
        }
        return null;
    }
}
