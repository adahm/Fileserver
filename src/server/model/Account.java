package server.model;

import server.integration.FileDAO;

public class Account{
    private String name;
    private String password;
    private transient FileDAO fileDAO;

    public Account(String name, String password, FileDAO fileDAO){
        this.name = name;
        this.password = password;
        this.fileDAO = fileDAO;
    }

    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
}
