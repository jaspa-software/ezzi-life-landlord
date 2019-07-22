package za.co.ezzilyf.partner;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.activities.StudentHomeActivity;
import za.co.ezzilyf.partner.models.User;
import za.co.ezzilyf.partner.utils.SharedPrefConfig;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = "AuthenticationActivity";

    private static final int RC_SIGN_IN = 1234;

    private FirebaseAuth mAuth;

    private CoordinatorLayout rootLayout;

    private String strType;


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

       // setContentView(R.layout.activity_authentication);

        rootLayout = findViewById(R.id.authentication_rootLayout);

        strType = getIntent().getStringExtra("TYPE").trim();

        createSignInIntent();

    }

    public void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                       .setLogo(R.drawable.ezzilyf)      // Set logo drawable
                        .setTheme(R.style.AppTheme)
                        .setTosAndPrivacyPolicyUrls(
                                "https://www.ezzilife.co.za/terms-of-services.html",
                                "https://www.ezzilife.co.za/privacy-policy.html")
                        .build(),
                RC_SIGN_IN);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // create user
                User newUser = new User();

                newUser.setDisplayName(user.getDisplayName());

                newUser.setEmailAddress(user.getEmail());

               // newUser.setPhotoUrl(user.getPhotoUrl().toString());

                newUser.setType(strType);

                newUser.setUid(user.getUid());

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore.collection("users")
                        .document(user.getUid())
                        .set(newUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                if (TextUtils.equals(strType,"PARTNER")) {
                                    Intent i = new Intent(AuthenticationActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }else if (TextUtils.equals(strType,"STUDENT")) {
                                    Intent i = new Intent(AuthenticationActivity.this, StudentHomeActivity.class);
                                    startActivity(i);
                                    finish();
                                }


                                updateSharedPref(user);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(AuthenticationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


            } else {

                Toast.makeText(this, response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                //
                // Snackbars snackbars = new Snackbar(rootLayout,)
            }
        }
    }


    private void updateSharedPref(FirebaseUser user) {

        SharedPrefConfig sharedPrefConfig = new SharedPrefConfig(this);

        sharedPrefConfig.writeUserProfile(
                user.getUid(),user.getDisplayName(),strType,user.getEmail(),user.getPhoneNumber()
        );
    }
}
