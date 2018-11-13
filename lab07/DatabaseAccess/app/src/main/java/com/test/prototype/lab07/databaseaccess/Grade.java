package com.test.prototype.lab07.databaseaccess;

public class Grade {

    private int studentId;
    private String courseComponent;
    private float mark;

    public Grade(int sid, String courseComponent, float mark) {
        this.studentId = sid;
        this.courseComponent = courseComponent;
        this.mark = mark;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCourseComponent() {
        return courseComponent;
    }

    public void setCourseComponent(String courseComponent) {
        this.courseComponent = courseComponent;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public String toString() {
        return studentId + "'s " + courseComponent + ": " + mark;
    }
}
