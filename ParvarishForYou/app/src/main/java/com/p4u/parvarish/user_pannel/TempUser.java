package com.p4u.parvarish.user_pannel;

import com.google.firebase.database.Exclude;

public class TempUser {


    private String Id;
    private String Name;
    private String Email;
    private String Mobile;
    private String Address;
    private String Identity;
    private String BookHaving;
    private String BookDeposit;

    public TempUser() {
        //empty constructor needed
    }
    public TempUser(int position){
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getIdentity() {
        return Identity;
    }

    public void setIdentity(String identity) {
        Identity = identity;
    }

    public String getBookHaving() {
        return BookHaving;
    }

    public void setBookHaving(String bookHaving) {
        BookHaving = bookHaving;
    }

    public String getBookDeposit() {
        return BookDeposit;
    }

    public void setBookDeposit(String bookDeposit) {
        BookDeposit = bookDeposit;
    }

    public TempUser(String id, String name, String email, String mobilenumber, String address, String identity, String bookHaving, String bookDeposit) {
        this.Id=id;
        this.Name = name;
        this.Email = email;
        this.Mobile=mobilenumber;
        this.Address=address;
        this.Identity=identity;
        this.BookHaving=bookHaving;
        this.BookDeposit=bookDeposit;

    }

}
