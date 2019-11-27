package com.p4u.parvarish.video;

public class YoutubeVideo_Model {
    private String name;
    private String videoURL;
    private String key;


    public YoutubeVideo_Model() {
        //empty constructor needed
    }

    public YoutubeVideo_Model(String name, String videoUrl,String key) {

        this.name = name;
        this.videoURL = videoUrl;
        this.key=key;

    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getVideoURL() {
        return videoURL;
    }
    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
