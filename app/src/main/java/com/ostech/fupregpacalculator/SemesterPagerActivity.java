package com.ostech.fupregpacalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.ostech.fupregpacalculator.model.AcademicRecord;
import com.ostech.fupregpacalculator.model.Semester;

import java.util.ArrayList;
import java.util.List;

public class SemesterPagerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AddCoursesDialogFragment.AddCoursesDialogListener {

    private static final String TAG = SemesterPagerActivity.class.getCanonicalName();
    private static final String ACADEMIC_RECORD = "ACADEMIC_RECORD";

    private DrawerLayout rootLayout;
    private ActionBarDrawerToggle drawerToggler;
    private NavigationView navigationView;

    public ViewPager2 semesterViewPager;

    private static ArrayList<Semester> semesterList;
    public int additionalNumberOfCourses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_pager);

        if (savedInstanceState != null) {
            restoreAcademicRecord(savedInstanceState);
        }

        rootLayout = findViewById(R.id.semester_pager_drawer_layout);
        drawerToggler = new ActionBarDrawerToggle(this, rootLayout, R.string.nav_open,
                R.string.nav_close);
        navigationView = findViewById(R.id.semester_pager_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        rootLayout.addDrawerListener(drawerToggler);
        drawerToggler.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        semesterViewPager = findViewById(R.id.semester_view_pager);

        semesterList = AcademicRecord.getInstance().getSemesterList();

        semesterViewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public Fragment createFragment(int position) {
                return SemesterFragment.newInstance(position);
            }

            @Override
            public int getItemCount() {
                return semesterList.size();
            }
        });

        for (int i = 0; i < semesterList.size(); i++) {
            semesterViewPager.setCurrentItem(i);
            break;
        }
    }

    private void restoreAcademicRecord(Bundle savedInstanceState) {
        AcademicRecord recoveredAcademicRecord =
                (AcademicRecord) savedInstanceState.getSerializable("ACADEMIC_RECORD");

        if (recoveredAcademicRecord != null) {
            Log.i(TAG, "onCreate: Recovered academic record:" + recoveredAcademicRecord.getInstitutionType());
        }

        AcademicRecord academicRecord = AcademicRecord.getInstance();
        academicRecord.setInstitutionType(recoveredAcademicRecord.getInstitutionType());
        academicRecord.setSemesterList(recoveredAcademicRecord.getSemesterList());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable(ACADEMIC_RECORD, AcademicRecord.getInstance());
    }

    @Override
    public void onBackPressed() {
        if (semesterViewPager.getCurrentItem() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to reset the course details you have entered?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        } else {
            semesterViewPager.setCurrentItem(semesterViewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggler.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }   //  end of onOptionsItemSelected()

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gpa_calculator_menu_item:
                returnHome();
                break;

            case R.id.help_menu_item:
                returnHome("HELP_FRAGMENT");
                break;

            case R.id.about_menu_item:
                returnHome("ABOUT_FRAGMENT");
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        rootLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void returnHome() {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void returnHome(String destinationFragmentName) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra(destinationFragmentName, destinationFragmentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void navigateToPreviousSemester(int position) {
        if (position != 0) {
            semesterViewPager.setCurrentItem(position - 1);
        }
    }

    public void navigateToNextSemester(int position) {
        if (position != semesterList.size() - 1) {
            semesterViewPager.setCurrentItem(position + 1);
        }
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SemesterPagerActivity.class);

        return intent;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        AppCompatEditText addCoursesEditText = ((AddCoursesDialogFragment) dialog).addCoursesEditText;

        additionalNumberOfCourses = (addCoursesEditText.getText().toString() == "")
                ? 0 : Integer.parseInt(addCoursesEditText.getText().toString());

        Log.i(TAG, "onDialogPositiveClick: number of courses " + additionalNumberOfCourses);

        SemesterFragment currentSemesterFragment = (SemesterFragment) getCurrentSemesterFragment();

        currentSemesterFragment.addCoursesToSemester();
    }

    public Fragment getCurrentSemesterFragment(){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments != null){
            for (Fragment currentFragment : fragments) {
                if (currentFragment != null && currentFragment.isVisible())
                    return currentFragment;
            }
        }

        return null;
    }
}
