package za.co.ezzilyf.partner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.fragments.MyPropertiesFragment;
import za.co.ezzilyf.partner.fragments.MyRoomsFragment;
import za.co.ezzilyf.partner.fragments.MyTenantsFragment;
import za.co.ezzilyf.partner.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);

        displayInitialFragment();

        BottomNavigationView navigation = findViewById(R.id.main_bottom_nanvigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void displayInitialFragment(){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, MyPropertiesFragment.getInstance())
                .commit();

        getSupportActionBar().setTitle("My Properties");

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_my_properties:
                    toolbar.setTitle("My Properties");

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment_container,
                                    MyPropertiesFragment.getInstance())
                            .commit();

                    return true;
                case R.id.navigation_my_rooms:
                    toolbar.setTitle("My Rooms");

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment_container,
                                    MyRoomsFragment.getInstance())
                            .commit();

                    return true;
                case R.id.navigation_my_tenants:
                    toolbar.setTitle("My Tenants");

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment_container,
                                    MyTenantsFragment.getInstance())
                            .commit();

                    return true;

                case R.id.navigation_profile:
                    toolbar.setTitle("Settings");

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment_container,
                                    SettingsFragment.getInstance())
                            .commit();

                    return true;

            }
            return false;
        }
    };
}
