package com.p4u.parvarish.Attandence.student;

public class NoticeData {
    private String noticeid;
    private String noticedescription;
    public NoticeData(){

    }
    public NoticeData(String noticeid, String noticedescription) {
        this.noticeid=noticeid;
        this.noticedescription=noticedescription;
    }
    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    public String getNoticedescription() {
        return noticedescription;
    }

    public void setNoticedescription(String noticedescription) {
        this.noticedescription = noticedescription;
    }

}
