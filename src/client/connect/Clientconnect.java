package client.connect;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;
//fixa en sendFIle metod

//fixa en reciveFile metod


public class Clientconnect {
    private Socket client;

    private InputStream input;
    private OutputStream output;

    public void connect() throws IOException {
        client = new Socket("localhost", 2000);
        input = client.getInputStream();
        output = client.getOutputStream();
    }
    public void sendFile(File fileToSend)throws IOException{
        byte[] buffer = new byte[(int)fileToSend.length()];
        BufferedInputStream filReader = new BufferedInputStream(new FileInputStream(fileToSend));
        filReader.read(buffer,0,buffer.length);
        output.write(buffer,0,buffer.length);
        output.flush();
    }

    public void downloadFile(File reciverFile) throws IOException{
        byte[] buffer = new byte[1000000];
        reciverFile.createNewFile();
        BufferedOutputStream fileWrite = new BufferedOutputStream(new FileOutputStream(reciverFile));
        int n;
        while ((n = input.read(buffer)) != -1) {
            fileWrite.write(buffer, 0, n);
        }
        fileWrite.close();
    }

}
