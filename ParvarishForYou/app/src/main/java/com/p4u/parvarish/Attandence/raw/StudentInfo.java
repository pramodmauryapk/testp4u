package com.p4u.parvarish.Attandence.raw;

/**
 * Created by eatarsi on 7/8/2017.
 */

public class StudentInfo {

    public String studentname;
    public String classname;
    public String sectionname;

    public StudentInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public StudentInfo(String studentname, String classname, String sectionname) {
        this.studentname = studentname;
        this.classname = classname;
        this.sectionname = sectionname;
    }

}
