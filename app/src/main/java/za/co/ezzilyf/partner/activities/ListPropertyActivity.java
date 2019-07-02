package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.models.Property;
import za.co.ezzilyf.partner.utils.SharedPrefConfig;

public class ListPropertyActivity extends AppCompatActivity {

    private static final String TAG = "ListPropertyActivity";

    private static final int RC_IMAGE_PICKER = 200;

    private static final int RC_PERMISSION = 200;

    private boolean isPropertyTypeSelected = false;

    private EditText etPropertyDescription, etBedrooms, etBathrooms, etToilets;

    private CheckBox cbStudyArea, cbLounge, cbKitchen, cbWifi;

    private TextView tvGreeting;

    private AutocompleteSupportFragment autocompleteSupportFragment;

    private Button btnHouse, btnApartment, btnHostels;

    private TextView etPropertyLocation;

    private CheckBox cbConfirmOwnership;

    private Button btnBack, btnNext, btnSubmit;

    private LinearLayout step1, step2, step3;

    private int currentLayout = 1;

    private int selectedImage = 0;

    private String strKitchen, strLounge, strWifi, strStudyArea, strPropertyType;

    private int intBedrooms, intBathrooms, intToilets;

    private ImageView ivPhoto1, ivPhoto2, ivPhoto3;

    private ProgressDialog progressDialog;

    private CoordinatorLayout rootLayout;

    private ScrollView scrollView;

    private String strImageUrlOne, strImageUrlTwo, strImageUrlThree;

    private StorageReference mStorageReference;

    private String currentUserId;

