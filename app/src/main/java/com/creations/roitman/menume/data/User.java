package com.creations.roitman.menume.data;

/**
 * Represents the user of the app.
 */
public class User {

    private String username;
    private String password;
    private String mail;


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

    /**
     * Gets the mail address of the user.
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * Gets the password of the user.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the username of the user.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the mail of the user.
     * @param mail address to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Sets the password of the user.
     * @param password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the username of the user.
     * @param username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
