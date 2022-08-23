package com.dk.common.beans;

/**
 * @author dkay
 * @version 1.0
 */
public class UserAccount {
    private String account;
    private String passwd;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "account='" + account + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
