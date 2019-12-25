package com.p4u.parvarish.Attandence.admin;

import com.p4u.parvarish.Attandence.student.AttandenceData;

public class StudentData extends AttandenceData {

    private String studentId;
    private String studentName;
    private String studentfathername;
    private String studentdob;
    private String studentmothername;
    private String studenthomemobile;
    private String studentadmyear;
    private String studentaddress;
    private String studentgender;
    private String studentfeepaid;
    private String studentfeepending;
    private String studentimgurl;
    private String studentpin;
    private String studentresultid;
    private String studentrole;
    private String passcode;



    private boolean checked = false;
    public StudentData() {
    }




    public StudentData(String studentId,
                       String studentName,
                       String studentfathername,
                       String studentdob,
                       String studentmothername,
                       String studenthomemobile,
                       String studentadmyear,
                       String studentaddress,
                       String studentgender,
                       String studentfeepaid,
                       String studentfeepending,
                       String studentimgurl,
                       String studentpin,
                       String studentresultid,
                       String studentrole,
                       String passcode,
                       boolean checked){
        this.studentId=studentId;
        this.studentName=studentName;
        this.studentfathername=studentfathername;
        this.studentdob=studentdob;
        this.studentmothername=studentmothername;
        this.studenthomemobile=studenthomemobile;
        this.studentadmyear=studentadmyear;
        this.studentaddress=studentaddress;
        this.studentgender=studentgender;
        this.studentfeepaid=studentfeepaid;
        this.studentfeepending=studentfeepending;
        this.studentimgurl=studentimgurl;
        this.studentpin=studentpin;
        this.studentresultid=studentresultid;
        this.studentrole=studentrole;
        this.passcode=passcode;
        this.checked=checked;

    }
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }


    public String getStudentfathername() {
        return studentfathername;
    }

    public void setStudentfathername(String studentfathername) {
        this.studentfathername = studentfathername;
    }

    public String getStudentdob() {
        return studentdob;
    }

    public void setStudentdob(String studentdob) {
        this.studentdob = studentdob;
    }

    public String getStudentmothername() {
        return studentmothername;
    }

    public void setStudentmothername(String studentmothername) {
        this.studentmothername = studentmothername;
    }

    public String getStudenthomemobile() {
        return studenthomemobile;
    }

    public void setStudenthomemobile(String studenthomemobile) {
        this.studenthomemobile = studenthomemobile;
    }


    public String getStudentadmyear() {
        return studentadmyear;
    }

    public void setStudentadmyear(String studentadmyear) {
        this.studentadmyear = studentadmyear;
    }

    public String getStudentaddress() {
        return studentaddress;
    }

    public void setStudentaddress(String studentaddress) {
        this.studentaddress = studentaddress;
    }

    public String getStudentgender() {
        return studentgender;
    }

    public void setStudentgender(String studentgender) {
        this.studentgender = studentgender;
    }

    public String getStudentfeepaid() {
        return studentfeepaid;
    }

    public void setStudentfeepaid(String studentfeepaid) {
        this.studentfeepaid = studentfeepaid;
    }

    public String getStudentfeepending() {
        return studentfeepending;
    }

    public void setStudentfeepending(String studentfeepending) {
        this.studentfeepending = studentfeepending;
    }

    public String getStudentimgurl() {
        return studentimgurl;
    }

    public void setStudentimgurl(String studentimgurl) {
        this.studentimgurl = studentimgurl;
    }

    public String getStudentpin() {
        return studentpin;
    }

    public void setStudentpin(String studentpin) {
        this.studentpin = studentpin;
    }



    public String getStudentresultid() {
        return studentresultid;
    }

    public void setStudentresultid(String studentresultid) {
        this.studentresultid = studentresultid;
    }
    public String getStudentrole() {
        return studentrole;
    }


    public void setStudentrole(String studentrole) {
        this.studentrole = studentrole;
    }
    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public void toggleChecked()
    {
        checked = !checked;
    }
}
