package com.p4u.parvarish.marquee;

public class News {
    private String newsId;
    private String marqueeText;

    News() {

    }



    News(String newsId, String marqueeText) {
        this.newsId = newsId;
        this.marqueeText = marqueeText;

    }
    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    String getMarqueeText() {
        return marqueeText;
    }

    void setMarqueeText(String marqueeText) {
        this.marqueeText = marqueeText;
    }
}