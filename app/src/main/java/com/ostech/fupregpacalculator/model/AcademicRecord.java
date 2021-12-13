package com.ostech.fupregpacalculator.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ostech.fupregpacalculator.database.CourseCursorWrapper;
import com.ostech.fupregpacalculator.database.DepartmentsDatabaseHelper;

import static com.ostech.fupregpacalculator.database.DepartmentsDatabaseSchema.DepartmentsDatabaseTables.Columns.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcademicRecord implements Serializable {
    private static final String TAG = AcademicRecord.class.getCanonicalName();

    private static AcademicRecord academicRecord;

    private College collegeType;
    private String departmentName;
    private ArrayList<Semester> semesterList = new ArrayList<>();
    private double cumulativeGradePointAverage;

    private Context context;
    private SQLiteDatabase departmentsDatabase;

    public College getCollegeType() {
        return collegeType;
    }

    public void setCollegeType(College collegeType) {
        this.collegeType = collegeType;

        for (Semester currentSemester: getSemesterList()) {
            currentSemester.setCollege(collegeType);
        }
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public ArrayList<Semester> getSemesterList() {
        return semesterList;
    }

    public void setSemesterList(ArrayList<Semester> semesterList) {
        this.semesterList = semesterList;
    }

    public double getCumulativeGradePointAverage() {
        calculateCGPA();

        return (double) (Math.round(cumulativeGradePointAverage * 100)) / 100;
    }

    public static AcademicRecord getInstance(Context context) {
        if (academicRecord == null) {
            academicRecord = new AcademicRecord(context);
        }

        return academicRecord;
    }

    private AcademicRecord(Context context) {
        this.context = context.getApplicationContext();
        this.departmentsDatabase = new DepartmentsDatabaseHelper(context).getReadableDatabase();
    }

    public int getNumberOfSemesters() {
        return semesterList.size();
    }

    public void calculateCGPA() {
        for (Semester currentSemester: semesterList) {
            currentSemester.calculateSemesterGPA();
        }

        cumulativeGradePointAverage = (getTotalCreditUnit() != 0) ?
                (getTotalGradePoint() / getTotalCreditUnit()) : 0;
    }

    public double getTotalCreditUnit() {
        double totalCreditUnit = 0;

        for (Semester currentSemester: semesterList) {
            totalCreditUnit += currentSemester.getTotalCreditUnit();
        }

        return totalCreditUnit;
    }

    public double getTotalGradePoint() {
        double totalGradePoint = 0;

        for (Semester currentSemester: semesterList) {
            totalGradePoint += currentSemester.getTotalGradePoint();
        }

        return totalGradePoint;
    }

    public int getNumberOfCourses() {
        int totalCourseNumber = 0;

        for (Semester currentSemester: semesterList) {
            totalCourseNumber += currentSemester.getNumberOfCourses();
        }

        return totalCourseNumber;
    }

    public void getCoursesFromDatabase() {
        for (Semester currentSemester: semesterList) {
            getSemesterCoursesFromDatabase(currentSemester);
        }
    }

    private void getSemesterCoursesFromDatabase(Semester semester) {
        Pattern semesterDetailsPattern = Pattern.compile("([1-5]00) level (1st|2nd) semester",
                Pattern.CASE_INSENSITIVE);
        Log.i(TAG, "getSemesterCoursesFromDatabase: Semester name: " + semester.getSemesterName());
        Matcher semesterDetailsMatcher = semesterDetailsPattern.matcher(semester.getSemesterName());

        String level = semesterDetailsMatcher.group(1);
        String semesterNumber = semesterDetailsMatcher.group(2);

        String departmentName = getDepartmentName().replace(" ", "_");

        String selectionColumns = LEVEL + " = ? AND " + SEMESTER + " = ?";
        String[] selectionColumnArguments = {level, semesterNumber};
        String sortOrder = COURSE_CODE;

        CourseCursorWrapper cursor = (CourseCursorWrapper) departmentsDatabase.query(
                departmentName,
                null,
                selectionColumns,
                selectionColumnArguments,
                null,
                null,
                sortOrder
        );

        try {
            cursor.moveToFirst();

            semester.getCourseList().add(cursor.getCourse());
        } finally {
            cursor.close();
        }

    }   //  end of getSemesterCoursesFromDatabase()

    public void sortCoursesInSemesters() {
        for (Semester currentSemester: semesterList) {
            Collections.sort(currentSemester.getCourseList(), Course.courseCodeComparator);
        }
    }
}
