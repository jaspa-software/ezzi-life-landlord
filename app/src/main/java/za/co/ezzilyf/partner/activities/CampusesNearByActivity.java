package za.co.ezzilyf.partner.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_campuses_near_by);

        property = (Property) getIntent().getSerializableExtra("PROPERTY");

        initViews();

        if (property !=null) {

            TextView tvPropertyName = findViewById(R.id.campuses_near_by_tvPropertyName);

            tvPropertyName.setText(property.getPropertyName() + " - " + property.getPropertyRefNumber());

            getInstitutions();

        }
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