    private Uri mImageUri;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_property);

        mStorageReference = FirebaseStorage.getInstance().getReference("properties");

        SharedPrefConfig sharedPrefConfig = new SharedPrefConfig(this );

        currentUserId = sharedPrefConfig.readUid();

        rootLayout = findViewById(R.id.list_property_rootLayout);

        scrollView = findViewById(R.id.listing_property_scrollView);

        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Submitting Your Listing");

        progressDialog.setMessage("Please wait while we submitting your listing...");

        progressDialog.setCanceledOnTouchOutside(false);

        initViews();

        setListeners();
    }

    private void initViews() {

        autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.list_property_location_autocomplete);

        step1 = findViewById(R.id.list_property_step1Layout);

        step2 = findViewById(R.id.list_property_step2Layout);

        step3 = findViewById(R.id.list_property_step3Layout);

        btnBack = findViewById(R.id.list_property_btnBack);

        btnNext = findViewById(R.id.list_property_btnNext);

        btnHostels = findViewById(R.id.list_property_Hostels);

        btnHouse = findViewById(R.id.list_property_btnHouse);

        btnSubmit = findViewById(R.id.list_property_btnSubmit);

        btnApartment = findViewById(R.id.list_property_btnApartment);

        etPropertyDescription = findViewById(R.id.list_property_etPropertySummary);

        initGooglePlaces();

        etPropertyLocation = findViewById(R.id.list_property_etPropertyLocation);

        etBedrooms = findViewById(R.id.list_property_etBedrooms);

        etBathrooms = findViewById(R.id.list_property_etBathrooms);

        etToilets = findViewById(R.id.list_property_etToilets);

        cbKitchen = findViewById(R.id.list_property_cbKitchen);

        cbLounge = findViewById(R.id.list_property_cLounge);

        cbStudyArea = findViewById(R.id.list_property_cbStudyArea);

        cbWifi = findViewById(R.id.list_property_cbWifi);

        ivPhoto1 = findViewById(R.id.list_property_ivPhoto1);

        ivPhoto2 = findViewById(R.id.list_property_ivPhoto2);

        ivPhoto3 = findViewById(R.id.list_property_ivPhoto3);

    }

    private void setListeners() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                previousLayoutOut();

                currentLayout--;

            }
        });

        btnApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnApartment.setBackgroundResource(R.drawable.selected_choice_button);

                btnHouse.setBackgroundResource(R.drawable.unselected_choice_button);

                btnHostels.setBackgroundResource(R.drawable.unselected_choice_button);

                isPropertyTypeSelected =true;

                strPropertyType = "Apartment";

            }
        });

        btnHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnApartment.setBackgroundResource(R.drawable.unselected_choice_button);

                btnHouse.setBackgroundResource(R.drawable.selected_choice_button);

                btnHostels.setBackgroundResource(R.drawable.unselected_choice_button);

                isPropertyTypeSelected = true;

                strPropertyType = "House";

            }
        });

        btnHostels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnApartment.setBackgroundResource(R.drawable.unselected_choice_button);

                btnHouse.setBackgroundResource(R.drawable.unselected_choice_button);

                btnHostels.setBackgroundResource(R.drawable.selected_choice_button);

                isPropertyTypeSelected = true;

                strPropertyType = "Hostels";

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLayout++;

                nextLayoutOut();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(ListPropertyActivity.this);

                builder.setTitle("Submit Listing");

                builder.setMessage("Your listing is ready for submission. Continue to submit?");

                builder.setCancelable(false);

                // add the buttons
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        submitListing();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        cbKitchen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    strKitchen = "Yes";

                }else {

                    strKitchen = "No";
                }

            }
        });

        cbStudyArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    strStudyArea = "Yes";

                }else {

                    strStudyArea = "No";
                }

            }
        });

        cbLounge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    strLounge = "Yes";

                }else {

                    strLounge = "No";
                }

            }
        });

        cbWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    strWifi = "Yes";

                }else {

                    strWifi = "No";
                }

            }
        });

        ivPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedImage = 1;

                checkPermission();
            }
        });

        ivPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedImage = 2;

                checkPermission();
            }
        });

        ivPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedImage = 3;

                checkPermission();
            }
        });

    }

    // save listing details to Firebase("properties")
    private void submitListing() {

        progressDialog.show();

        String listingId = "EL" + Calendar.getInstance().getTimeInMillis();

        final Property property = new Property();

        // set property details
        property.setPropertyId(listingId);

        property.setPropertyListerUid(currentUserId);

        property.setPropertyDescription(etPropertyDescription.getText().toString().trim());

        property.setPropertyLocation(etPropertyLocation.getText().toString().trim());

        property.setPropertyType(strPropertyType);

        property.setIsKitchenAvailable(strKitchen);

        property.setIsLoungeAvailable(strLounge);

        property.setIsStudyAreaAvailable(strStudyArea);

        property.setIsWifiAvailable(strWifi);

        property.setNumberOfBedrooms(Integer.parseInt(etBedrooms.getText().toString().trim()));

        property.setNumberOfBathrooms(Integer.parseInt(etBathrooms.getText().toString().trim()));

        property.setNumberOfToilets(Integer.parseInt(etToilets.getText().toString().trim()));

        property.setPhotoOneUrl(strImageUrlOne);

        property.setPhotoTwoUrl(strImageUrlTwo);

        property.setPhotoThreeUrl(strImageUrlThree);

        property.setListingStatus("Pending For Approval");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("properties")
                .document(listingId)
                .set(property)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();

                        showSnackBar(e.getMessage());

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();

                        Toast.makeText(ListPropertyActivity.this, "Property Listing Submitted Successfully", Toast.LENGTH_LONG).show();

                        finish();

                    }
                });

    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {

                // permission not granted. ask for it
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                // show pop up
                requestPermissions(permissions,RC_PERMISSION);

            }else {

                // permission already granted
                pickImageFromGallery();
            }

        }else{
            // device less then mashmallow

            pickImageFromGallery();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case  RC_PERMISSION :

                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    pickImageFromGallery();

                }else{

                    // permission denied
                    showSnackBar("Permission Denied...!");
                }
        }
    }

    private void pickImageFromGallery() {

        // intent to pick image
        Intent intent  =   new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        startActivityForResult(intent, RC_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == RC_IMAGE_PICKER) {

            switch (selectedImage) {

                case 1:

                    ivPhoto1.setImageURI(data.getData());

                    mImageUri = data.getData();

                    uploadImageToFirebaseOne();

                    break;

                case 2:

                    ivPhoto2.setImageURI(data.getData());

                    mImageUri = data.getData();

                    uploadImageToFirebaseTwo();

                    break;

                case 3:

                    ivPhoto3.setImageURI(data.getData());

                    mImageUri = data.getData();

                    uploadImageToFirebaseThree();

                    break;
            }

        }
    }

    private void nextLayoutOut() {

        switch (currentLayout) {

            case 1:

//                step1.setVisibility(View.GONE);
//
//                step2.setVisibility(View.VISIBLE);
//
//                step3.setVisibility(View.GONE);
//
//                btnBack.setVisibility(View.VISIBLE);
//
//                btnNext.setVisibility(View.VISIBLE);
//
//
//                break;

            case 2:

                if (TextUtils.isEmpty(etPropertyDescription.getText())) {

                    etPropertyDescription.setError("Field cannot be empty!");

                    etPropertyDescription.requestFocus();

                    currentLayout--;

                    return;

                }

                if (etPropertyDescription.getText().length() < 50) {

                    etPropertyDescription.setError("Description is too short, minimum of 50 characters required!");

                    etPropertyDescription.requestFocus();

                    currentLayout--;

                    return;

                }

                if (!isPropertyTypeSelected) {

                    Toast.makeText(this, "Please selected property type!", Toast.LENGTH_SHORT).show();

                    currentLayout--;

                    return;

                }

                if (TextUtils.isEmpty(etPropertyLocation.getText())) {

                    Toast.makeText(this, "Please select property location", Toast.LENGTH_SHORT).show();

                    currentLayout--;

                    return;
                }


                step1.setVisibility(View.GONE);

                step2.setVisibility(View.VISIBLE);

                scrollView.setVisibility(View.VISIBLE);

                step3.setVisibility(View.GONE);

                btnBack.setVisibility(View.VISIBLE);

                btnNext.setVisibility(View.VISIBLE);

                break;

            case 3:

                if (TextUtils.isEmpty(etBedrooms.getText())) {

                    etBedrooms.setError("Enter number of bedrooms");

                    etBedrooms.requestFocus();

                    currentLayout--;

                    return;

                }

                if (TextUtils.isEmpty(etBathrooms.getText())) {

                    etBathrooms.setError("Enter number of bathrooms");

                    etBathrooms.requestFocus();

                    currentLayout--;

                    return;

                }

                if (TextUtils.isEmpty(etToilets.getText())) {

                    etToilets.setError("Enter number of toilets");

                    etToilets.requestFocus();

                    currentLayout--;

                    return;

                }


                step1.setVisibility(View.GONE);

                step2.setVisibility(View.GONE);

                step3.setVisibility(View.VISIBLE);

                btnBack.setVisibility(View.VISIBLE);

                btnNext.setVisibility(View.GONE);

                btnSubmit.setVisibility(View.VISIBLE);

                break;
        }

    }

    private void previousLayoutOut() {

        switch (currentLayout) {

            case 3:

                step1.setVisibility(View.GONE);

                step2.setVisibility(View.VISIBLE);

                step3.setVisibility(View.GONE);

                btnBack.setVisibility(View.VISIBLE);

                btnNext.setVisibility(View.VISIBLE);

                btnSubmit.setVisibility(View.GONE);


                break;

            case 2:

                step1.setVisibility(View.VISIBLE);

                step2.setVisibility(View.GONE);

                scrollView.setVisibility(View.GONE);

                step3.setVisibility(View.GONE);

                btnBack.setVisibility(View.GONE);

                btnNext.setVisibility(View.VISIBLE);

                break;

            case 1:

                btnBack.setVisibility(View.GONE);

                btnNext.setVisibility(View.VISIBLE);

                break;
        }

    }

    private void initGooglePlaces() {

        // Initialize Places.
        Places.initialize(this, "AIzaSyBVS_Zd197zQOAhmqRgJtEOHx4l5iZ4_k4");

//      Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);


        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME,
                Place.Field.LAT_LNG));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                if (place != null){
                   // mLocation = place.getAddress();
                   // mLatitude = place.getLatLng().latitude;
                   // mLongitude = place.getLatLng().longitude;
                    etPropertyLocation.setText(place.getAddress());
                }

            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, "An error occurred: " + status.getStatusMessage());
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

    private void uploadImageToFirebaseOne() {

        final StorageReference fileReference =
                mStorageReference.child(currentUserId + "_photo1.jpg");

        UploadTask uploadTask = fileReference.putFile(mImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                showSnackBar(e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        strImageUrlOne = uri.toString();

                        Toast.makeText(ListPropertyActivity.this, "Image 1, uploaded successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showSnackBar(e.getMessage());

                    }
                });

            }
        });

    }

    private void uploadImageToFirebaseTwo() {

        final StorageReference fileReference =
                mStorageReference.child(currentUserId + "_photo2.jpg");

        UploadTask uploadTask = fileReference.putFile(mImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                showSnackBar(e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        strImageUrlTwo = uri.toString();

                        Toast.makeText(ListPropertyActivity.this, "Image 2, uploaded successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showSnackBar(e.getMessage());

                    }
                });

            }
        });

    }

    private void uploadImageToFirebaseThree() {

        final StorageReference fileReference =
                mStorageReference.child(currentUserId + "_photo3.jpg");

        UploadTask uploadTask = fileReference.putFile(mImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                showSnackBar(e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        strImageUrlThree = uri.toString();

                        Toast.makeText(ListPropertyActivity.this, "Image 3, uploaded successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showSnackBar(e.getMessage());

                    }
                });

            }
        });

    }
}
