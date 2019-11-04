package com.p4u.parvarish.user_pannel;

import com.google.firebase.database.Exclude;

public class ReqestedUser {


    private String userId;
    private String userName;
    private String key;
    private String userEmail;
    private String userMobile;
    private String userAddress;
    private String userTime;



    public ReqestedUser() {
        //empty constructor needed
    }
    public ReqestedUser(int position){
    }



    public ReqestedUser(String id, String name, String email, String mobilenumber, String address,  String time) {
        this.userId=id;
        this.userName = name;
        this.userEmail = email;
        this.userMobile=mobilenumber;
        this.userAddress=address;
        this.userTime=time;

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

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    void setKey(String key) {
        this.key = key;
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


    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

}
