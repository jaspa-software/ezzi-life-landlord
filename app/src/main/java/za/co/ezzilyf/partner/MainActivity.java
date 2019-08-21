package za.co.ezzilyf.partner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.activities.HelpActivity;
import za.co.ezzilyf.partner.adapters.ViewPagerAdapter;
import za.co.ezzilyf.partner.fragments.MyPropertiesFragment;
import za.co.ezzilyf.partner.fragments.MyRoomsFragment;
import za.co.ezzilyf.partner.fragments.MyTenantsFragment;
import za.co.ezzilyf.partner.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ViewPagerAdapter viewPagerAdapter;

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

        initViews();
    }

    private void initViews() {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        //setup view pager
        ViewPager viewPager = findViewById(R.id.main_viewPager);

        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.main_tabLayout);

        Toolbar toolbar = findViewById(R.id.main_toolbar);

        TextView mTitle = toolbar.findViewById(R.id.toolbar_title_main);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() !=null) {

            mTitle.setText("Ezzi Life - Landlord");

            mTitle.setTextColor(getResources().getColor(android.R.color.white));

            getSupportActionBar().setDisplayShowTitleEnabled(false);

            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }

        tabLayout.setupWithViewPager(viewPager);

       // viewPager.setCurrentItem(1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_profile) {

            Intent intent = new Intent(MainActivity.this, HelpActivity.class);

            startActivity(intent);

        }

        if (id == R.id.menu_sign_out) {

            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.signOut();

            Toast.makeText(this, "Thank you for using Ezzi Life (L)", Toast.LENGTH_SHORT).show();

            getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new MyPropertiesFragment(), "Properties");

        adapter.addFragment(new MyRoomsFragment(), "Rooms");

        adapter.addFragment(new MyTenantsFragment(), "Tenants");

        viewPager.setAdapter(adapter);
    }
}
