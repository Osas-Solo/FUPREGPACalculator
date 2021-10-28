package com.ostech.fupregpacalculator.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable {
    private String levelName;
    private ArrayList<LevelSemester> semesters;

    public Level(String levelName) {
        setLevelName(levelName);
        setSemesters(new ArrayList<>(2));
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public ArrayList<LevelSemester> getSemesters() {
        return semesters;
    }

    public void setSemesters(ArrayList<LevelSemester> semesters) {
        this.semesters = semesters;

        int numberOfSemesters = 2;

        for (int i = 0; i < numberOfSemesters; i++) {
            this.semesters.add(new LevelSemester(this.levelName));

            String semesterName = this.levelName + " " + ((i == 0) ? "1st " : "2nd ") +
                    "Semester";
            this.semesters.get(i).setSemesterName(semesterName);
        }
    }

}
