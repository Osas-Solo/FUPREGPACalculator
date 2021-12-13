package com.ostech.fupregpacalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.ostech.fupregpacalculator.model.AcademicRecord;

public class ResultActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = ResultActivity.class.getCanonicalName();
    private static final String ACADEMIC_RECORD = "ACADEMIC_RECORD";

    private DrawerLayout rootLayout;
    private ActionBarDrawerToggle drawerToggler;
    private NavigationView navigationView;

    private Fragment onScreenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (savedInstanceState != null) {
            restoreAcademicRecord(savedInstanceState);
        }

        rootLayout = findViewById(R.id.result_drawer_layout);
        drawerToggler = new ActionBarDrawerToggle(this, rootLayout, R.string.nav_open,
                R.string.nav_close);
        navigationView = findViewById(R.id.result_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        rootLayout.addDrawerListener(drawerToggler);
        drawerToggler.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        switchFragment(new ResultFragment());
    }   //  end of onCreate()

    private void restoreAcademicRecord(Bundle savedInstanceState) {
        AcademicRecord recoveredAcademicRecord =
                (AcademicRecord) savedInstanceState.getSerializable("ACADEMIC_RECORD");

        if (recoveredAcademicRecord != null) {
            Log.i(TAG, "onCreate: Recovered academic record:" + recoveredAcademicRecord.getCollegeType());
        }

        AcademicRecord academicRecord = AcademicRecord.getInstance(this);
        academicRecord.setCollegeType(recoveredAcademicRecord.getCollegeType());
        academicRecord.setDepartmentName(recoveredAcademicRecord.getDepartmentName());
        academicRecord.setSemesterList(recoveredAcademicRecord.getSemesterList());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable(ACADEMIC_RECORD, AcademicRecord.getInstance(this));
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
                if (!(onScreenFragment instanceof HelpFragment)) {
                    switchFragment(new HelpFragment());
                }
                break;

            case R.id.about_menu_item:
                if (!(onScreenFragment instanceof AboutFragment)) {
                    switchFragment(new AboutFragment());
                }
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

    public void switchFragment(Fragment fragment) {
        onScreenFragment = fragment;

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.result_dummy_container, onScreenFragment)
                .commit();
    }   //  end of switchFragment()

    @Override
    public void onBackPressed() {
        finish();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ResultActivity.class);

        return intent;
    }
}
