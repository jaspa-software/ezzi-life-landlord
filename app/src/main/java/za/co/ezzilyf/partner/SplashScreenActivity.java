package za.co.ezzilyf.partner;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        ImageView logo = findViewById(R.id.splash_ivLogo);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.animation);

        logo.startAnimation(animation);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                if (firebaseAuth.getCurrentUser() !=null){

                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);

                    startActivity(i);

                    finish();
                }else{

                    Intent i = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);

                    startActivity(i);

                    finish();
                }
            }
        }, 5000);

    }
}
