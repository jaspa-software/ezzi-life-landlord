package za.co.ezzilyf.partner;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import za.co.ezzilyf.partner.activities.StudentHomeActivity;
import za.co.ezzilyf.partner.activities.WelcomeActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser ==null) {
            Intent intent = new Intent(this, WelcomeActivity.class);

            startActivity(intent);

            finish();
        }else {

            Intent intent = new Intent(this, WelcomeActivity.class);

            // TODO check auth ID and compare proper screen

            startActivity(intent);

            finish();

        }

    }
}
