package com.p4u.parvarish.Attandence.Teacher;

/**
 * Created by eatarsi on 7/1/2017.
 */

public class StudentdataModel {

    String name,classname,section,subject;

    public StudentdataModel(String name){
        this.name=name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getName(){

        return name;
    }

    public void setClassname(String classname){
        this.classname=classname;
    }

    public String getClassname(){
        return classname;
    }

    public void setSection(String section) {

        this.section = section;
    }
    public String getSection() {

        return section;
    }

    public void setSubject(String subject) {

        this.subject = subject;
    }
    public String getSubject() {

        return subject;
    }




}
