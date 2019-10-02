package com.p4u.parvarish.galary;

import com.google.firebase.database.Exclude;

public class Image_Model {
    private String name;
    private String imageURL;
    private String key;
    private String description;

    public Image_Model() {
        //empty constructor needed
    }
    public Image_Model(int position){
    }
    Image_Model(String name, String imageUrl, String Des) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        this.imageURL = imageUrl;
        this.description = Des;
    }
    String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return imageURL;
    }
    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }
    @Exclude
    String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
