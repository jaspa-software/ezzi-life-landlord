package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.models.Property;
import za.co.ezzilyf.partner.models.Room;

public class AddRoomActivity extends AppCompatActivity {

    private static final String TAG = "AddRoomActivity";

    private static final int RC_IMAGE_PICKER = 200;

    private static final int RC_PERMISSION = 200;

    private RadioGroup rgOccupantsType, rgRoomType;

    private EditText etMonthlyRental, etRoomDescription, etOccupants;

    private int selectedImage = 0;

    private TextView tvRoomId;

    private ImageView ivRoomImage1, ivRoomImage2, ivRoomImage3;

    private Button btnAddRoom;

    private RadioButton rbOccupants, rbRoomType;

    private CoordinatorLayout rootLayout;

    private boolean isImageOneUploaded, isImageTwoUploaded, isImageThreeUploaded;

    private Property property;

    private String roomId = "R" + Calendar.getInstance().getTimeInMillis();



    private String strImageUrlOne, strImageUrlTwo, strImageUrlThree;

    private StorageReference mStorageReference;

    private String currentUserId;

    private Uri mImageUri;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_room);

        mStorageReference = FirebaseStorage.getInstance().getReference("rooms");

        Toolbar toolbar = findViewById(R.id.add_room_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add Room");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        property = (Property) getIntent().getSerializableExtra("PROPERTY");


        rootLayout = findViewById(R.id.add_room_rootLayout);

        isImageOneUploaded = false;
        isImageTwoUploaded = false;
        isImageThreeUploaded = false;

        initViews();

        setListeners();
    }

    private void setListeners() {

        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateInput();

            }
        });

        ivRoomImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedImage = 1;

                checkPermission();
            }
        });

        ivRoomImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedImage = 2;

                checkPermission();

            }
        });

        ivRoomImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedImage = 3;

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
                    showSnackBar("Permission Denied...!");
                }
        }
    }

    private void pickImageFromGallery() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,3)
                .setMinCropResultSize(600,400)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                switch (selectedImage) {

                    case 1:

                        ivRoomImage1.setImageURI(resultUri);

                        mImageUri = resultUri;

                        uploadImageToFirebaseOne();

                        break;

                    case 2:

                        ivRoomImage2.setImageURI(resultUri);

                        mImageUri = resultUri;

                        uploadImageToFirebaseTwo();

                        break;

                    case 3:

                        ivRoomImage3.setImageURI(resultUri);

                        mImageUri = resultUri;

                        uploadImageToFirebaseThree();

                        break;
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void validateInput() {

        // check if room type has been selected
        int selectedRoomTypeTypeId = rgRoomType.getCheckedRadioButtonId();

        int selectedOccupantsTypeTypeId = rgOccupantsType.getCheckedRadioButtonId();

        rbOccupants = findViewById(selectedOccupantsTypeTypeId);

        rbRoomType = findViewById(selectedRoomTypeTypeId);

        // check if room type has been selected
        if (rgRoomType.getCheckedRadioButtonId() == -1){
            showSnackBar("Please selected Room Type!");
            return;
        }

        // check if room occupants has been selected
        if (rgOccupantsType.getCheckedRadioButtonId() == -1){
            showSnackBar("Please selected Occupants Type!");
            return;
        }

        // check if room description has been entered
        if (TextUtils.isEmpty(etRoomDescription.getText())) {

            etRoomDescription.setError("Room description is required");

            etRoomDescription.requestFocus();

            return;

        }

        if (etRoomDescription.getText().length() < 2) {

            etRoomDescription.setError("Room nmber/name is too short!");

            etRoomDescription.requestFocus();

            return;
        }

        // check if number of occupants has been entered
        if (TextUtils.isEmpty(etOccupants.getText())) {

            etOccupants.setError("Number of occupants is required");

            etOccupants.requestFocus();

            return;

        }

        // check if all three photos has been uploaded
        if (!isImageOneUploaded) {

            showSnackBar("Please upload Room Photo 1");

            return;
        }

        if (!isImageTwoUploaded) {

            showSnackBar("Please upload Room Photo 2");

            return;
        }

        if (!isImageThreeUploaded) {

            showSnackBar("Please upload Room Photo 3");

            return;
        }

        // check if monthly rental has been entered
        if (TextUtils.isEmpty(etMonthlyRental.getText())) {

            etMonthlyRental.setError("Monthly Rental Is Required");

            etMonthlyRental.requestFocus();

            return;

        }

        // all is good, save entry to database
        saveRoom();

        // close activity, redirect to rooms activity

    }

    private void saveRoom() {

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please wait...");

        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();

        final Room room = new Room();

        // set roomID
        room.setRoomId(roomId);





        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("rooms")
                .document(roomId)
                .set(room)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();

                        showSnackBar(e.getMessage());

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressDialog.dismiss();

                        Toast.makeText(AddRoomActivity.this, "Room Added Successfully", Toast.LENGTH_SHORT).show();

                        finish();



                    }
                });


    }

    private void initViews() {

        rgOccupantsType = findViewById(R.id.add_room_rgOccupantsType);

        rgRoomType = findViewById(R.id.add_room_rgRoomTypeGroup);

        etMonthlyRental = findViewById(R.id.add_room_etMonthlyRental);

        etRoomDescription = findViewById(R.id.add_room_etRoomDescription);

        tvRoomId = findViewById(R.id.add_room_tvRoomId);

        tvRoomId.setText(roomId);

        ivRoomImage1 = findViewById(R.id.add_room_ivPhoto1);

        ivRoomImage2 = findViewById(R.id.add_room_ivPhoto2);

        ivRoomImage3 = findViewById(R.id.add_room_ivPhoto3);

        btnAddRoom = findViewById(R.id.add_room_btnAddRoom);

        etOccupants = findViewById(R.id.add_room_etNumberOfOccupants);

    }

    private void showSnackBar(String message) {

        final Snackbar snackbar = Snackbar.make(rootLayout, message, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(rootLayout.getWindowToken(), 0);

        snackbar.show();
    }

    private void uploadImageToFirebaseOne() {

        LinearLayout linearLayout = findViewById(R.id.add_room_image1_progress);

        linearLayout.setVisibility(View.VISIBLE);

        final ProgressBar progressBar = findViewById(R.id.add_room_image1_progress_status_progress);

        final TextView textView = findViewById(R.id.add_room_image1_progress_status);

        final StorageReference fileReference =
                mStorageReference.child(currentUserId + "_photo1.jpg");

        UploadTask uploadTask = fileReference.putFile(mImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                showSnackBar(e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        strImageUrlOne = uri.toString();

                        textView.setText("Image 1 Uploaded successfully");

                        progressBar.setVisibility(View.GONE);

                        isImageOneUploaded = true;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showSnackBar(e.getMessage());

                    }
                });

            }
        });

    }

    private void uploadImageToFirebaseTwo() {

        LinearLayout linearLayout = findViewById(R.id.add_room_image2_progress);

        linearLayout.setVisibility(View.VISIBLE);

        final ProgressBar progressBar = findViewById(R.id.add_room_image2_progress_status_progress);

        final TextView textView = findViewById(R.id.add_room_image2_progress_status);

        final StorageReference fileReference =
                mStorageReference.child(currentUserId + "_photo2.jpg");

        UploadTask uploadTask = fileReference.putFile(mImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                showSnackBar(e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        strImageUrlTwo = uri.toString();

                        textView.setText("Image 2 Uploaded successfully");

                        progressBar.setVisibility(View.GONE);

                        isImageTwoUploaded = true;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showSnackBar(e.getMessage());

                    }
                });

            }
        });

    }

    private void uploadImageToFirebaseThree() {

        LinearLayout linearLayout = findViewById(R.id.add_room_image3_progress);

        linearLayout.setVisibility(View.VISIBLE);

        final ProgressBar progressBar = findViewById(R.id.add_room_image3_progress_status_progress);

        final TextView textView = findViewById(R.id.add_room_image3_progress_status);

        final StorageReference fileReference =
                mStorageReference.child(currentUserId + "_photo3.jpg");

        UploadTask uploadTask = fileReference.putFile(mImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                showSnackBar(e.getMessage());

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        strImageUrlThree = uri.toString();

                        textView.setText("Image 3 Uploaded successfully");

                        progressBar.setVisibility(View.GONE);

                        isImageThreeUploaded = true;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        showSnackBar(e.getMessage());

                    }
                });

            }
        });

    }
}
