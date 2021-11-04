package com.ostech.fupregpacalculator.model;

public class CollegeOfTechnology extends College {
    public CollegeOfTechnology() {
        getLevels().add(new Level("100 Level"));
        getLevels().add(new Level("200 Level"));
        getLevels().add(new Level("300 Level"));
        getLevels().add(new Level("400 Level"));
        getLevels().add(new Level("500 Level"));
    }
}
