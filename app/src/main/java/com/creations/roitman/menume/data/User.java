package com.creations.roitman.menume.data;

public class User {

    String username;
    String password;
    String mail;


    /**
     * Public constructor for sign up.
     * @param username of the user
     * @param password of the user
     * @param mail of the user
     */
    public User(String username, String password, String mail) {
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    /**
     * Public constructor for login.
     * @param password of the user
     * @param mail of the user
     */

    public User(String password, String mail) {
        this.password = password;
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
