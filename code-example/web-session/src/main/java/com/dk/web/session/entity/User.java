package com.dk.web.session.entity;


import java.io.Serializable;

/**
 * @author dkay
 * @version 1.0
 */
public class User implements Serializable {
    private String username;
    private Integer age;
    private Account account;

    public static User defaultUser() {
        User user = new User();
        Account account = new Account();
        account.setAccount("dk");
        account.setPwd("123456");
        user.setAccount(account);
        user.setUsername("kai");
        user.setAge(25);
        return user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age=" + age +
                ", account=" + account +
                '}';
    }
}
