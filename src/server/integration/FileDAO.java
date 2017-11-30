package server.integration;


import server.model.Account;
import server.model.FileObj;

import java.sql.*;
import java.util.ArrayList;

public class FileDAO {
    private String URL = "jdbc:mysql:///file";
    private String driver = "com.mysql.jdbc.Driver";
    private String userID = "root";

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


    public FileDAO(){
        try{
            Connection con;
            Class.forName(driver);
            con = DriverManager.getConnection(URL, userID, null);
            createStatments(con);
        }catch (Exception e){
            e.printStackTrace();
        }
        //sätt upp connection med db

    }
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
    //metod för att kolla upp om acount finns i DB
    public Account findAccount(String accountName){
        ResultSet rset = null;
        try {
            findAccountStmt.setString(1,accountName);
            rset = findAccountStmt.executeQuery();
            if (rset.next()){
                return new Account(accountName,rset.getString(1),this);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    //metod för att lägga till konto
    public void createAccount(Account account){
        try {
            createAccountStmt.setString(2,account.getName());
            createAccountStmt.setString(1,account.getPassword());
            createAccountStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    //metod för att kolla om filnamn finns i db

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
    public void createFile(FileObj fileObj){
        try {
         createFileStmt.setString(1, fileObj.getName());
         createFileStmt.setInt(2, fileObj.getSize());
         createFileStmt.setString(3, fileObj.getOwner());
         createFileStmt.setBoolean(4, fileObj.getPremission());
         createFileStmt.setBoolean(5, fileObj.getRead());
         createFileStmt.setBoolean(6, fileObj.getWrite());
         createFileStmt.setBoolean(7, fileObj.getTrack());
         createFileStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
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
    public void deleteAccount(String accName){
        try {
           deleteAccountStmt.setString(1,accName);
           deleteAccountStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void deleteFile(String fileName){
        try {
            deleteFileStmt.setString(1,fileName);
            deleteFileStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

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
    public void setTracker(String filname, Boolean setTrack){
        try {
            setTrackerStmt.setBoolean(1,setTrack);

            setTrackerStmt.setString(2,filname);
            setTrackerStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean checkIfTracked(String filname,String owner){
        try {
            checkTrackerSmt.setString(1,filname);
            checkTrackerSmt.setString(2,owner);
            ResultSet rset = checkTrackerSmt.executeQuery();
            if (rset.next()){
                if(rset.getBoolean(1)){
                    return true;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

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

    //metod för att lägga till en fil

    //metod för att hämta lista med filer

    //metod för att kolal tracker

    //metod för att ta bort konto

    //metod för att ta bort fil

    //metod för att logga in jämföra namn och pass

    //metod för att sätta track till true på en fil

    public void createStatments(Connection con){
        try {
            findAccountStmt = con.prepareStatement("SELECT * FROM account WHERE user = ?");
            createAccountStmt = con.prepareStatement("INSERT INTO account VALUES(?,?)");
            findFileStmt = con.prepareStatement("SELECT * FROM file WHERE NAME  = ?");
            deleteAccountStmt = con.prepareStatement("DELETE FROM account WHERE user = ?");
            deleteFileStmt = con.prepareStatement("DELETE FROM file WHERE name = ?");
            createFileStmt = con.prepareStatement("INSERT INTO file VALUES (?,?,?,?,?,?,?)");

            getFileListStmt = con.prepareStatement("SELECT * FROM file WHERE owner = ? OR (private = '0' AND file.read = '1')");

            checkFileAccessStmt = con.prepareStatement("SELECT * FROM file WHERE name = ? AND (owner = ? OR (private = '1' AND file.read = '0'))");

            updateFileStmt = con.prepareStatement("UPDATE file SET size = ?, private = ?, file.read= ?, file.write = ? WHERE name = ?");

            setTrackerStmt = con.prepareStatement("UPDATE file SET track = ? WHERE name = ?");
            checkTrackerSmt = con.prepareStatement("SELECT track FROM file WHERE name = ? AND owner <> ?");

            loginStmt = con.prepareStatement("SELECT * FROM account WHERE user = ? AND password =? ");
            checkIfFileOwner = con.prepareStatement("SELECT owner FROM file WHERE name = ? AND owner = ?");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
