package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.models.Property;
import za.co.ezzilyf.partner.utils.SharedPrefConfig;

public class ListPropertyActivity extends AppCompatActivity {

    private static final String TAG = "ListPropertyActivity";

    private EditText etPropertyName, etPropertyAddress;

    private RadioGroup rbPropertyType;

    private RadioButton rbApartment, rbHouse, rbBlockRooms;

    private AutocompleteSupportFragment autocompleteSupportFragment;

    private CoordinatorLayout rootLayout;

    private ScrollView scrollView;

    private Button btnSubmit;

    private FirebaseUser user;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_property);

        initViews();

        initGooglePlaces();

        user = FirebaseAuth.getInstance().getCurrentUser();

        btnSubmit = findViewById(R.id.list_my_property_btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitListing();

            }
        });

    }

    private void submitListing() {

        // check data
        if(TextUtils.isEmpty(etPropertyName.getText())) {

            etPropertyName.setError("Property Name is Required!");

            etPropertyName.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(etPropertyAddress.getText())) {

            etPropertyAddress.setError("Property Address is Required!");

            etPropertyAddress.requestFocus();

            return;
        }

        if (!rbHouse.isChecked() && !rbBlockRooms.isChecked() && !rbApartment.isChecked()) {

            showSnackBar("Property Type is required");

            return;
        }

        String propertyType = "";

        if (rbHouse.isChecked()) {

            propertyType = "House";
        }

        if (rbBlockRooms.isChecked()) {

            propertyType = "Block Rooms";
        }

        if (rbApartment.isChecked()) {

            propertyType = "Apartment";
        }

        HashMap<String, Object> property = new HashMap<>();

        final String propertyRefNumber = ""+Calendar.getInstance().getTimeInMillis();

        property.put("propertyRefNumber",propertyRefNumber.substring(propertyRefNumber.length() - 6));

        property.put("propertyName",etPropertyName.getText().toString());

        property.put("propertyType",propertyType);

        property.put("properyAddress",etPropertyAddress.getText().toString());

        property.put("propertyOwnerName",user.getDisplayName());

        property.put("properyOwnerUid",user.getUid());

        // disable button
        btnSubmit.setEnabled(false);

        // change button text
        btnSubmit.setText("Submitting...");
        //

        FirebaseFirestore propertyRef = FirebaseFirestore.getInstance();

        propertyRef.collection("properties")
                .document(propertyRefNumber.substring(propertyRefNumber.length() - 6))
                .set(property)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showSnackBar(e.getMessage());

                        Log.d(TAG, "onFailure: ERROR " + e.getMessage());

                        btnSubmit.setEnabled(true);

                        // change button text
                        btnSubmit.setText("Submit");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                       Intent intent = new Intent(ListPropertyActivity.this, PropertyDetailsActivity.class);

                       intent.putExtra("PROPERTY_ID", propertyRefNumber.substring(propertyRefNumber.length() - 6));

                        startActivity(intent);

                       finish();

                    }
                });

    }

    private void initViews() {

        rootLayout = findViewById(R.id.list_property_rootLayout);

        autocompleteSupportFragment =  (AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.list_my_property_tvAddress);

        etPropertyName = findViewById(R.id.list_property_etName);

        etPropertyAddress = findViewById(R.id.list_my_property_etAddress);

        rbPropertyType = findViewById(R.id.list_my_property_rbType);

        rbApartment = findViewById(R.id.list_my_property_rbApartment);

        rbHouse = findViewById(R.id.list_my_property_rbHouse);

        rbBlockRooms = findViewById(R.id.list_my_property_rbBlockRooms);

    }

    private void initGooglePlaces() {

        // Initialize Places.
        Places.initialize(this, "AIzaSyBVS_Zd197zQOAhmqRgJtEOHx4l5iZ4_k4");

//      Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        autocompleteSupportFragment.setCountry("ZA");

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME,
                Place.Field.LAT_LNG));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                if (place != null){

                    etPropertyAddress.setText(place.getAddress());
                }

            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, "An error occurred: " + status.getStatusMessage());

                showSnackBar(status.getStatusMessage());
            }
        });

    }

    private void showSnackBar(String message) {

        final Snackbar snackbar = Snackbar.make(rootLayout, message, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(rootLayout.getWindowToken(), 0);

        snackbar.show();
    }

}
