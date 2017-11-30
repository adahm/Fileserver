package server.connect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnect {
    ServerSocket serverSocket;
    //set up serversocket to accept connections to send files
    public ServerConnect() throws IOException{
        serverSocket = new ServerSocket(2000);
    }
    //get the connection from the the client that wants to dowload/uppload a file
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
