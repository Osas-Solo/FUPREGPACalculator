package com.ostech.fupregpacalculator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Institution implements Serializable {
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
        return null;
    }

    public double getGradePoint(String grade) {
        return points[Arrays.binarySearch(grades, grade)];
    }
}
