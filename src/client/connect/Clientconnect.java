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
        //connect to the server and set up output and input streams
    }

    //read from the selected file and write to the outputbuffer and then close the connections to the server
    public void sendFile(File fileToSend)throws IOException{
        byte[] buffer = new byte[(int)fileToSend.length()];
        BufferedInputStream filReader = new BufferedInputStream(new FileInputStream(fileToSend));
        filReader.read(buffer,0,buffer.length);
        output.write(buffer,0,buffer.length);
        output.flush();
        filReader.close();
        output.close();
        input.close();
        client.close();
        System.out.println("sent");
    }
    //create a stream to write to the file and get the input from the server and write it to the file and then close the connection to the server
    public void downloadFile(File reciverFile) throws IOException{
        byte[] buffer = new byte[1000000];
        BufferedOutputStream fileWrite = new BufferedOutputStream(new FileOutputStream(reciverFile));
        int n;
        System.out.println("reading input");
        while ((n = input.read(buffer, 0, buffer.length)) != -1) {
            fileWrite.write(buffer, 0, n);
        }
        fileWrite.flush();
        fileWrite.close();
        System.out.println("got file");

        output.close();
        input.close();
        client.close();
    }

}
