package com.ostech.fupregpacalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = NavigationActivity.class.getCanonicalName();

    private static final String ABOUT_FRAGMENT = "ABOUT_FRAGMENT";
    private static final String HELP_FRAGMENT = "HELP_FRAGMENT";

    private DrawerLayout rootLayout;
    private ActionBarDrawerToggle drawerToggler;
    private NavigationView navigationView;

    private Fragment onScreenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        rootLayout = findViewById(R.id.drawer_layout);
        drawerToggler = new ActionBarDrawerToggle(this, rootLayout, R.string.nav_open,
                R.string.nav_close);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        rootLayout.addDrawerListener(drawerToggler);
        drawerToggler.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        switchFragment(new IntroFragment());

        //  when activity is started as an intent from SemesterPagerActivity
        switchToAboutOrHelpFragment();
    }   //  end of onCreate()

    private void switchToAboutOrHelpFragment() {
        if (getIntent() != null) {
            String destinationFragmentName = getIntent().getStringExtra(ABOUT_FRAGMENT);

            if (destinationFragmentName == null) {
                destinationFragmentName = getIntent().getStringExtra(HELP_FRAGMENT);
            }

            if (destinationFragmentName != null) {
                switch (destinationFragmentName) {
                    case ABOUT_FRAGMENT:
                        switchFragment(new AboutFragment());
                        break;

                    case HELP_FRAGMENT:
                        switchFragment(new HelpFragment());
                        break;
                }
            }
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
                if (!(onScreenFragment instanceof IntroFragment)) {
                    switchFragment(new IntroFragment());
                }
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

    public void switchFragment(Fragment fragment) {
        onScreenFragment = fragment;

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.dummy_container, onScreenFragment)
                .commit();
    }   //  end of switchFragment()

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to close this app?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }   //  end of onBackPressed()
}   //  end of class