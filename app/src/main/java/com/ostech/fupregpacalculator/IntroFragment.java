package com.ostech.fupregpacalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ostech.fupregpacalculator.model.AcademicRecord;
import com.ostech.fupregpacalculator.model.CollegeOfEducation;
import com.ostech.fupregpacalculator.model.Institution;
import com.ostech.fupregpacalculator.model.Level;
import com.ostech.fupregpacalculator.model.LevelSemester;
import com.ostech.fupregpacalculator.model.Polytechnic;
import com.ostech.fupregpacalculator.model.Semester;
import com.ostech.fupregpacalculator.model.University;

import java.util.ArrayList;

public class IntroFragment extends Fragment {
    private static final String TAG = IntroFragment.class.getCanonicalName();

    private Institution institution;

    private AppCompatSpinner institutionSpinner;
    private RecyclerView semestersRecyclerView;
    private AppCompatButton proceedButton;

    private LevelAdapter levelAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro, container, false);

        institutionSpinner = view.findViewById(R.id.institution_spinner);
        semestersRecyclerView = view.findViewById(R.id.intro_semesters_recycler_view);
        proceedButton = view.findViewById(R.id.intro_proceed_button);

        semestersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        institutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSemestersLayout();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupAcademicRecord();
            }
        });

        return view;
    }   //  end of onCreateView()

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = getResources().getString(R.string.gpa_calculator_menu_item_title) + " - " +
                getResources().getString(R.string.gpa_calculator_intro_title);

        getActivity().setTitle(title);
    }

    private void updateSemestersLayout() {
        levelAdapter = null;

        String institutionType = institutionSpinner.getSelectedItem().toString();

        switch (institutionType) {
            case "University":
                institution = new University();
                break;

            case "Polytechnic":
                institution = new Polytechnic();
                break;

            case "College of Education":
                institution = new CollegeOfEducation();
                break;
        }

        if (levelAdapter == null) {
            levelAdapter = new LevelAdapter(institution.getLevels());
            semestersRecyclerView.setAdapter(levelAdapter);
        } else {
            levelAdapter.setLevels(institution.getLevels());
        }
    }   //  end of updateSemestersLayout()

    private void setupAcademicRecord() {
        ArrayList<Semester> semesterList = new ArrayList<>();

        for (Level currentLevel: institution.getLevels()) {
            for (int i = 0; i < currentLevel.getSemesters().size(); i++) {
                LevelSemester currentSemester = currentLevel.getSemesters().get(i);

                if (currentSemester.isSelected()) {
                    Semester semester = new Semester(currentSemester.getSemesterName());
                    semester.setInstitution(institution);
                    semesterList.add(semester);
                }
            }
        }

        if (semesterList.size() != 0) {
            AcademicRecord.getInstance().setInstitutionType(institution);
            AcademicRecord.getInstance().setSemesterList(semesterList);

            Intent semestersSetupIntent = SemestersSetupActivity.newIntent(getActivity());
            startActivity(semestersSetupIntent);
        } else {
            Toast noSemesterSelectedToast = Toast.makeText(getActivity(),
                    getString(R.string.no_semester_selected_toast),
                    Toast.LENGTH_SHORT);
            noSemesterSelectedToast.show();
        }
    }   //  end of setupAcademicRecord()

    private class LevelHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView levelNameTextView;
        private CheckBox firstSemesterCheckedTextView;
        private CheckBox secondSemesterCheckedTextView;

        private Level level;

        public LevelHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.holder_intro_level, parent, false));
            itemView.setOnClickListener(this);

            levelNameTextView = itemView.findViewById(R.id.intro_level_name);
            firstSemesterCheckedTextView = itemView.findViewById(R.id.first_semester_check_box);
            secondSemesterCheckedTextView = itemView.findViewById(R.id.second_semester_check_box);

            firstSemesterCheckedTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    level.getSemesters().get(0).setSelected(isChecked);
                }
            });

            secondSemesterCheckedTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    level.getSemesters().get(1).setSelected(isChecked);
                }
            });
        }

        @Override
        public void onClick(View view) {
        }

        public void bind(Level level) {
            this.level = level;
            levelNameTextView.setText(this.level.getLevelName());
        }
    }

    private class LevelAdapter extends RecyclerView.Adapter<LevelHolder> {
        private ArrayList<Level> levels;

        public LevelAdapter(ArrayList<Level> levels) {
            this.levels = levels;
        }

        @Override
        public LevelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LevelHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(LevelHolder holder, int position) {
            Level currentLevel = levels.get(position);
            holder.bind(currentLevel);
        }

        @Override
        public int getItemCount() {
            return levels.size();
        }

        public void setLevels(ArrayList<Level> levels) {
            this.levels = levels;
        }
    }
}   //  end of class
