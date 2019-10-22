package za.co.ezzilyf.partner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

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

        FirebaseAuth auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);

        if (auth.getCurrentUser() !=null) {

            checkIfProfileIsCreated(auth.getCurrentUser().getUid());

        }

        initViews();
    }

    private void checkIfProfileIsCreated(final String uid) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("landlords")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (!documentSnapshot.exists()) {

                            //show create profile dialog
                            showCreateProfileDialog(uid);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // show error dialog box
                        showErrorDialog(e);

                    }
                });

    }

    private void showErrorDialog(Exception e) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_error_message, viewGroup, false);


        TextView errorMessage = dialogView.findViewById(R.id.dialog_error_message_body);

        TextView errorTitle = dialogView.findViewById(R.id.dialog_error_message_title);

        Button dismiss = dialogView.findViewById(R.id.dialog_error_message_btnDismiss);


        errorMessage.setText(e.getMessage());

        errorTitle.setText("EzziLife");

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

    }

    private void showSuccessDialog(String message) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_success_message, viewGroup, false);


        TextView successMessage = dialogView.findViewById(R.id.dialog_success_message_body);

        successMessage.setText(message);

        Button dismiss = dialogView.findViewById(R.id.dialog_success_btnDismiss);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });
    }

    private void showCreateProfileDialog(final String uid) {

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_profile, viewGroup, false);


        final TextView tvFullNames = dialogView.findViewById(R.id.dialog_create_profile_etFullNames);

        final TextView tvIdNumber = dialogView.findViewById(R.id.dialog_create_profile_etIdNumber);

        final TextView tvAddress = dialogView.findViewById(R.id.dialog_create_profile_etAddress);

        final TextView tvNationality = dialogView.findViewById(R.id.dialog_create_profile_etNationality);

        final TextView tvContactNumber = dialogView.findViewById(R.id.dialog_create_profile_etContactNumber);


        final Button btnCreate = dialogView.findViewById(R.id.dialog_create_profile_btnCreate);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(tvFullNames.getText())) {

                    tvFullNames.setText("Name is requires");

                    tvFullNames.requestFocus();

                    return;

                }

                if (TextUtils.isEmpty(tvIdNumber.getText())) {

                    tvIdNumber.setText("ID Number is requires");

                    tvIdNumber.requestFocus();

                    return;

                }

                if (TextUtils.isEmpty(tvAddress.getText())) {

                    tvAddress.setText("Address is requires");

                    tvAddress.requestFocus();

                    return;

                }

                if (TextUtils.isEmpty(tvContactNumber.getText())) {

                    tvContactNumber.setText("Contact number is requires");

                    tvContactNumber.requestFocus();

                    return;

                }

                if (TextUtils.isEmpty(tvNationality.getText())) {

                    tvNationality.setText("Nationality is requires");

                    tvNationality.requestFocus();

                    return;

                }

                btnCreate.setEnabled(false);

                btnCreate.setText("Creating profile...");
                // create a profile to database

                HashMap<String, Object> profileDetails = new HashMap<>();

                profileDetails.put("fullNames", tvFullNames.getText().toString());

                profileDetails.put("idNumber", tvIdNumber.getText().toString());

                profileDetails.put("address", tvAddress.getText().toString());

                profileDetails.put("contactNumber", tvContactNumber.getText().toString());

                profileDetails.put("nationality", tvNationality.getText().toString());

                // save details to firebase
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore.collection("landlords")
                        .document(uid)
                        .set(profileDetails)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                // close dialog
                                alertDialog.dismiss();

                                showSuccessDialog("Profile created successfully");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // close dialog
                                alertDialog.dismiss();

                                showErrorDialog(e);

                            }
                        });

            }
        });

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

            mTitle.setText("EzziLife - Landlord");

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

            Toast.makeText(this, "Thank you for using EzziLife", Toast.LENGTH_SHORT).show();

            getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new MyPropertiesFragment(), "Residences");

       // adapter.addFragment(new MyRoomsFragment(), "Rooms");

        adapter.addFragment(new MyTenantsFragment(), "Tenants");

        viewPager.setAdapter(adapter);
    }
}
