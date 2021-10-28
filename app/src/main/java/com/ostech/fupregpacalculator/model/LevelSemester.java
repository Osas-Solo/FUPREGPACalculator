package com.ostech.fupregpacalculator.model;

import java.io.Serializable;

public class LevelSemester implements Serializable {
    private String semesterName;
    private boolean isSelected;

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public LevelSemester(String semesterName) {
        setSemesterName(semesterName);
    }

}
