package za.co.ezzilyf.partner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.AuthenticationActivity;
import za.co.ezzilyf.partner.R;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        Button btnListMyPlace = findViewById(R.id.welcome_btnListPlace);

        Button btnSearchForPlace = findViewById(R.id.welcome_btnSearchPlace);

        btnListMyPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeActivity.this, AuthenticationActivity.class);

                intent.putExtra("TYPE", "PARTNER");

                startActivity(intent);

                finish();

            }
        });

        btnSearchForPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeActivity.this, AuthenticationActivity.class);

                intent.putExtra("TYPE", "STUDENT");

                startActivity(intent);

                finish();

            }
        });


    }
}
