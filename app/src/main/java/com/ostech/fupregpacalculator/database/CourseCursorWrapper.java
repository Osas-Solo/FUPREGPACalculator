package com.ostech.fupregpacalculator.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.ostech.fupregpacalculator.model.Course;

import java.util.UUID;

import static com.ostech.fupregpacalculator.database.DepartmentsDatabaseSchema.DepartmentsDatabaseTables.*;
import static com.ostech.fupregpacalculator.database.DepartmentsDatabaseSchema.DepartmentsDatabaseTables.Columns.*;

public class CourseCursorWrapper extends CursorWrapper {
    public CourseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Course getCourse() {
        String courseCode = getString(getColumnIndex(COURSE_CODE));
        int creditUnit = getInt(getColumnIndex(CREDIT_UNIT));

        Course course = new Course(courseCode, creditUnit, "A");

        return course;
    }
}
