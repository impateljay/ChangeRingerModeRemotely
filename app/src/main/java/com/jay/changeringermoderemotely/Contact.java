package com.jay.changeringermoderemotely;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by jaypatel on 03/11/17
 */

@Table(name = "Contact")
public class Contact extends Model {
    @Column(name = "name")
    private String name;
    @Column(name = "phoneNumber")
    private String phoneNumber;

    public Contact() {
    }

    Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    static List<Contact> getAllContacts() {
        return new Select().from(Contact.class).execute();
    }

    static boolean checkPhoneNumberExists(String phoneNumber) {
        return new Select().from(Contact.class).where("phoneNumber = ?", phoneNumber).executeSingle() != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }
}
