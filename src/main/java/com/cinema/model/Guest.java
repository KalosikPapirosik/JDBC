package com.cinema.model;

public class Guest {
    int id;
    String document;
    String lastname;
    String firstname;
    String patronymic;

    public Guest(int id, String document, String lastname, String firstname, String patronymic) {
        this.id = id;
        this.document = document;
        this.lastname = lastname;
        this.firstname = firstname;
        this.patronymic = patronymic;
    }

    public Guest(int id, String document, String lastname, String firstname) {
        this.id = id;
        this.document = document;
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int getId() {
        return id;
    }

    public String getDocument() {
        return document;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", document='" + document + '\'' +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                '}';
    }
}
