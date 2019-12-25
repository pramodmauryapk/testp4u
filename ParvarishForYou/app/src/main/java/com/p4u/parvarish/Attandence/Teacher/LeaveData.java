package com.p4u.parvarish.Attandence.Teacher;

public class LeaveData {
    private String LeaveName;
    private String Date;
    public LeaveData(){

    }
    public LeaveData(String leaveName, String date) {
        LeaveName = leaveName;
        Date = date;
    }
    public String getLeaveName() {
        return LeaveName;
    }

    public void setLeaveName(String leaveName) {
        LeaveName = leaveName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

}
