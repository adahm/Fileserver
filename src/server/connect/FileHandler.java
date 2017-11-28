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

import java.net.Socket;

public class FileHandler extends Thread{

    public FileHandler(Socket socket){

    }
}
