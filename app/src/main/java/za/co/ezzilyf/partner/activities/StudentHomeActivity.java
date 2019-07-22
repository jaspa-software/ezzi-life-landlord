package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.fragments.MyPropertiesFragment;
import za.co.ezzilyf.partner.fragments.MyRoomsFragment;
import za.co.ezzilyf.partner.fragments.MyTenantsFragment;
import za.co.ezzilyf.partner.fragments.SettingsFragment;
import za.co.ezzilyf.partner.fragments.StudentAccountFragment;
import za.co.ezzilyf.partner.fragments.StudentHomeFragment;
import za.co.ezzilyf.partner.fragments.StudentNoticesFragment;

public class StudentHomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_home);

        toolbar = findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);

        displayInitialFragment();

        BottomNavigationView navigation = findViewById(R.id.student_bottom_navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_my_properties:
                    toolbar.setTitle("Home");

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.student_frame_container,
                                    StudentHomeFragment.getInstance())
                            .commit();

                    return true;
                case R.id.navigation_my_rooms:
                    toolbar.setTitle("Notices");

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.student_frame_container,
                                    StudentNoticesFragment.getInstance())
                            .commit();

                    return true;
                case R.id.navigation_my_tenants:
                    toolbar.setTitle("Account");

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.student_frame_container,
                                    StudentAccountFragment.getInstance())
                            .commit();

                    return true;

            }
            return false;
        }
    };

    private void displayInitialFragment(){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.student_frame_container, StudentHomeFragment.getInstance())
                .commit();

        getSupportActionBar().setTitle("Home");

    }


}
