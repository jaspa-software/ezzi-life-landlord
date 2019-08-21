package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        propertyId = getIntent().getStringExtra("PROPERTY_ID");

        if (propertyId !=null) {

            initViews();

            getAmenities();

            setListeners();
        }

        ImageView btnCloseButton = findViewById(R.id.amenities_btnClose);

        btnCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

    private void setListeners() {

        cbWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("wifi","Yes");
                }else{
                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("wifi","No");
                }
            }
        });

        cbStudy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("studyArea","Yes");
                }else{
                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("studyArea","No");
                }

            }
        });

        cbWardrobe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("wardrobes","Yes");
                }else{
                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("wardrobes","No");
                }

            }
        });

        cbParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("parking","Yes");
                }else{
                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("parking","No");
                }

            }
        });

        cbCookingArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("cookingArea","Yes");
                }else{
                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("cookingArea","No");
                }

            }
        });

        cbLounge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("lounge","Yes");
                }else{
                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("lounge","No");
                }

            }
        });

        cbLaundry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                if (isChecked) {

                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("laundry","Yes");
                }else{
                    firebaseFirestore.collection("properties")
                            .document(propertyId)
                            .update("laundry","No");
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


        amenitiesRef.collection("properties")
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

                        Toast.makeText(AmenitiesActivity.this, "Updating ammenties", Toast.LENGTH_SHORT).show();

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
