package com.ostech.fupregpacalculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ostech.fupregpacalculator.model.AcademicRecord;
import com.ostech.fupregpacalculator.model.Semester;

import java.util.ArrayList;

public class SemestersSetupFragment extends Fragment {
    private static final String TAG = SemestersSetupFragment.class.getCanonicalName();
    
    private RecyclerView semestersRecyclerView;
    private AppCompatButton proceedButton;

    private final ArrayList<Semester> semesterList = AcademicRecord.getInstance(getActivity()).getSemesterList();

    private SemesterAdapter semesterAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semesters_setup, container, false);

        semestersRecyclerView = view.findViewById(R.id.semesters_setup_recycler_view);
        proceedButton = view.findViewById(R.id.semesters_setup_proceed_button);

        semestersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (semesterAdapter == null) {
            semesterAdapter = new SemesterAdapter(semesterList);
            semestersRecyclerView.setAdapter(semesterAdapter);
        } else {
            semesterAdapter.setSemesters(semesterList);
        }

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCalculatorActivity();
            }
        });

        return view;
    }   //  end of onCreateView()

    private void goToCalculatorActivity() {
        boolean areTooManyCoursesInSemester = areTooManyCoursesInSemester();

        if (areTooManyCoursesInSemester) {
            Toast tooManyCoursesInSemesterToast = Toast.makeText(getActivity(),
                    getString(R.string.too_many_courses_in_semester_toast), Toast.LENGTH_SHORT);
            tooManyCoursesInSemesterToast.show();
        } else {
            Intent semesterPagerIntent = SemesterPagerActivity.newIntent(getActivity());
            startActivity(semesterPagerIntent);
        }
    }

    private boolean areTooManyCoursesInSemester() {
        int maximumNumberOfCourses = 30;

        for (Semester currentSemester: semesterList) {
            if (currentSemester.getNumberOfCourses() > maximumNumberOfCourses) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getResources().getString(R.string.gpa_calculator_menu_item_title) + " - " +
                getResources().getString(R.string.gpa_calculator_semesters_setup_title);

        getActivity().setTitle(title);
    }

    private class SemesterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView semesterNameTextView;
        private EditText courseNumberEditText;

        private Semester semester;

        public SemesterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.holder_semesters_setup_semester, parent, false));
            itemView.setOnClickListener(this);

            semesterNameTextView = itemView.findViewById(R.id.semesters_setup_semester_name);
            courseNumberEditText = itemView.findViewById(R.id.semesters_setup_course_number_edit_text);

            courseNumberEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() != 0) {
                        int numberOfCourses = Integer.parseInt(s.toString());

                        semester.setCourseList(numberOfCourses);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        @Override
        public void onClick(View view) {
        }

        public void bind(Semester semester) {
            this.semester = semester;
            semesterNameTextView.setText(this.semester.getSemesterName());

            if (semester.getNumberOfCourses() > 0) {
                courseNumberEditText.setText(this.semester.getNumberOfCourses());
            }
        }
    }

    private class SemesterAdapter extends RecyclerView.Adapter<SemesterHolder> {
        private ArrayList<Semester> semesters;

        public SemesterAdapter(ArrayList<Semester> semesters) {
            this.semesters = semesters;
        }

        @Override
        public SemesterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SemesterHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SemesterHolder holder, int position) {
            Semester currentSemester = semesters.get(position);
            holder.bind(currentSemester);
        }

        @Override
        public int getItemCount() {
            return semesters.size();
        }

        public void setSemesters(ArrayList<Semester> semesters) {
            this.semesters = semesters;
        }
    }
}   //  end of class
