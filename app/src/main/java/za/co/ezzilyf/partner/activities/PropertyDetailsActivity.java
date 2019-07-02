package za.co.ezzilyf.partner.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.models.Property;

public class PropertyDetailsActivity extends AppCompatActivity {

    private TextView tvStudy, tvLounge, tvKitchen, tvWifi, tvBedroom, tvBathroom, tvToilet;

    private TextView etAddress;

    private TextView tvStatus, tvComments, tvEvaluationDate;

    private Button btnAddRoom;

    private String strListingStatus;

    private static final String TAG = "PropertyDetailsActivity";

    private Property property;

    private ImageView ivPhotoOne, ivPhotoTwo, ivPhotoThree;

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

        getSupportActionBar().setTitle("Property Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        property = (Property) getIntent().getSerializableExtra("PROPERTY");

        if (property !=null) {

            strListingStatus = property.getListingStatus();

        }

        initViews();

        setListeners();
    }

    private void initViews() {

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

        btnAddRoom = findViewById(R.id.property_details_btnAddRoom);

        ivPhotoOne = findViewById(R.id.property_details_ivPhoto1);

        ivPhotoTwo = findViewById(R.id.property_details_ivPhoto2);

        ivPhotoThree = findViewById(R.id.property_details_ivPhoto3);

        setValues();
    }

    private void setValues() {

        tvStatus.setText(property.getListingStatus());

        tvLounge.setText(property.getIsLoungeAvailable());

        tvKitchen.setText(property.getIsKitchenAvailable());

        tvWifi.setText(property.getIsWifiAvailable());

        tvBedroom.setText(property.getNumberOfBedrooms()+"");

        tvBathroom.setText(property.getNumberOfBathrooms()+"");

        tvToilet.setText(property.getNumberOfToilets()+"");

        etAddress.setText(property.getPropertyLocation());

        if (TextUtils.equals(strListingStatus,"Pending For Approval")) {

            // TODO uncomment this
            //  btnAddRoom.setVisibility(View.GONE);

            tvComments.setText("Waiting for approval");

            tvEvaluationDate.setText("N/A");
        }

        Picasso.get().load(property.getPhotoOneUrl()).into(ivPhotoOne);

        Picasso.get().load(property.getPhotoTwoUrl()).into(ivPhotoTwo);

        Picasso.get().load(property.getPhotoThreeUrl()).into(ivPhotoThree);

    }

    private  void setListeners(){

        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PropertyDetailsActivity.this, AddRoomActivity.class);

                intent.putExtra("PROPERTY", property);

                startActivity(intent);

            }
        });
    }
}
