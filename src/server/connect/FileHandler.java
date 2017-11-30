package server.connect;
import java.io.*;
import java.net.Socket;

public class FileHandler {
    //send the file to the client though the socket obetained form when client connected
    public void senFile(Socket clientSocket, String filename) throws IOException{
        OutputStream output = clientSocket.getOutputStream();
        //get the file from the fileserver directory
        File f = new File("C:/Users/Andreas/IdeaProjects/Fileserver/src/server/serverFiles/"+filename); //fixa till map på server
        System.out.println("sending");
        //read the file and send it though the outputstream
        byte[] buffer = new byte[(int)f.length()];
        BufferedInputStream filReader = new BufferedInputStream(new FileInputStream(f));
        filReader.read(buffer,0,buffer.length);
        output.write(buffer,0,buffer.length);
        output.flush();
        filReader.close();
        output.close();
        System.out.println("sent");
        //close the connection with the client

    }

    public int reciveFile(Socket clientSocket, String filename) throws IOException{
        //get the path name to where the files will be stored on the server
        File f = new File("C:/Users/Andreas/IdeaProjects/Fileserver/src/server/serverFiles/"+filename);
        f.createNewFile();
        //get the stream to the client
        InputStream input = clientSocket.getInputStream();

        byte[] buffer = new byte[1000000];
        //stream to write to the file
        BufferedOutputStream fileWrite = new BufferedOutputStream(new FileOutputStream(f));
        int n;
        //read the input and write it to the file
        while ((n = input.read(buffer, 0, buffer.length)) != -1) {
            fileWrite.write(buffer, 0, n);
        }
        System.out.println("server got file");
        fileWrite.flush();
        fileWrite.close();
        input.close();
        //close connection and return the file length to save in DB
        return (int) f.length();
    }
}
