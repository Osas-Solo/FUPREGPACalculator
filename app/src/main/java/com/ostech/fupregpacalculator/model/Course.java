package com.ostech.fupregpacalculator.model;

import java.io.Serializable;
import java.util.Comparator;

public class Course implements Serializable {
    private String courseCode;
    private int creditUnit;
    private String grade;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getCreditUnit() {
        return creditUnit;
    }

    public void setCreditUnit(int creditUnit) {
        this.creditUnit = creditUnit;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Course(String courseCode, int creditUnit, String grade) {
        setCourseCode(courseCode);
        setCreditUnit(creditUnit);
        setGrade(grade);
    }

    public static Comparator<Course> courseCodeComparator = (firstCourse, secondCourse) -> {
        String firstCourseCode = firstCourse.getCourseCode().toUpperCase();
        String secondCourseCode = secondCourse.getCourseCode().toUpperCase();

        return firstCourseCode.compareTo(secondCourseCode);
    };

}
