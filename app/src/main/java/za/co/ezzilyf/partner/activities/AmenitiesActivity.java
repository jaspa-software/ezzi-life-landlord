package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.models.Property;

public class AmenitiesActivity extends AppCompatActivity {

    private CheckBox cbWifi, cbStudy, cbWardrobe, cbParking, cbCookingArea, cbLounge, cbLaundry;

    private Property property;

    private LinearLayout rootLayout;

    private String propertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_amenities);

        rootLayout = findViewById(R.id.amenities_rootLayout);

        Toolbar toolbar = findViewById(R.id.ammenities_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        propertyId = getIntent().getStringExtra("PROPERTY_ID");

        if (propertyId !=null) {

            initViews();

            getAmenities();

            setListeners();
        }

    }

    private void setListeners() {

        cbWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("wifi","Yes");
                    Toast.makeText(AmenitiesActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("wifi","No");
                    Toast.makeText(AmenitiesActivity.this, "Removed successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cbStudy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("studyArea","Yes");
                    Toast.makeText(AmenitiesActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("studyArea","No");
                    Toast.makeText(AmenitiesActivity.this, "Removed successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cbWardrobe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("wardrobes","Yes");
                    Toast.makeText(AmenitiesActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("wardrobes","No");
                    Toast.makeText(AmenitiesActivity.this, "Removed successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cbParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("parking","Yes");
                    Toast.makeText(AmenitiesActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("parking","No");
                    Toast.makeText(AmenitiesActivity.this, "Removed successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cbCookingArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("cookingArea","Yes");
                    Toast.makeText(AmenitiesActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("cookingArea","No");
                    Toast.makeText(AmenitiesActivity.this, "Removed successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cbLounge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("lounge","Yes");
                    Toast.makeText(AmenitiesActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("lounge","No");
                    Toast.makeText(AmenitiesActivity.this, "Removed successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cbLaundry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("laundry","Yes");
                    Toast.makeText(AmenitiesActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseFirestore.collection("residences")
                            .document(propertyId)
                            .update("laundry","No");
                    Toast.makeText(AmenitiesActivity.this, "Removed successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initViews() {

        cbWifi = findViewById(R.id.amenities_cbWifi);

        cbStudy = findViewById(R.id.amenities_cbStudyDesks);

        cbWardrobe = findViewById(R.id.amenities_cbWadrobes);

        cbParking = findViewById(R.id.amenities_cbParking);

        cbCookingArea = findViewById(R.id.amenities_cbCookingArea);

        cbLounge = findViewById(R.id.amenities_cbLounge);

        cbLaundry = findViewById(R.id.amenities_cbLaundry);

    }

    private void getAmenities() {

        FirebaseFirestore amenitiesRef = FirebaseFirestore.getInstance();


        amenitiesRef.collection("residences")
                .document(propertyId)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showSnackBar(e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        property = documentSnapshot.toObject(Property.class);

                        updateCheckBoxes();
                    }
                });

    }

    private void updateCheckBoxes() {

        if (property.getCookingArea() !=null && TextUtils.equals(property.getCookingArea().trim(), "Yes")) {

            cbCookingArea.setChecked(true);

        }else{

            cbCookingArea.setChecked(false);
        }

        if (property.getLaundry() !=null && TextUtils.equals(property.getLaundry().trim(), "Yes")) {

            cbLaundry.setChecked(true);

        }else{

            cbLaundry.setChecked(false);
        }


        if (property.getStudyArea() !=null && TextUtils.equals(property.getStudyArea().trim(), "Yes")) {

            cbStudy.setChecked(true);

        }else{

            cbStudy.setChecked(false);
        }


        if (property.getLounge() !=null && TextUtils.equals(property.getLounge().trim(), "Yes")) {

            cbLounge.setChecked(true);

        }else{

            cbLounge.setChecked(false);
        }

        if (property.getParking() !=null && TextUtils.equals(property.getParking().trim(), "Yes")) {

            cbParking.setChecked(true);

        }else{

            cbParking.setChecked(false);
        }

        if (property.getWifi() !=null && TextUtils.equals(property.getWifi().trim(), "Yes")) {

            cbWifi.setChecked(true);

        }else{

            cbWifi.setChecked(false);
        }

        if (property.getWardrobes() !=null &&TextUtils.equals(property.getWardrobes().trim(), "Yes")) {

            cbWardrobe.setChecked(true);

        }else{

            cbWardrobe.setChecked(false);
        }
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
