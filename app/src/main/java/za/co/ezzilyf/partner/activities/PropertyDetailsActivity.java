package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.adapters.SlidingImageAdapter;
import za.co.ezzilyf.partner.models.Property;

public class PropertyDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView tvStudy, tvLounge, tvKitchen, tvWifi, tvBedroom, tvBathroom, tvToilet;

    private TextView etAddress;

    private TextView tvStatus, tvComments, tvEvaluationDate, tvInspectionDateTime;

    private Button btnAddRoom;

    private String strListingStatus, strInspectionDate, strInspectionTime;

    private static final String TAG = "PropertyDetailsActivity";

    private Property property;


    private EditText etDescription;

    private String[] urls;

    private static ViewPager mPager;

    private static int currentPage = 0;

    private static int NUM_PAGES = 0;

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

        final ProgressDialog progressDialog = new ProgressDialog(this);

        property = (Property) getIntent().getSerializableExtra("PROPERTY");

        btnAddRoom = findViewById(R.id.property_details_btnAddRoom);

        if (property !=null) {

            strListingStatus = property.getListingStatus();

            urls = new String [] {property.getPhotoOneUrl(), property.getPhotoTwoUrl(), property.getPhotoThreeUrl()};

            getSupportActionBar().setTitle(property.getPropertyName() + " - " + property.getPropertyId());

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Button btnAcceptDate = findViewById(R.id.property_details_btnAcceptDate);

            Button btnReschedulDate = findViewById(R.id.property_details_btnRescheduleDate);

            btnReschedulDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // show date picker
                    setInspectionDate();

                }
            });

            LinearLayout linearLayout = findViewById(R.id.property_detetails_inspection);

            if (TextUtils.equals(property.getListingStatus(),"Confirm Inspection Date")){

                // initialise confirm date widgets
                linearLayout.setVisibility(View.VISIBLE);

                btnAcceptDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                        progressDialog.setMessage("Setting inspection appointment. Please wait....");

                        progressDialog.show();

                        firebaseFirestore.collection("properties")
                                .document(property.getPropertyId())
                                .update("listingStatus","Pending Inspection")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        progressDialog.dismiss();

                                        Toast.makeText(PropertyDetailsActivity.this, "Inspection Appointment Schedule Successfully", Toast.LENGTH_SHORT).show();

                                        finish();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        progressDialog.dismiss();

                                        Toast.makeText(PropertyDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                });
            } else if (TextUtils.equals(property.getListingStatus(),"Pending Inspection")){

                btnAcceptDate.setVisibility(View.GONE);


            }else if (TextUtils.equals(property.getListingStatus(),"Pending for Approval")){

                linearLayout.setVisibility(View.GONE);

            }else if (TextUtils.equals(property.getListingStatus(),"Approved")){

                btnAddRoom.setVisibility(View.VISIBLE); //btnAddRoom

            }

        }

        initViews();

        setListeners();

        initSlider();
    }

    private void initSlider() {

        mPager = findViewById(R.id.property_details_viewPager);
        mPager.setAdapter(new SlidingImageAdapter(PropertyDetailsActivity.this,urls));

        CirclePageIndicator indicator = findViewById(R.id.event_overview_indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = urls.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private void initViews() {

        tvInspectionDateTime = findViewById(R.id.property_details_tvDateTime);

        etDescription = findViewById(R.id.property_details_etDescription);

        tvStatus = findViewById(R.id.property_details_tvStatus);

        tvLounge = findViewById(R.id.property_details_tvLounge);

        tvKitchen = findViewById(R.id.property_details_tvKitchen);

        tvWifi = findViewById(R.id.property_details_tvWifi);

        tvBedroom = findViewById(R.id.property_details_tvBedroom);

        tvBathroom = findViewById(R.id.property_details_tvBathrooms);

        tvToilet = findViewById(R.id.property_details_tvToilets);

        etAddress = findViewById(R.id.property_details_tvLocation);

        tvStatus = findViewById(R.id.property_details_tvStatus);

        tvComments = findViewById(R.id.property_details_tvComments);

        tvEvaluationDate = findViewById(R.id.property_details_tvNextEvaluationDate);

        setValues();
    }

    private void setValues() {

        etDescription.setText(property.getPropertyDescription());

        etDescription.setEnabled(false);

        tvStatus.setText(property.getListingStatus());

        if (TextUtils.equals(strListingStatus,"Pending for Approval")) {

            tvStatus.setBackgroundResource(R.color.colorPending);

        }

        tvLounge.setText(property.getIsLoungeAvailable());

        tvKitchen.setText(property.getIsKitchenAvailable());

        tvWifi.setText(property.getIsWifiAvailable());

        tvBedroom.setText(property.getNumberOfBedrooms()+"");

        tvBathroom.setText(property.getNumberOfBathrooms()+"");

        tvToilet.setText(property.getNumberOfToilets()+"");

        etAddress.setText(property.getPropertyLocation());

        if (TextUtils.equals(strListingStatus,"Pending for Approval")) {

            btnAddRoom.setVisibility(View.GONE);

            // tvComments.setText("Pending for Approval");
            //tvComments.setBackgroundResource(R.color.colorPending);

            tvEvaluationDate.setText("N/A");
        }

    }

    private  void setListeners(){

        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PropertyDetailsActivity.this, AddRoomActivity.class);

                intent.putExtra("PROPERTY", property);

                startActivity(intent);

                finish();

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR,year);

        c.set(Calendar.MONTH,month);

        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        strInspectionDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        setInspectionTime();

    }


    private void setInspectionDate() {

        DialogFragment inspectionDate = new za.co.ezzilyf.partner.utils.DatePicker();

        inspectionDate.show(getSupportFragmentManager(),"EVENT_DATE");
    }


    private void setInspectionTime() {

        DialogFragment eventTime = new za.co.ezzilyf.partner.utils.TimePicker();

        eventTime.show(getSupportFragmentManager(),"EVENT_TIME");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        strInspectionTime = hourOfDay + " : " + minute;

        tvInspectionDateTime.setText( strInspectionDate + ", " + strInspectionTime);

        updateFirebaseWithNewDate();

    }

    private void updateFirebaseWithNewDate() {

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Please Wait...");

        progressDialog.setMessage("Rescheduling Inspection Date and Time...");

        progressDialog.show();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        Map<String, Object> newDateTime = new HashMap<>();

        newDateTime.put("inspectionDate", strInspectionDate);

        newDateTime.put("inspectionTime", strInspectionTime);

        newDateTime.put("listingStatus", "Waiting For Inspector To New Confirm Date and Time");


        firebaseFirestore.collection("properties")
                .document(property.getPropertyId())
                .update(newDateTime)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();

                        Toast.makeText(PropertyDetailsActivity.this, "Date and Time rescheduled. Inspector has been notified", Toast.LENGTH_LONG).show();

                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(PropertyDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

}
