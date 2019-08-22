package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.adapters.PropertyPhotosAdapter;
import za.co.ezzilyf.partner.models.Photo;

public class PropertyPhotosActivity extends AppCompatActivity {

    private static final String TAG = "PropertyPhotosActivity";

    private static final int RC_PERMISSION = 200;

    private RecyclerView recyclerView;

    private TextView textView;

    private Uri mImageUri;

    private PropertyPhotosAdapter photosAdapter;

    private StorageReference mStorageReference;

    private ProgressDialog progressDialog;

    private String propertyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_property_photos);

        recyclerView = findViewById(R.id.property_photos_recyclerView);

        propertyId = getIntent().getStringExtra("PROPERTY_ID");

        textView = findViewById(R.id.property_photos_message);

        mStorageReference = FirebaseStorage.getInstance().getReference("propertyImages");

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        recyclerView.setHasFixedSize(true);

        if (propertyId !=null) {

            getPropertyPhotos();

        }


        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Uploading Image");

        progressDialog.setMessage("Please wait...");

        progressDialog.setCanceledOnTouchOutside(false);


        Button btnUploadPhoto = findViewById(R.id.property_photos_btnUploadPhoto);

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission();

            }
        });

        ImageView btnClose = findViewById(R.id.property_photos_btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

    private void getPropertyPhotos() {

        textView.setText("Please wait....");

        FirebaseFirestore photosRef = FirebaseFirestore.getInstance();

        photosRef.collection("photos")
                .document(propertyId)
                .collection("propertyPhotos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e !=null) {

                            textView.setText(e.getMessage());

                            recyclerView.setVisibility(View.GONE);

                            textView.setVisibility(View.VISIBLE);

                            return;
                        }


                        if(queryDocumentSnapshots !=null && !queryDocumentSnapshots.isEmpty()) {

                            List<Photo> photoList = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                                Photo photo = doc.toObject(Photo.class);

                                photoList.add(photo);

                            }

                            photosAdapter = new PropertyPhotosAdapter(photoList, PropertyPhotosActivity.this);

                            recyclerView.setAdapter(photosAdapter);

                            photosAdapter.notifyDataSetChanged();

                        }else {
                            textView.setText("No property images added");

                            recyclerView.setVisibility(View.GONE);

                            textView.setVisibility(View.VISIBLE);

                        }
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

                uploadImageToFirebase();


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void uploadImageToFirebase() {

        progressDialog.show();

        String photoId = Calendar.getInstance().getTimeInMillis()+"";

        final StorageReference fileReference =
                mStorageReference.child(photoId + ".jpg");

        UploadTask uploadTask = fileReference.putFile(mImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();

                Toast.makeText(PropertyPhotosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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

                        progressDialog.dismiss();

                        Toast.makeText(PropertyPhotosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

    private void addImage(Uri uri) {

        HashMap<String,Object> photo = new HashMap<>();

        photo.put("url", uri.toString());

        FirebaseFirestore photosRef = FirebaseFirestore.getInstance();

        photosRef.collection("photos")
                .document("077990")
                .collection("propertyPhotos")
                .add(photo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        progressDialog.dismiss();

                        Toast.makeText(PropertyPhotosActivity.this, "Photo Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();

                        Toast.makeText(PropertyPhotosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
