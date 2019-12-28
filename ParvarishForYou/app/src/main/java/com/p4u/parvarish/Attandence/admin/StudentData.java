package com.p4u.parvarish.Attandence.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.p4u.parvarish.Attandence.student.AttandenceData;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class StudentData extends AttandenceData implements List<String> {

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

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<String> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {
        return null;
    }

    @Override
    public boolean add(String s) {
        return false;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends String> collection) {
        return false;
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends String> collection) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public String get(int i) {
        return null;
    }

    @Override
    public String set(int i, String s) {
        return null;
    }

    @Override
    public void add(int i, String s) {

    }

    @Override
    public String remove(int i) {
        return null;
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return 0;
    }

    @NonNull
    @Override
    public ListIterator<String> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<String> listIterator(int i) {
        return null;
    }

    @NonNull
    @Override
    public List<String> subList(int i, int i1) {
        return null;
    }
}
