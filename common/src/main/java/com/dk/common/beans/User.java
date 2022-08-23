package com.dk.common.beans;

/**
 * @author dkay
 * @version 1.0
 */
public class User extends Person {
    private UserAccount account;

    public UserAccount getAccount() {
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "User{" +
                "account=" + account +
                "} " + super.toString();
    }
}
