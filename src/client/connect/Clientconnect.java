package client.connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;
//fixa en sendFIle metod

//fixa en reciveFile metod


public class Clientconnect {
    private Socket client;

    private BufferedReader input;
    private PrintWriter output;

    public void connect(String host, int port, OutObserver out) throws IOException {
        client = new Socket("localhost", port);
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output = new PrintWriter(client.getOutputStream(), true);
        ForkJoinPool.commonPool().execute(() -> readInput(out));
    }
    FileInputStream fis;
    BufferedInputStream bis;
    BufferedOutputStream out;
    byte[] buffer = new byte[8192];
        try {
        fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);
        out = new BufferedOutputStream(socket.getOutputStream());
        int count;
        while ((count = bis.read(buffer)) > 0) {
            out.write(buffer, 0, count);

        }

    public void quit() throws IOException{
        client.close();
    }

    public void sendGuess(String guess){
        output.println(guess);
    }

    public void start(){
        output.println("START");
    }

    //using the observer pattern shown on the coursewebb
    public void readInput(OutObserver out){
        String in;
        try {
            while ((in = input.readLine()) != null) {
                out.getServerInput(in);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
