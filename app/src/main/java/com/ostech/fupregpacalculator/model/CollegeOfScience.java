package com.ostech.fupregpacalculator.model;

import java.io.Serializable;

public class CollegeOfScience extends College implements Serializable {
    public CollegeOfScience() {
        getLevels().add(new Level("100 Level"));
        getLevels().add(new Level("200 Level"));
        getLevels().add(new Level("300 Level"));
        getLevels().add(new Level("400 Level"));
    }
}
