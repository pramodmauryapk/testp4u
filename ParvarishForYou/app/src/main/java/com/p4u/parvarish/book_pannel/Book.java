package com.p4u.parvarish.book_pannel;

public class Book {

    private String bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookSubject;
    private String bookYear;
    private String bookCost;
    private String bookAvaibility;
    private String bookLocation;
    private String bookDonor;
    private String bookDonorMobile;
    private String bookDonorTime;
    private String bookHandoverTo;
    private String bookHandoverTime;

    public Book() {
    }




    public Book(String bookId,
                String bookTitle,
                String bookAuthor,
                String bookSubject,
                String bookYear,
                String bookCost,
                String bookAvaibility,
                String bookLocation,
                String bookDonor,
                String bookDonorMobile,
                String bookDonorTime,
                String bookHandoverTo,
                String bookHandoverTime){
        this.bookId=bookId;
        this.bookTitle=bookTitle;
        this.bookAuthor=bookAuthor;
        this.bookSubject=bookSubject;
        this.bookYear=bookYear;
        this.bookCost=bookCost;
        this.bookAvaibility=bookAvaibility;
        this.bookLocation=bookLocation;
        this.bookDonor=bookDonor;
        this.bookDonorMobile=bookDonorMobile;
        this.bookDonorTime=bookDonorTime;
        this.bookHandoverTo=bookHandoverTo;
        this.bookHandoverTime=bookHandoverTime;
    }

    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public String getBookAuthor() {
        return bookAuthor;
    }
    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
    public String getBookSubject() {
        return bookSubject;
    }
    public void setBookSubject(String bookSubject) {
        this.bookSubject = bookSubject;
    }
    public String getBookYear() {
        return bookYear;
    }
    public void setBookYear(String bookYear) {
        this.bookYear = bookYear;
    }
    public String getBookCost() {
        return bookCost;
    }
    public void setBookCost(String bookCost) {
        this.bookCost = bookCost;
    }
    public String getBookAvaibility() {
        return bookAvaibility;
    }
    public void setBookAvaibility(String bookAvaibility) {
        this.bookAvaibility = bookAvaibility;
    }
    public String getBookLocation() {      return bookLocation;    }
    public void setBookLocation(String bookLocation) {        this.bookLocation = bookLocation;    }
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
    public String getBookDonorTime() {
        return bookDonorTime;
    }
    public void setBookDonorTime(String bookDonorTime) {
        this.bookDonorTime = bookDonorTime;
    }
    public String getBookHandoverTo() {
        return bookHandoverTo;
    }
    public void setBookHandoverTo(String bookHandoverTo) {
        this.bookHandoverTo = bookHandoverTo;
    }
    public String getBookHandoverTime() {
        return bookHandoverTime;
    }
    public void setBookHandoverTime(String bookHandoverTime) {        this.bookHandoverTime = bookHandoverTime;    }

}
