package com.p4u.parvarish.Collection;

public class Collection_data {
    private String Id;
    private String Name;
    private String Mobile;
    private String Url;



    private String Address;

    Collection_data(String id, String name, String mobile, String url, String address) {
        Id=id;
        Name = name;
        Mobile = mobile;
        Url=url;
        Address=address;
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

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }


    public Collection_data(){

    }

}
