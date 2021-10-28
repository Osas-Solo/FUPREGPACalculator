package com.ostech.fupregpacalculator;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ostech.fupregpacalculator.SemesterPagerActivity;
import com.ostech.fupregpacalculator.model.AcademicRecord;
import com.ostech.fupregpacalculator.model.Course;
import com.ostech.fupregpacalculator.model.Institution;
import com.ostech.fupregpacalculator.model.Polytechnic;
import com.ostech.fupregpacalculator.model.Semester;

import java.util.ArrayList;
import java.util.Arrays;

public class SemesterFragment extends Fragment {
    private static final String TAG = SemesterFragment.class.getCanonicalName();

    private static final String ARG_SEMESTER_POSITION = "current_semester";
    private static final String ARG_ADDITIONAL_NUMBER_OF_COURSES = "number_of_courses";

    private RecyclerView semesterRecyclerView;
    private AppCompatButton previousSemesterButton;
    private AppCompatButton nextSemesterButton;
    private AppCompatButton calculateButton;

    private int semesterPosition;
    private Semester currentSemester;

    private CourseAdapter courseAdapter;

    public static SemesterFragment newInstance(int semesterPosition) {
        Bundle args = new Bundle();
        args.putInt(ARG_SEMESTER_POSITION, semesterPosition);

        SemesterFragment fragment = new SemesterFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        semesterPosition = getArguments().getInt(ARG_SEMESTER_POSITION);
        currentSemester = AcademicRecord.getInstance().getSemesterList().get(semesterPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semester, container, false);

        semesterRecyclerView = view.findViewById(R.id.semester_recycler_view);
        previousSemesterButton = view.findViewById(R.id.previous_semester_button);
        nextSemesterButton = view.findViewById(R.id.next_semester_button);
        calculateButton = view.findViewById(R.id.calculate_button);

        updateButtonsVisibility();

        SemesterPagerActivity semesterPagerActivity = (SemesterPagerActivity) getActivity();

        previousSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterPagerActivity.navigateToPreviousSemester(semesterPosition);
            }
        });

        nextSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semesterPagerActivity.navigateToNextSemester(semesterPosition);
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToResultActivity();
            }
        });

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            semesterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            int numberOfColumns = 3;
            semesterRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                    numberOfColumns));
        }

        updateSemesterRecyclerView();

        return view;
    }   //  end of onCreateView()

    private void updateSemesterRecyclerView() {
        ArrayList<Course> courseList = currentSemester.getCourseList();

        if (courseAdapter == null) {
            courseAdapter = new CourseAdapter(courseList);
            semesterRecyclerView.setAdapter(courseAdapter);
        } else {
            courseAdapter.setCourses(courseList);
            courseAdapter.notifyDataSetChanged();
        }
    }

    private void updateButtonsVisibility() {
        calculateButton.setVisibility(View.INVISIBLE);

        if (semesterPosition == 0) {
            previousSemesterButton.setVisibility(View.INVISIBLE);

            if (semesterPosition == AcademicRecord.getInstance().getNumberOfSemesters() - 1) {
                nextSemesterButton.setVisibility(View.INVISIBLE);
                calculateButton.setVisibility(View.VISIBLE);
            }
        } else if (semesterPosition == AcademicRecord.getInstance().getNumberOfSemesters() - 1) {
            nextSemesterButton.setVisibility(View.INVISIBLE);
            calculateButton.setVisibility(View.VISIBLE);
        } else {
            calculateButton.setVisibility(View.INVISIBLE);
        }
    }

    private void goToResultActivity() {
        Intent resultIntent = ResultActivity.newIntent(getActivity());
        startActivity(resultIntent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(currentSemester.getSemesterName());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateButtonsVisibility();
        updateSemesterRecyclerView();
        getActivity().setTitle(currentSemester.getSemesterName());
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_semester, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_courses_menu_item) {
            AddCoursesDialogFragment dialog = AddCoursesDialogFragment.newInstance();
            dialog.show(getActivity().getSupportFragmentManager(), ARG_ADDITIONAL_NUMBER_OF_COURSES);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addCoursesToSemester() {
        int additionalNumberOfCourses = ((SemesterPagerActivity) getActivity()).
                additionalNumberOfCourses;

        if (additionalNumberOfCourses > 0) {
            Toast courseAddedToast = new Toast(getActivity());

            Log.i(TAG, "addCoursesToSemester: Number of courses: " + additionalNumberOfCourses);

            int totalNumberOfCoursesInSemester = currentSemester.getNumberOfCourses() +
                    additionalNumberOfCourses;

            if (totalNumberOfCoursesInSemester < 30) {
                courseAddedToast = Toast.makeText(getActivity(),
                        getString(R.string.courses_added_successfully_toast, additionalNumberOfCourses),
                        Toast.LENGTH_SHORT);

                currentSemester.addCourses(additionalNumberOfCourses);
                updateSemesterRecyclerView();
            } else {
                courseAddedToast = Toast.makeText(getActivity(),
                        getString(R.string.too_many_courses_in_semester_toast),
                        Toast.LENGTH_SHORT);
            }

            courseAddedToast.show();
        }
    }

    private void deleteCourse(Course unneededCourse) {
        ArrayList<Course> courseList = currentSemester.getCourseList();

        courseList.remove(unneededCourse);

        updateSemesterRecyclerView();

        Toast courseDeletedToast = Toast.makeText(getActivity(),
                getString(R.string.course_deleted_successfully_toast, unneededCourse.getCourseCode()),
                Toast.LENGTH_SHORT);
        courseDeletedToast.show();
    }

    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText semesterCourseCodeEditText;
        private EditText semesterCreditUnitEditText;
        private Spinner semesterGradeSpinner;
        private ImageButton semesterDeleteCourseButton;

        private Course course;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.holder_semester_course, parent, false));
            itemView.setOnClickListener(this);

            semesterCourseCodeEditText = itemView.findViewById(R.id.semester_course_code_edit_text);
            semesterCreditUnitEditText = itemView.findViewById(R.id.semester_credit_unit_edit_text);
            semesterGradeSpinner = itemView.findViewById(R.id.semester_grade_spinner);
            semesterDeleteCourseButton = itemView.findViewById(R.id.semester_delete_course_image_button);

            semesterCourseCodeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    course.setCourseCode(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            semesterCreditUnitEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() != 0) {
                        int creditUnit = Integer.parseInt(s.toString());
                        course.setCreditUnit(creditUnit);
                    } else {
                        course.setCreditUnit(0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            semesterGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String grade = semesterGradeSpinner.getSelectedItem().toString();

                    course.setGrade(grade);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            semesterDeleteCourseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCourse(course);
                }
            });
        }

        @Override
        public void onClick(View view) {
        }

        public void bind(Course course) {
            this.course = course;

            semesterCourseCodeEditText.setText(this.course.getCourseCode());

            if (this.course.getCreditUnit() != 0) {
                semesterCreditUnitEditText.setText("" + this.course.getCreditUnit());
            }

            Institution institution = AcademicRecord.getInstance().getInstitutionType();
            String[] gradesArray;

            if (institution instanceof Polytechnic) {
                gradesArray = getResources().getStringArray(R.array.polytechnic_grades);
            } else {
                gradesArray = getResources().getStringArray(
                        R.array.university_and_college_of_education_grades);
            }

            ArrayAdapter<String> gradesArrayAdapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_spinner_item, gradesArray );
            semesterGradeSpinner.setAdapter(gradesArrayAdapter);

            int gradeIndex = Arrays.binarySearch(institution.getGrades(), course.getGrade());

            semesterGradeSpinner.setSelection(gradeIndex);
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {
        private ArrayList<Course> courses;

        public CourseAdapter(ArrayList<Course> courses) {
            this.courses = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CourseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            Course currentCourse = courses.get(position);
            holder.bind(currentCourse);
        }

        @Override
        public int getItemCount() {
            return courses.size();
        }

        public void setCourses(ArrayList<Course> courses) {
            this.courses = courses;
        }
    }
}   //  end of class
