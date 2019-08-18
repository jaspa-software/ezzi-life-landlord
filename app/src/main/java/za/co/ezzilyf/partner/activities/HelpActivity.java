package za.co.ezzilyf.partner.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.R;

public class HelpActivity extends AppCompatActivity {

    private static final String TAG = "HelpActivity";

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = findViewById(R.id.help_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Help");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        CardView privacyPolicy = findViewById(R.id.help_privacyPolicy);

        CardView termsConditions = findViewById(R.id.help_termsAndConditions);

        CardView dataUsage = findViewById(R.id.help_dataUsagePolicy);

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(HelpActivity.this, "Will link to website Privacy Policy Page", Toast.LENGTH_SHORT).show();
            }
        });

        termsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(HelpActivity.this, "Will link to website Terms and Conditions Page", Toast.LENGTH_SHORT).show();
            }
        });

        dataUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(HelpActivity.this, "Will link to website Data Usage Page", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
