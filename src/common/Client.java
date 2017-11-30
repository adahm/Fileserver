package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote{
    //interface to print info when file that is track is written/read/delted
    void getTrackerInfo(String msg)throws RemoteException;
}
