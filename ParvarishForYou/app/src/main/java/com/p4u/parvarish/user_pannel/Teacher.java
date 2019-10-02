package com.p4u.parvarish.user_pannel;

import com.google.firebase.database.Exclude;

public class Teacher {


    private String userId;
    private String userName;
    private String key;
    private String userEmail;
    private int position;
    private String userPassword;
    private String userRole;
    private String userMobile;
    private String userAddress;
    private String userIdentity;
    private String userStatus;
    private String imageURL;


    public Teacher() {
        //empty constructor needed
    }
    public Teacher (int position){
        this.position = position;
    }



    public Teacher(String id, String name, String email,String password, String role, String mobilenumber, String address, String identity, String status, String imageUrl) {
        this.userId=id;
        this.userName = name;
        this.userEmail = email;
        this.userPassword=password;
        this.userRole=role;
        this.userMobile=mobilenumber;
        this.userAddress=address;
        this.userIdentity=identity;
        this.userStatus=status;
        this.imageURL = imageUrl;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getUserRole() {
        return userRole;
    }
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserMobile() {
        return userMobile;
    }
    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserAddress() {
        return userAddress;
    }
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserIdentity() {
        return userIdentity;
    }
    public void setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
