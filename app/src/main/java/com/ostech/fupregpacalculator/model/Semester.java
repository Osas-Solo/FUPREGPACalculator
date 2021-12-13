package com.ostech.fupregpacalculator.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Semester implements Serializable {
    private College college;

    private String semesterName;
    private ArrayList<Course> courseList;
    private double semesterGPA;

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    public void setCourseList(int numberOfCourses) {
        this.courseList = new ArrayList<>(numberOfCourses);

        for (int i = 1; i <= numberOfCourses; i++) {
            this.courseList.add(new Course("", 0, "A"));
        }
    }

    public double getSemesterGPA() {
        calculateSemesterGPA();

        return (double) (Math.round(semesterGPA * 100)) / 100;
    }

    public Semester(String semesterName) {
        setSemesterName(semesterName);
        courseList = new ArrayList<>();
    }

    public void calculateSemesterGPA() {
        semesterGPA = (getTotalCreditUnit() != 0) ? (getTotalGradePoint() / getTotalCreditUnit())
                : 0;
    }

    public double getTotalCreditUnit() {
        double totalCreditUnit = 0;

        for (Course currentCourse: courseList) {
            totalCreditUnit += currentCourse.getCreditUnit();
        }

        return totalCreditUnit;
    }

    public double getTotalGradePoint() {
        double totalGradePoint = 0;

        for (Course currentCourse: courseList) {
            totalGradePoint += currentCourse.getCreditUnit() *
                    college.getGradePoint(currentCourse.getGrade());
        }

        return totalGradePoint;
    }

    public int getNumberOfCourses() {
        return courseList.size();
    }

    public void addCourses(int numberOfCourses) {
        for (int i = 1; i <= numberOfCourses; i++) {
            this.courseList.add(new Course("", 0, "A"));
        }
    }
}
