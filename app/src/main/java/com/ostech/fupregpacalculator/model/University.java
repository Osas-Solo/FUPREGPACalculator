package com.ostech.fupregpacalculator.model;

import java.util.ArrayList;

public class University extends Institution {

    public University() {
        getLevels().add(new Level("100 Level"));
        getLevels().add(new Level("200 Level"));
        getLevels().add(new Level("300 Level"));
        getLevels().add(new Level("400 Level"));
        getLevels().add(new Level("500 Level"));
        getLevels().add(new Level("600 Level"));
        getLevels().add(new Level("700 Level"));
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
}
