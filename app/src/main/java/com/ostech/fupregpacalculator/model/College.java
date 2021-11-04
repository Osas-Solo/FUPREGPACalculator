package com.ostech.fupregpacalculator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class College implements Serializable {
    public final double MAXIMUM_GPA = 5.0;

    private ArrayList<Level> levels = new ArrayList<>();

    private String[] grades = {"A", "B", "C", "D", "E", "F"};
    private double[] points = {5, 4, 3, 2, 1, 0};

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public String[] getGrades() {
        return grades;
    }

    public double[] getPoints() {
        return points;
    }

    public String getRemark(double gradePointAverage) {
        String remark = "";

        if (gradePointAverage >= 4.50) {
            remark = "First Class";
        } else if (gradePointAverage >= 3.50 && gradePointAverage <= 4.49){
            remark = "Second Class Upper Division";
        } else if (gradePointAverage >= 2.40 && gradePointAverage <= 3.49){
            remark = "Second Class Lower Division";
        } else if (gradePointAverage >= 1.50 && gradePointAverage <= 2.39){
            remark = "Third Class";
        } else {
            remark = "Probation";
        }

        return remark;
    }

    public double getGradePoint(String grade) {
        return points[Arrays.binarySearch(grades, grade)];
    }
}
