package com.example.edpestcontrolservices;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String emailAddress;
    private String password;

    public Note() {
        //public no-arg constructor needed by Firebase
    }

    public Note(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public String getPassword() {
        return password;
    }
}
