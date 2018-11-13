package com.test.prototype.lab06.storage;


public class Contact {

    private String firstName;
    private String lastName;
    private String phone;

    public Contact(String fName, String lName, String phoneNum) {
        firstName = fName;
        lastName = lName;
        phone = phoneNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toString() {
        return firstName + " " + lastName + " - " + phone;
    }
}
