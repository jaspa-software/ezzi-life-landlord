package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.adapters.SlidingImageAdapter;
import za.co.ezzilyf.partner.models.Property;

public class PropertyDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PropertyDetailsActivity";

    private String propertyId;

    private Property amenity;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_property_details);

        Toolbar toolbar = findViewById(R.id.property_details_toolbar);

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

            checkAmenities();

            checkCampuses();

            checkPhotos();
        }

    }

    private void checkAmenities() {

        final FirebaseFirestore amenitiesRef = FirebaseFirestore.getInstance();

        final TextView amenitiesStatus = findViewById(R.id.property_details_tvAmenitiesStatus);

        amenitiesRef.collection("properties")
                .document(propertyId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (e !=null) {

                            amenitiesStatus.setVisibility(View.VISIBLE);

                            amenitiesStatus.setText(e.getMessage());

                            amenitiesStatus.setTextColor(Color.RED);

                            return;
                        }

                        if (documentSnapshot.exists()) {

                            amenity = documentSnapshot.toObject(Property.class);

                            if (amenity !=null) {

                                if ( amenity.getStudyArea() == null ||
                                        amenity.getWardrobes() == null ||
                                        amenity.getWifi() == null ||
                                        amenity.getCookingArea() == null ||
                                        amenity.getLounge() == null ||
                                        amenity.getLaundry()== null) {

                                    amenitiesStatus.setTextColor(Color.RED);

                                    amenitiesStatus.setText("Property Amenities not added!");

                                    amenitiesStatus.setVisibility(View.VISIBLE);

                                }else{

                                    amenitiesStatus.setVisibility(View.GONE);
                                }
                            }else {

                                amenitiesStatus.setTextColor(Color.YELLOW);

                                amenitiesStatus.setText("Could not load amenities!");

                                amenitiesStatus.setVisibility(View.VISIBLE);
                            }


                        }


                    }
                });
    }

    private void checkCampuses() {

        final FirebaseFirestore amenitiesRef = FirebaseFirestore.getInstance();

        final TextView insititutionStatus = findViewById(R.id.property_details_tvInstitutionsStatus);

        amenitiesRef.collection("nearByInstitutions")
                .whereEqualTo("propertyId", propertyId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e !=null) {

                            insititutionStatus.setVisibility(View.VISIBLE);

                            insititutionStatus.setText(e.getMessage());

                            insititutionStatus.setTextColor(Color.RED);

                            return;
                        }

                        if (queryDocumentSnapshots !=null && queryDocumentSnapshots.size() > 0) {

                            insititutionStatus.setVisibility(View.VISIBLE);

                            insititutionStatus.setText(queryDocumentSnapshots.size() + " campuses added");

                            insititutionStatus.setTextColor(Color.BLUE);

                        }else{

                            insititutionStatus.setTextColor(Color.RED);

                            insititutionStatus.setText("No near by campuses added!");

                            insititutionStatus.setVisibility(View.VISIBLE);

                        }
                    }
                });
    }

    private void checkPhotos() {

        final FirebaseFirestore photosRef = FirebaseFirestore.getInstance();

        final TextView photosStatus = findViewById(R.id.property_details_tvPhotosStatus);

        photosRef.collection("photos")
                .document(propertyId)
                .collection("propertyPhotos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e !=null) {

                            photosStatus.setVisibility(View.VISIBLE);

                            photosStatus.setText(e.getMessage());

                            photosStatus.setTextColor(Color.RED);

                            return;
                        }

                        if (queryDocumentSnapshots !=null && queryDocumentSnapshots.size() > 0) {

                            photosStatus.setTextColor(Color.BLUE);

                            photosStatus.setText(queryDocumentSnapshots.size() + " photos uploaded");

                            photosStatus.setVisibility(View.VISIBLE);

                        }else{

                            photosStatus.setTextColor(Color.RED);

                            photosStatus.setText("No photos uploaded");

                            photosStatus.setVisibility(View.VISIBLE);

                        }

                    }
                });
    }

    private void initViews() {

        final TextView tvPropertyName = findViewById(R.id.property_details_tvPropertyName);

        final TextView tvPropertyId= findViewById(R.id.property_details_tvPropertyId);

        final TextView tvPropertyType = findViewById(R.id.property_details_tvPropertyType);

        final TextView tvPropertyAddress = findViewById(R.id.property_details_tvPropertyAddress);

        final TextView tvMessage = findViewById(R.id.property_details_message);

        FirebaseFirestore propertyRef = FirebaseFirestore.getInstance();

        propertyRef.collection("properties")
                .document(propertyId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            Property property = documentSnapshot.toObject(Property.class);

                            if (property == null) {

                                return;
                            }

                            tvPropertyName.setText(property.getPropertyName());

                            tvPropertyId.setText(property.getPropertyRefNumber());

                            tvPropertyType.setText(property.getPropertyType());

                            tvPropertyAddress.setText(property.getProperyAddress());

                            getSupportActionBar().setTitle(property.getPropertyName() + " - " + property.getPropertyRefNumber());

                        }else{

                            tvMessage.setText("Couldn't connect to server at this time");

                            tvMessage.setTextColor(Color.RED);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        tvMessage.setText(e.getMessage());

                        tvMessage.setTextColor(Color.RED);
                    }
                });


        CardView cvAmenities = findViewById(R.id.property_details_amenitiesLayout);

        cvAmenities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PropertyDetailsActivity.this, AmenitiesActivity.class);

                intent.putExtra("PROPERTY_ID",propertyId);

                startActivity(intent);

            }
        });

        CardView cvInstitutions = findViewById(R.id.property_details_institutionsLayout);

        cvInstitutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PropertyDetailsActivity.this, CampusesNearByActivity.class);

                intent.putExtra("PROPERTY",amenity);

                startActivity(intent);

            }
        });


        CardView cvPhotos = findViewById(R.id.property_details_photosLayout);

        cvPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PropertyDetailsActivity.this, PropertyPhotosActivity.class);

                intent.putExtra("PROPERTY",propertyId);

                startActivity(intent);

            }
        });

    }


}
