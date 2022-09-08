package com.dk.web.session.entity;


import java.io.Serializable;

/**
 * @author dkay
 * @version 1.0
 */
public class Account implements Serializable {
    private String account;
    private String pwd;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "Account{" +
                "account='" + account + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
