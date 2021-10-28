package com.ostech.fupregpacalculator.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AcademicRecord implements Serializable {
    private static final AcademicRecord academicRecord = new AcademicRecord();

    private Institution institutionType;
    private ArrayList<Semester> semesterList = new ArrayList<>();
    private double cumulativeGradePointAverage;

    public Institution getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(Institution institutionType) {
        this.institutionType = institutionType;

        for (Semester currentSemester: getSemesterList()) {
            currentSemester.setInstitution(institutionType);
        }
    }

    public ArrayList<Semester> getSemesterList() {
        return semesterList;
    }

    public void setSemesterList(ArrayList<Semester> semesterList) {
        this.semesterList = semesterList;
    }

    public double getCumulativeGradePointAverage() {
        calculateCGPA();

        return (double) (Math.round(cumulativeGradePointAverage * 100)) / 100;
    }

    public static AcademicRecord getInstance() {
        return academicRecord;
    }

    private AcademicRecord() { }

    public int getNumberOfSemesters() {
        return semesterList.size();
    }

    public void calculateCGPA() {
        for (Semester currentSemester: semesterList) {
            currentSemester.calculateSemesterGPA();
        }

        cumulativeGradePointAverage = (getTotalCreditUnit() != 0) ?
                (getTotalGradePoint() / getTotalCreditUnit()) : 0;
    }

    public double getTotalCreditUnit() {
        double totalCreditUnit = 0;

        for (Semester currentSemester: semesterList) {
            totalCreditUnit += currentSemester.getTotalCreditUnit();
        }

        return totalCreditUnit;
    }

    public double getTotalGradePoint() {
        double totalGradePoint = 0;

        for (Semester currentSemester: semesterList) {
            totalGradePoint += currentSemester.getTotalGradePoint();
        }

        return totalGradePoint;
    }

    public int getNumberOfCourses() {
        int totalCourseNumber = 0;

        for (Semester currentSemester: semesterList) {
            totalCourseNumber += currentSemester.getNumberOfCourses();
        }

        return totalCourseNumber;
    }

    public void sortCoursesInSemesters() {
        for (Semester currentSemester: semesterList) {
            Collections.sort(currentSemester.getCourseList(), Course.courseCodeComparator);
        }
    }
}
