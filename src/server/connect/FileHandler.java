package server.connect;

//fixa metod för att skicka en fil

//fixa metod för att ta emot en fil
//anropas av controller.
//i rmi callet kolla om filen finnsinna skickar

//om det är uppdate kolla permission för write om den är public
//sen anropas recive och skickar en bollean om attt klienten kan skcika filen

//om klient anropa retirive kolla permision först sen om den är read
//anropa sen send metoden här med filnamnet

//

import java.io.*;
import java.net.Socket;

public class FileHandler {

    public void senFile(Socket clientSocket, String filename) throws IOException{
        OutputStream output = clientSocket.getOutputStream();
        File f = new File("C:/Users/Andreas/IdeaProjects/Fileserver/serverFiles/"+filename);
        System.out.println("sending");
        byte[] buffer = new byte[(int)f.length()];
        BufferedInputStream filReader = new BufferedInputStream(new FileInputStream(f));
        filReader.read(buffer,0,buffer.length);
        output.write(buffer,0,buffer.length);
        output.flush();
        filReader.close();
        output.close();
        System.out.println("sent");

    }

    public int reciveFile(Socket clientSocket, String filename) throws IOException{
        //path needs to be changed where fiels are stored on the server
        File f = new File("C:/Users/Andreas/IdeaProjects/Fileserver/serverFiles/"+filename);
        f.createNewFile();
        InputStream input = clientSocket.getInputStream();
        byte[] buffer = new byte[1000000];
        BufferedOutputStream fileWrite = new BufferedOutputStream(new FileOutputStream(f));
        int n;
        while ((n = input.read(buffer, 0, buffer.length)) != -1) {
            fileWrite.write(buffer, 0, n);
        }
        System.out.println("server got file");
        fileWrite.flush();
        fileWrite.close();
        input.close();
        return (int) f.length();
    }
}
