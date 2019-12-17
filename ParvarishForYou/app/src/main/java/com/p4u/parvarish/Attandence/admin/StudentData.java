package com.p4u.parvarish.Attandence.admin;

public class StudentData {

    private String studentId;
    private String studentName;
    private String studentadmissionno;
    private String studentfathername;
    private String studentdob;
    private String studentmothername;
    private String studenthomemobile;
    private String studentclass;
    private String studentsection;
    private String studentadmyear;
    private String studentaddress;
    private String studentgender;
    private String studentfeepaid;
    private String studentfeepending;
    private String studentimgurl;
    private String studentpin;

    private String studentresultid;
    private String studentrole;
    private String studentschool;
    private String passcode;



    private boolean checked = false;
    public StudentData() {
    }




    public StudentData(String studentId,
                       String studentName,
                       String studentadmissionno,
                       String studentfathername,
                       String studentdob,
                       String studentmothername,
                       String studenthomemobile,
                       String studentclass,
                       String studentsection,
                       String studentadmyear,
                       String studentaddress,
                       String studentgender,
                       String studentfeepaid,
                       String studentfeepending,
                       String studentimgurl,
                       String studentpin,

                       String studentresultid,
                       String studentrole,
                       String studentschool,
                       String passcode,
                       boolean checked){
        this.studentId=studentId;
        this.studentName=studentName;
        this.studentadmissionno=studentadmissionno;
        this.studentfathername=studentfathername;
        this.studentdob=studentdob;
        this.studentmothername=studentmothername;
        this.studenthomemobile=studenthomemobile;
        this.studentclass=studentclass;
        this.studentsection=studentsection;
        this.studentadmyear=studentadmyear;
        this.studentaddress=studentaddress;
        this.studentgender=studentgender;
        this.studentfeepaid=studentfeepaid;
        this.studentfeepending=studentfeepending;
        this.studentimgurl=studentimgurl;
        this.studentpin=studentpin;

        this.studentresultid=studentresultid;
        this.studentrole=studentrole;
        this.studentschool=studentschool;
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

    public String getStudentadmissionno() {
        return studentadmissionno;
    }

    public void setStudentadmissionno(String studentadmissionno) {
        this.studentadmissionno = studentadmissionno;
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

    public String getStudentclass() {
        return studentclass;
    }

    public void setStudentclass(String studentclass) {
        this.studentclass = studentclass;
    }

    public String getStudentsection() {
        return studentsection;
    }

    public void setStudentsection(String studentsection) {
        this.studentsection = studentsection;
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
    public String getStudentschool() {
        return studentschool;
    }

    public void setStudentschool(String studentschool) {
        this.studentschool = studentschool;
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
