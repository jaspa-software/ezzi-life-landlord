package za.co.ezzilyf.partner.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.adapters.MyPropertiesAdapter;
import za.co.ezzilyf.partner.adapters.MyTenantsAdapter;
import za.co.ezzilyf.partner.models.Property;
import za.co.ezzilyf.partner.models.Tenant;
import za.co.ezzilyf.partner.utils.SharedPrefConfig;

public class MyTenantsFragment extends Fragment {

    private ProgressBar progressBar;

    private RecyclerView recyclerView;

    private TextView textView, textView2;

    private ImageView imageView;


    public MyTenantsFragment() {
        // Required empty public constructor
    }

    public static MyTenantsFragment getInstance(){

        MyTenantsFragment fragment = new MyTenantsFragment();

        return  fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_my_tenants, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        SharedPrefConfig sharedPrefConfig = new SharedPrefConfig(getContext());

        String currentUserId =  sharedPrefConfig.readUid();

        if (getActivity() !=null) {

            textView = getActivity().findViewById(R.id.my_tenants_textView);

            textView2 = getActivity().findViewById(R.id.my_tenants_textView2);

            recyclerView = getActivity().findViewById(R.id.my_tenants_recyclerView);

            progressBar = getActivity().findViewById(R.id.my_tenants_progressBar);

            imageView = getActivity().findViewById(R.id.my_tenants_imageView);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            recyclerView.setHasFixedSize(true);

            getTenants(currentUserId);

        }
    }

    private void getTenants(String currentUserId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("tenants")
                .whereEqualTo("landlordId", currentUserId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                        if (e !=null) {

                            progressBar.setVisibility(View.GONE);

                            textView.setText("Unexpected Error Occurred");

                            textView2.setText(e.getMessage());

                            textView.setVisibility(View.VISIBLE);

                            textView2.setVisibility(View.VISIBLE);

                            imageView.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.GONE);

                            return;
                        }

                        if (queryDocumentSnapshots !=null && queryDocumentSnapshots.size() != 0) {

                            List<Tenant> tenants = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                                Tenant tenant = doc.toObject(Tenant.class);

                                tenants.add(tenant);

                            }

                            MyTenantsAdapter adapter = new MyTenantsAdapter(tenants,getContext());

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);

                            textView.setVisibility(View.GONE);

                            textView2.setVisibility(View.GONE);

                            imageView.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.VISIBLE);

                        }else if (queryDocumentSnapshots !=null && queryDocumentSnapshots.size() == 0) {

                            progressBar.setVisibility(View.GONE);

                            textView.setText("No tenants listed");

                            textView2.setText("When you have tenants, you'll see them listed here.");

                            textView.setVisibility(View.VISIBLE);

                            textView2.setVisibility(View.VISIBLE);

                            imageView.setVisibility(View.VISIBLE);

                            recyclerView.setVisibility(View.GONE);

                        }

                    }
                });

    }
}
