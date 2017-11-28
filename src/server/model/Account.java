package server.model;

import server.integration.AccountDAO;

public class Account{
    private String name;
    private String password;
    private transient AccountDAO accountDB;

    public Account(String name, String password, AccountDAO accountDB){
        this.name = name;
        this.password = password;
        this.accountDB = accountDB
    }

    public String getName(){
        return name;
    }
}
