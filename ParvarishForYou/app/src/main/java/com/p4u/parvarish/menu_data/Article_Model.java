package com.p4u.parvarish.menu_data;

import com.google.firebase.database.Exclude;

public class Article_Model {
    private String id;
    private String title;
    private String imageURL;
    private String key;
    private String description;
    private String time;
    private String status;
    private String userid;

    public Article_Model() {
        //empty constructor needed
    }
    public Article_Model(int position){
    }



    public Article_Model(String id, String title, String imageUrl, String Des, String time, String status, String userid) {
        this.id=id;
        this.title = title;
        this.imageURL = imageUrl;
        this.description = Des;
        this.time=time;
        this.status=status;
        this.userid=userid;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    public String getImageUrl() {
        return imageURL;
    }
    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
