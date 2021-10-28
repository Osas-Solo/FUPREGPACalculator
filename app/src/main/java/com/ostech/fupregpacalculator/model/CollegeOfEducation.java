package com.ostech.fupregpacalculator.model;

public class CollegeOfEducation extends Institution {
    public CollegeOfEducation() {
        getLevels().add(new Level("Year 1"));
        getLevels().add(new Level("Year 2"));
        getLevels().add(new Level("Year 3"));
    }

    public String getRemark(double gradePointAverage) {
        String remark = "";

        if (gradePointAverage >= 4.50) {
            remark = "Distinction";
        } else if (gradePointAverage >= 3.50 && gradePointAverage <= 4.49){
            remark = "Credit";
        } else if (gradePointAverage >= 2.40 && gradePointAverage <= 3.49){
            remark = "Merit";
        } else if (gradePointAverage >= 1.50 && gradePointAverage <= 2.39){
            remark = "Pass";
        } else {
            remark = "Probation";
        }

        return remark;
    }
}
