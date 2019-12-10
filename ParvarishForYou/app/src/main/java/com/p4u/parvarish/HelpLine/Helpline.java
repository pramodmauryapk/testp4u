package com.p4u.parvarish.HelpLine;

public class Helpline {
    private String Service;
    private String Mobile;
    public Helpline(){

    }
    public Helpline(String service, String mobile) {
        Service = service;
        Mobile = mobile;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }
}
