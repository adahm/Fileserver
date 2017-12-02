package server.integration;


import server.model.Account;
import server.model.FileObj;

import java.sql.*;
import java.util.ArrayList;

public class FileDAO {
    private String URL = "jdbc:mysql:///file"; //url to the mysql db
    private String driver = "com.mysql.jdbc.Driver"; //driver for the Mysql DB
    private String userID = "root"; //user id to the DB

    private PreparedStatement findAccountStmt;
    private PreparedStatement createAccountStmt;
    private PreparedStatement deleteAccountStmt;
    private PreparedStatement loginStmt;

    private PreparedStatement findFileStmt;
    private PreparedStatement createFileStmt;
    private PreparedStatement deleteFileStmt;
    private PreparedStatement getFileListStmt;
    private PreparedStatement updateFileStmt;
    private PreparedStatement checkFileAccessStmt;
    private PreparedStatement setTrackerStmt;
    private PreparedStatement checkTrackerSmt;
    private PreparedStatement checkIfFileOwner;
    private PreparedStatement checkWriteFileAccessStmt;

    //set up a connection the mysql DB
    public FileDAO(){
        try{
            Connection con;
            Class.forName(driver);
            con = DriverManager.getConnection(URL, userID, null);
            createStatments(con);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //return true if username and password pair is in db otherwise false
    public boolean tryLogin(String username, String password){
        ResultSet rset = null;
        try {
            loginStmt.setString(1,username);
            loginStmt.setString(2,password);
            rset = loginStmt.executeQuery();
            if (rset.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    //return acunt if accountName is in Db otherwise null
    public Account findAccount(String accountName){
        ResultSet rset = null;
        try {
            findAccountStmt.setString(1,accountName);
            rset = findAccountStmt.executeQuery();
            if (rset.next()){
                return new Account(accountName,rset.getString(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //inserts a row in the acount table
    public void createAccount(Account account){
        try {
            createAccountStmt.setString(2,account.getName());
            createAccountStmt.setString(1,account.getPassword());
            createAccountStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //returns a file if it exist in the DB
    public FileObj findFile(String filename){
        ResultSet rset = null;
        try {
            findFileStmt.setString(1,filename);
            rset = findFileStmt.executeQuery();
            if (rset.next()){
                return new FileObj(filename,rset.getInt(2),rset.getString(3),rset.getBoolean(4),rset.getBoolean(5),rset.getBoolean(6),rset.getBoolean(7));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //create an entry in the File table
    public void createFile(FileObj fileObj){
        try {
         createFileStmt.setString(1, fileObj.getName());
         createFileStmt.setInt(2, fileObj.getSize());
         createFileStmt.setString(3, fileObj.getOwner());
         createFileStmt.setBoolean(4, fileObj.getPrivateFile());
         createFileStmt.setBoolean(5, fileObj.getRead());
         createFileStmt.setBoolean(6, fileObj.getWrite());
         createFileStmt.setBoolean(7, fileObj.getTrack());
         createFileStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //check if the user is allowed to read the file
    public Boolean checkAcess(String userName, String filename){
        ResultSet rset = null;
        try {
            checkFileAccessStmt.setString(1,filename);
            checkFileAccessStmt.setString(2,userName);
            rset = checkFileAccessStmt.executeQuery();
            if (rset.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    //check if the user is allowed to write to the file
    public boolean checkWritePrem(String userName,String filename){
        ResultSet rset = null;
        try {
            checkWriteFileAccessStmt.setString(1,filename);
            checkWriteFileAccessStmt.setString(2,userName);
            rset = checkWriteFileAccessStmt.executeQuery();
            if (rset.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    //deletes the acount entry in the acount table
    public void deleteAccount(String accName){
        try {
           deleteAccountStmt.setString(1,accName);
           deleteAccountStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //deletes an file entry in the file table
    public void deleteFile(String fileName){
        try {
            deleteFileStmt.setString(1,fileName);
            deleteFileStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //returns a list of strings that are the entries that the user is allowed to see in the file table
    public ArrayList<String> getFiles(String RequestName){
        ArrayList<String> fileObjs = new ArrayList<>();
        try {
            getFileListStmt.setString(1,RequestName);
            ResultSet rset = getFileListStmt.executeQuery();
            while (rset.next()){
                fileObjs.add(new FileObj(rset.getString(1),rset.getInt(2),rset.getString(3),rset.getBoolean(4),rset.getBoolean(5),rset.getBoolean(6),rset.getBoolean(7)).toString());
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return fileObjs;
    }

    //update a file entry in the file table
    public void updateFile(String filname,int size, Boolean setPrivate, Boolean setRead, Boolean setWrite){
        try {
            updateFileStmt.setInt(1,size);
            updateFileStmt.setBoolean(2,setPrivate);
            updateFileStmt.setBoolean(3,setRead);
            updateFileStmt.setBoolean(4,setWrite);
            updateFileStmt.setString(5,filname);
            updateFileStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //set the tracker colum in a file entry to true
    public void setTracker(String filname, Boolean setTrack){
        try {
            setTrackerStmt.setBoolean(1,setTrack);

            setTrackerStmt.setString(2,filname);
            setTrackerStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //return the owner if the track paramter is set to true and the user requesting is not the owner,
    public String checkIfTracked(String filname,String owner){
        try {
            checkTrackerSmt.setString(1,filname);
            checkTrackerSmt.setString(2,owner);
            ResultSet rset = checkTrackerSmt.executeQuery();
            if (rset.next()){
                if(rset.getBoolean(1)){
                    return rset.getString(2);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    //return true if the user is the owner of the file
    public boolean checkIfFileOwner(String acountName, String filename){
        try {
            checkIfFileOwner.setString(1,filename);
            checkIfFileOwner.setString(2,acountName);
            ResultSet rset = checkIfFileOwner.executeQuery();
            if (rset.next()){
               return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public void createStatments(Connection con){
        try {
            //get the rows where the username is asked for
            findAccountStmt = con.prepareStatement("SELECT * FROM account WHERE user = ?");

            //create row in the account table
            createAccountStmt = con.prepareStatement("INSERT INTO account VALUES(?,?)");
            //find a file with a given name
            findFileStmt = con.prepareStatement("SELECT * FROM file WHERE NAME  = ?");
            //delete an account with a given name
            deleteAccountStmt = con.prepareStatement("DELETE FROM account WHERE user = ?");
            //deltete a file with a given name
            deleteFileStmt = con.prepareStatement("DELETE FROM file WHERE name = ?");
            //inseret a row in the file table
            createFileStmt = con.prepareStatement("INSERT INTO file VALUES (?,?,?,?,?,?,?)");
            //get the entries in the file table with a given owner or where they are set to not private and read to true
            getFileListStmt = con.prepareStatement("SELECT * FROM file WHERE owner = ? OR (private = '0' AND file.read = '1')");
            //get entry for a file with the supliedd filename and where the owner is correct or the file is set to public and to read acess to true
            checkFileAccessStmt = con.prepareStatement("SELECT * FROM file WHERE name = ? AND (owner = ? OR (private = '0' AND file.read = '1'))");
            //get enrty frin fuke table with given filename and owner name or set to private and allowed to write
            checkWriteFileAccessStmt = con.prepareStatement("SELECT * FROM file WHERE name = ? AND (owner = ? OR (private = '0' AND file.write = '1'))");
            //uppdate a file with the given name
            updateFileStmt = con.prepareStatement("UPDATE file SET size = ?, private = ?, file.read= ?, file.write = ? WHERE name = ?");
            //set track to true in a given file
            setTrackerStmt = con.prepareStatement("UPDATE file SET track = ? WHERE name = ?");
            //check if the tracker is true in a given file and where the one that is checking is not the owner so we should send a message
            checkTrackerSmt = con.prepareStatement("SELECT track,owner FROM file WHERE name = ? AND owner <> ?");
            //return a row if the given username and password pair exist in the table
            loginStmt = con.prepareStatement("SELECT * FROM account WHERE user = ? AND password =? ");
            //return the owner of a given file if the requester is the owner
            checkIfFileOwner = con.prepareStatement("SELECT owner FROM file WHERE name = ? AND owner = ?");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
