package com.ostech.fupregpacalculator.model;

import java.util.Arrays;

public class Polytechnic extends Institution{
    public final double MAXIMUM_GPA = 4.0;

    private String[] grades = {"A", "AB", "B", "BC", "C", "CD", "D", "E", "F"};
    private double[] points = {4, 3.5, 3.25, 3, 2.75, 2.5, 2.25, 2, 0};

    public Polytechnic() {
        getLevels().add(new Level("ND 1"));
        getLevels().add(new Level("ND 2"));
        getLevels().add(new Level("HND 1"));
        getLevels().add(new Level("HND 2"));
    }

    public String getRemark(double gradePointAverage) {
        String remark = "";

        if (gradePointAverage >= 3.50) {
            remark = "First Class";
        } else if (gradePointAverage >= 3.00 && gradePointAverage <= 3.49){
            remark = "Second Class Upper Division";
        } else if (gradePointAverage >= 2.00 && gradePointAverage <= 2.99){
            remark = "Second Class Lower Division";
        } else if (gradePointAverage >= 1.00 && gradePointAverage <= 1.99){
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
