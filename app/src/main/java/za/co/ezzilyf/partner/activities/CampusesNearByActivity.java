package za.co.ezzilyf.partner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.adapters.NearByAdapter;
import za.co.ezzilyf.partner.models.NearBy;
import za.co.ezzilyf.partner.models.Property;

public class CampusesNearByActivity extends AppCompatActivity {

    private static final String TAG = "CampusesNearByActivity";

    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    private TextView textView;

    private Property property;

    private NearByAdapter adapter;

    ArrayAdapter<String> campusAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_campuses_near_by);

        property = (Property) getIntent().getSerializableExtra("PROPERTY");

        Toolbar toolbar = findViewById(R.id.campuses_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initViews();

        if (property !=null) {

            TextView tvPropertyName = findViewById(R.id.campuses_near_by_tvPropertyName);

            tvPropertyName.setText(property.getPropertyName() + " - " + property.getPropertyRefNumber());

            getInstitutions();

        }

        Button btnAddCampus = findViewById(R.id.campuses_near_by_btnAdd);

        btnAddCampus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddCampusDialog();

            }
        });

    }


    private void showAddCampusDialog() {

        String [] institutions = getResources().getStringArray(R.array.institutions);

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_near_by, viewGroup, false);

        final AutoCompleteTextView actCampus = dialogView.findViewById(R.id.dialog_near_by_actCampus);

        final AutoCompleteTextView actInstitution = dialogView.findViewById(R.id.dialog_near_by_actInstitution);

        ArrayAdapter<String> institutionsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,
                institutions);;


        final List<String> campusList =  new ArrayList<>();

        actInstitution.setAdapter(institutionsAdapter);

        actInstitution.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus){

                    String institution = actInstitution.getText().toString().trim();

                    FirebaseFirestore institutionCampusRef = FirebaseFirestore.getInstance();
                    actCampus.setHint("Loading campuses....");

                    institutionCampusRef.collection("campuses")
                            .whereEqualTo("institution",institution)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {

                                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots){

                                            campusList.add(doc.get("campusName").toString());

                                            campusAdapter = new ArrayAdapter<>(CampusesNearByActivity.this,
                                                    android.R.layout.simple_list_item_1,
                                                    campusList);

                                        }

                                        actCampus.setAdapter(campusAdapter);
                                        actCampus.setHint("Type name of campus");

                                    }else{
                                        actCampus.setHint("No campuses found");
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    actCampus.setError(e.getMessage());
                                }
                            });

                }else {
                    campusList.clear();
                }
            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {


                HashMap<String, Object> institutionDetails = new HashMap<>();

                institutionDetails.put("campus",actCampus.getText().toString().trim());

                institutionDetails.put("institution", actInstitution.getText().toString().trim());

                institutionDetails.put("propertyCoverImage", "https://images.prop24.com/199224006/Crop525x350");

                institutionDetails.put("propertyId", property.getPropertyRefNumber());

                institutionDetails.put("propertyName", property.getPropertyName());

                FirebaseFirestore nearByRef = FirebaseFirestore.getInstance();

                nearByRef.collection("nearByInstitutions")
                        .add(institutionDetails)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                dialog.dismiss();

                                Toast.makeText(CampusesNearByActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();

    }

    private void getInstitutions() {

        FirebaseFirestore nearByInstitutionRef = FirebaseFirestore.getInstance();


        nearByInstitutionRef.collection("nearByInstitutions")
                .whereEqualTo("propertyId",property.getPropertyRefNumber())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e !=null) {

                            textView.setVisibility(View.VISIBLE);

                            progressBar.setVisibility(View.GONE);

                            textView.setText(e.getMessage());

                            return;
                        }

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {

                            progressBar.setVisibility(View.GONE);

                            List<NearBy> nearList = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                                NearBy nearBy = doc.toObject(NearBy.class);

                                nearList.add(nearBy);
                            }

                            adapter = new NearByAdapter(nearList,CampusesNearByActivity.this);

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            textView.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.VISIBLE);

                        }else{

                            textView.setVisibility(View.VISIBLE);

                            progressBar.setVisibility(View.GONE);

                            textView.setText("No institutions added");

                            recyclerView.setVisibility(View.GONE);

                        }

                    }
                });
    }

    private void initViews() {

        recyclerView = findViewById(R.id.campuses_near_by_recyclerVIew);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);

        progressBar  = findViewById(R.id.campuses_near_by_propressBar);

        textView = findViewById(R.id.campuses_near_by_tvMessage);

    }
}
