package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.models.Landlord;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private static final int RC_PERMISSION = 200;

    private TextInputLayout etDisplayName;

    private TextInputLayout etEmail;

    private TextInputLayout etPhoneNumber;

    private String photoUrl;

    private Uri mImageUri;

    private StorageReference mStorageReference;

    private CircleImageView ivProfileImage;

    private String currentUserId = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        mStorageReference = FirebaseStorage.getInstance().getReference("profile_pics");

        ImageView btnClose = findViewById(R.id.profile_ivClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        initViews();

        Button btnUpdate = findViewById(R.id.profile_btnUpdate);
        
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                updateProfile();
            }
        });

        fetchProfile();
    }

    private void updateProfile() {

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Updating Profile");

        progressDialog.setMessage("Please wait....");

        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();

        HashMap<String, Object> userDetails = new HashMap<>();

        userDetails.put("displayName",etDisplayName.getEditText().getText().toString() );

        userDetails.put("emailAddress",etEmail.getEditText().getText().toString() );

        userDetails.put("contactNumber",etPhoneNumber.getEditText().getText().toString() );

        userDetails.put("photoUrl",photoUrl );

        FirebaseFirestore userRef = FirebaseFirestore.getInstance();

        userRef.collection("landlords")
                .document(currentUserId)
                .set(userDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();

                        Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();

                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void fetchProfile() {

        FirebaseFirestore userRef = FirebaseFirestore.getInstance();

        userRef.collection("landlords")
                .document(currentUserId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if ( e !=null) {

                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            return;
                        }


                        if (documentSnapshot !=null && documentSnapshot.exists()) {

                            Landlord landlord = documentSnapshot.toObject(Landlord.class);

                            if (landlord !=null) {

                                etDisplayName.getEditText().setText(landlord.getDisplayName());

                                etEmail.getEditText().setText(landlord.getEmailAddress());

                                etPhoneNumber.getEditText().setText(landlord.getContactNumber());

                                photoUrl = landlord.getPhotoUrl();

                                if (photoUrl  == null || TextUtils.isEmpty(photoUrl)) {

                                    return;
                                }

                                Glide
                                        .with(ProfileActivity.this)
                                        .load(photoUrl)
                                        .centerCrop()
                                        .placeholder(R.drawable.spinner)
                                        .into(ivProfileImage);

                            }
                        }

                    }
                });

    }

    private void initViews() {

        etDisplayName = findViewById(R.id.profile_etFullNames);

        etEmail = findViewById(R.id.profile_etEmail);

        etPhoneNumber = findViewById(R.id.profile_etContactNumber);

        ivProfileImage = findViewById(R.id.profile_profilePic);

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission();

            }
        });

    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {

                // permission not granted. ask for it
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                // show pop up
                requestPermissions(permissions,RC_PERMISSION);

            }else {

                // permission already granted
                pickImageFromGallery();
            }

        }else{
            // device less then mashmallow

            pickImageFromGallery();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case  RC_PERMISSION :

                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    pickImageFromGallery();

                }else{

                    // permission denied
                    //showSnackBar("Permission Denied...!");
                }
        }
    }

    private void pickImageFromGallery() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,3)
                .setMinCropResultSize(400,400)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mImageUri= result.getUri();

                Glide
                        .with(ProfileActivity.this)
                        .load(mImageUri)
                        .centerCrop()
                        .placeholder(R.drawable.spinner)
                        .into(ivProfileImage);

                uploadImageToFirebase();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void uploadImageToFirebase() {

        //progressDialog.show();


        final StorageReference fileReference =
                mStorageReference.child(currentUserId + ".jpg");

        UploadTask uploadTask = fileReference.putFile(mImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

               // progressDialog.dismiss();

                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        addImage(uri);



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                      //  progressDialog.dismiss();

                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

    private void addImage(Uri uri) {

        HashMap<String,Object> photo = new HashMap<>();

        photo.put("photoUrl", uri.toString());

        FirebaseFirestore photosRef = FirebaseFirestore.getInstance();

        photosRef.collection("landlords")
                .document(currentUserId)
                .update(photo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(ProfileActivity.this, "Photo Uploaded Successfully", Toast.LENGTH_SHORT).show();

                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                       // progressDialog.dismiss();

                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
