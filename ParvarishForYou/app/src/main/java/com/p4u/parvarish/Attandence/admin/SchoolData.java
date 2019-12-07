package com.p4u.parvarish.Attandence.admin;

public class SchoolData {

    private String schoolId;
    private String schoolName;
    private String schoolAdd;
    private String schoolMedium;
    private String schoolYear;
    private int schoolStudents;
    private int schoolTeachers;
    private String schoolPrinicipleName;
    private String schoolPriniciplePhone;
    private String schoollogo;
    private String schoolRole;
    private String passcode;
    public SchoolData() {
    }


    public SchoolData(String schoolId,
                      String schoolName,
                      String schoolAdd,
                      String schoolMedium,
                      String schoolYear,
                      int schoolStudents,
                      int schoolTeachers,
                      String schoolPrinicipleName,
                      String schoolPriniciplePhone,
                      String schoollogo,
                      String schoolRole,
                      String passcode){
        this.schoolId=schoolId;
        this.schoolName=schoolName;
        this.schoolAdd=schoolAdd;
        this.schoolMedium=schoolMedium;
        this.schoolYear=schoolYear;
        this.schoolStudents=schoolStudents;
        this.schoolTeachers=schoolTeachers;
        this.schoolPrinicipleName=schoolPrinicipleName;
        this.schoolPriniciplePhone=schoolPriniciplePhone;
        this.schoollogo=schoollogo;
        this.schoolRole=schoolRole;
        this.passcode=passcode;

    }
    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolAdd() {
        return schoolAdd;
    }

    public void setSchoolAdd(String schoolAdd) {
        this.schoolAdd = schoolAdd;
    }

    public String getSchoolMedium() {
        return schoolMedium;
    }

    public void setSchoolMedium(String schoolMedium) {
        this.schoolMedium = schoolMedium;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public int getSchoolStudents() {
        return schoolStudents;
    }

    public void setSchoolStudents(int schoolStudents) {
        this.schoolStudents = schoolStudents;
    }

    public int getSchoolTeachers() {
        return schoolTeachers;
    }

    public void setSchoolTeachers(int schoolTeachers) {
        this.schoolTeachers = schoolTeachers;
    }

    public String getSchoolPrinicipleName() {
        return schoolPrinicipleName;
    }

    public void setSchoolPrinicipleName(String schoolPrinicipleName) {
        this.schoolPrinicipleName = schoolPrinicipleName;
    }

    public String getSchoolPriniciplePhone() {
        return schoolPriniciplePhone;
    }

    public void setSchoolPriniciplePhone(String schoolPriniciplePhone) {
        this.schoolPriniciplePhone = schoolPriniciplePhone;
    }

    public String getSchoollogo() {
        return schoollogo;
    }

    public void setSchoollogo(String schoollogo) {
        this.schoollogo = schoollogo;
    }
    public String getSchoolRole() {
        return schoolRole;
    }

    public void setSchoolRole(String schoolRole) {
        this.schoolRole = schoolRole;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }


}
