package com.example.anushajasti.ase;

public class User {
    String firstname, lastname, emailid, password, phone;

    public User(String firstname, String lastname, String emailid, String password, String phone) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailid = emailid;
        this.password = password;
        this.phone = phone;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
