package com.ostech.fupregpacalculator;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ostech.fupregpacalculator.model.AcademicRecord;
import com.ostech.fupregpacalculator.model.Course;
import com.ostech.fupregpacalculator.model.Institution;
import com.ostech.fupregpacalculator.model.Semester;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ResultFragment extends Fragment {
    private static final String TAG = ResultFragment.class.getCanonicalName();

    private RecyclerView semestersRecyclerView;
    private AppCompatTextView cgpaTextView;
    private AppCompatTextView tnuTextView;
    private AppCompatTextView tcpTextView;
    private AppCompatTextView numberOfCoursesTextView;
    private AppCompatTextView remarkTextView;
    private RecyclerView semesterCoursesRecyclerView;

    private final ArrayList<Semester> semesterList = AcademicRecord.getInstance().getSemesterList();
    private final Institution institution = AcademicRecord.getInstance().getInstitutionType();

    private SemesterAdapter semesterAdapter;
    private SemesterCoursesAdapter semesterCoursesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        semestersRecyclerView = view.findViewById(R.id.result_semester_recycler_view);
        cgpaTextView = view.findViewById(R.id.result_cgpa_text_view);
        tnuTextView = view.findViewById(R.id.result_tnu_text_view);
        tcpTextView = view.findViewById(R.id.result_tcp_text_view);
        numberOfCoursesTextView = view.findViewById(R.id.result_number_of_courses_text_view);
        remarkTextView = view.findViewById(R.id.result_remark_text_view);
        semesterCoursesRecyclerView = view.findViewById(R.id.result_semester_courses_recycler_view);

        AcademicRecord.getInstance().calculateCGPA();

        double cgpa = AcademicRecord.getInstance().getCumulativeGradePointAverage();
        double totalCreditUnit = AcademicRecord.getInstance().getTotalCreditUnit();
        double totalGradePoint = AcademicRecord.getInstance().getTotalGradePoint();
        int numberOfCourses = AcademicRecord.getInstance().getNumberOfCourses();
        String remark = institution.getRemark(cgpa);

        cgpaTextView.setText(String.format("%.2f", cgpa));
        tnuTextView.setText(String.format("%.0f", totalCreditUnit));
        tcpTextView.setText(String.format("%.0f", totalGradePoint));
        numberOfCoursesTextView.setText(String.format("%d", numberOfCourses));
        remarkTextView.setText(remark);

        semestersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        AcademicRecord.getInstance().sortCoursesInSemesters();

        if (semesterAdapter == null) {
            semesterAdapter = new SemesterAdapter(semesterList);
            semestersRecyclerView.setAdapter(semesterAdapter);
        } else {
            semesterAdapter.setSemesters(semesterList);
        }

        semesterCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (semesterCoursesAdapter == null) {
            semesterCoursesAdapter = new SemesterCoursesAdapter(semesterList);
            semesterCoursesRecyclerView.setAdapter(semesterCoursesAdapter);
        } else {
            semesterCoursesAdapter.setSemesters(semesterList);
        }

        return view;
    }   //  end of onCreateView()

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getResources().getString(R.string.gpa_calculator_menu_item_title) + " - " +
                getResources().getString(R.string.gpa_calculator_result_title);

        getActivity().setTitle(title);
    }

    private class SemesterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AppCompatTextView gpaSemesterTextView;
        private AppCompatTextView gpaTextView;
        private AppCompatTextView tnuTextView;
        private AppCompatTextView tcpTextView;
        private AppCompatTextView numberOfCoursesTextView;
        private AppCompatTextView remarkTextView;
        private Semester semester;

        public SemesterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.holder_result_semester, parent, false));
            itemView.setOnClickListener(this);

            gpaSemesterTextView = itemView.findViewById(R.id.gpa_semester_text_view);
            gpaTextView = itemView.findViewById(R.id.gpa_text_view);
            tnuTextView = itemView.findViewById(R.id.tnu_text_view);
            tcpTextView = itemView.findViewById(R.id.tcp_text_view);
            numberOfCoursesTextView = itemView.findViewById(R.id.number_of_courses_text_view);
            remarkTextView = itemView.findViewById(R.id.remark_text_view);
        }

        @Override
        public void onClick(View view) {
        }

        public void bind(Semester semester) {
            this.semester = semester;
            double gpa = semester.getSemesterGPA();
            double totalCreditUnit = semester.getTotalCreditUnit();
            double totalGradePoint = semester.getTotalGradePoint();
            int numberOfCourses = semester.getNumberOfCourses();
            String remark = institution.getRemark(gpa);

            gpaSemesterTextView.setText(semester.getSemesterName());

            gpaTextView.setText(String.format("%.2f", gpa));
            tnuTextView.setText(String.format("%.0f", totalCreditUnit));
            tcpTextView.setText(String.format("%.0f", totalGradePoint));
            numberOfCoursesTextView.setText(String.format("%d", numberOfCourses));
            remarkTextView.setText(remark);
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

    private class SemesterCoursesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Semester semester;
        private AppCompatTextView semesterNameTextView;
        private TableLayout coursesTableLayout;

        public SemesterCoursesHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.holder_result_semester_courses, parent, false));
            itemView.setOnClickListener(this);

            semesterNameTextView = itemView.findViewById(R.id.holder_result_semester_name_text_view);
            coursesTableLayout = itemView.findViewById(R.id.result_semester_courses_table);
        }

        @Override
        public void onClick(View view) {
        }

        public void bind(Semester semester) {
            this.semester = semester;

            semesterNameTextView.setText(semester.getSemesterName());
            setSemesterResultTable(coursesTableLayout, semester);
        }

        private void setSemesterResultTable(TableLayout semesterResultTable, Semester semester) {
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
            layoutParams.setMargins(5, 0, 5, 0);
            layoutParams.width = WRAP_CONTENT;
            layoutParams.height = WRAP_CONTENT;

            for (int i = 0; i < semester.getNumberOfCourses(); i++) {
                TableRow currentRow = new TableRow(getActivity());
                Course currentCourse = semester.getCourseList().get(i);

                AppCompatTextView courseCodeTextView = new AppCompatTextView(getActivity());
                courseCodeTextView.setText(currentCourse.getCourseCode());
                alignTableCell(courseCodeTextView);

                AppCompatTextView creditUnitTextView = new AppCompatTextView(getActivity());
                creditUnitTextView.setText("" + currentCourse.getCreditUnit());
                alignTableCell(creditUnitTextView);

                AppCompatTextView gradeTextView = new AppCompatTextView(getActivity());
                gradeTextView.setText(currentCourse.getGrade());
                alignTableCell(gradeTextView);

                currentRow.addView(courseCodeTextView, layoutParams);
                currentRow.addView(creditUnitTextView, layoutParams);
                currentRow.addView(gradeTextView, layoutParams);

                semesterResultTable.addView(currentRow);
            }
        }

        private void alignTableCell(AppCompatTextView courseCodeTextView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                courseCodeTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            courseCodeTextView.setPadding(5, 5, 5, 5);
        }
    }

    private class SemesterCoursesAdapter extends RecyclerView.Adapter<SemesterCoursesHolder> {
        private ArrayList<Semester> semesters;

        public SemesterCoursesAdapter(ArrayList<Semester> semesters) {
            this.semesters = semesters;
        }

        @Override
        public SemesterCoursesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SemesterCoursesHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SemesterCoursesHolder holder, int position) {
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
