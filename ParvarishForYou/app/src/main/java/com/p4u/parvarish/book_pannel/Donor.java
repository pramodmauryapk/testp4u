package com.p4u.parvarish.book_pannel;

public class Donor {

    private String bookDonor;
    private String bookDonorMobile;

    public Donor() {
    }




    public Donor(String bookDonor,String bookDonorMobile){
        this.bookDonor=bookDonor;
        this.bookDonorMobile=bookDonorMobile;
    }



    public String getBookDonor() {
        return bookDonor;
    }
    public void setBookDonor(String bookDonor) {
        this.bookDonor = bookDonor;
    }
    public String getBookDonorMobile() {
        return bookDonorMobile;
    }
    public void setBookDonorMobile(String bookDonorMobile) {        this.bookDonorMobile = bookDonorMobile;    }


}
