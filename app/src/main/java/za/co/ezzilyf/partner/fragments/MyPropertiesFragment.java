package za.co.ezzilyf.partner.fragments;


import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.DocumentViewChange;

import java.util.ArrayList;
import java.util.List;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.activities.ListPropertyActivity;
import za.co.ezzilyf.partner.adapters.MyPropertiesAdapter;
import za.co.ezzilyf.partner.models.Property;
import za.co.ezzilyf.partner.utils.SharedPrefConfig;

import static za.co.ezzilyf.partner.MyApplication.CHANNEL_ID;

public class MyPropertiesFragment extends Fragment {

    private Button btnListMyProperty;

    private ProgressBar progressBar;

    private RecyclerView recyclerView;

    private TextView textView, textView2;

    private ImageView imageView;

    private NotificationManagerCompat notificationManagerCompat;

    private FirebaseUser user;

    public MyPropertiesFragment() {
        // Required empty public constructor
    }


    public static MyPropertiesFragment getInstance(){
        MyPropertiesFragment fragment = new MyPropertiesFragment();
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_properties, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        notificationManagerCompat = NotificationManagerCompat.from(getContext());

        user = FirebaseAuth.getInstance().getCurrentUser();


        if (getActivity() !=null) {

            textView = getActivity().findViewById(R.id.my_properties_textView);

            textView2 = getActivity().findViewById(R.id.my_properties_textView2);

            recyclerView = getActivity().findViewById(R.id.my_properties_recyclerView);

            progressBar = getActivity().findViewById(R.id.my_properties_progressBar);

            imageView = getActivity().findViewById(R.id.my_properties_imageView);

            btnListMyProperty = getActivity().findViewById(R.id.my_properties_btnListMyProperty);

            btnListMyProperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), ListPropertyActivity.class);

                    startActivity(intent);

                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            recyclerView.setHasFixedSize(true);

            textView.setText("Hello "  + user.getDisplayName() + "!");

            getMyProperties();

            FloatingActionButton fabAddProperty = getActivity().findViewById(R.id.my_properties_fabAddProperty);
            fabAddProperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), ListPropertyActivity.class);

                    startActivity(intent);
                }
            });
        }


    }

    private void getMyProperties() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("residences")
                .whereEqualTo("propertyOwnerUid", user.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                        if (e !=null) {

                            progressBar.setVisibility(View.GONE);

                            textView.setText("Unexpected Error Occurred");

                            textView2.setText(e.getMessage());

                            textView.setVisibility(View.VISIBLE);

                            textView2.setVisibility(View.VISIBLE);

                            btnListMyProperty.setVisibility(View.GONE);

                            imageView.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.GONE);

                            return;
                        }


                        if (queryDocumentSnapshots !=null && queryDocumentSnapshots.size() != 0) {

                            List<Property> properties = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                                Property property = doc.toObject(Property.class);

                                properties.add(property);

                            }

                            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                                if (dc.getType() == DocumentChange.Type.MODIFIED) {

                                    Property updatedProperty = dc.getDocument().toObject(Property.class);

                                   // showPushNotification(updatedProperty);

                                }
                            }

                            MyPropertiesAdapter adapter = new MyPropertiesAdapter(properties,getContext());

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);

                            textView.setVisibility(View.GONE);

                            textView2.setVisibility(View.GONE);

                            btnListMyProperty.setVisibility(View.GONE);

                            imageView.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.VISIBLE);



                        }else if (queryDocumentSnapshots !=null && queryDocumentSnapshots.size() == 0) {

                            progressBar.setVisibility(View.GONE);

                            textView.setText("No residences listed");

                            textView2.setText("When you have approved residence, you'll see them listed here.");

                            textView.setVisibility(View.VISIBLE);

                            textView2.setVisibility(View.VISIBLE);

                            btnListMyProperty.setVisibility(View.VISIBLE);

                            imageView.setVisibility(View.VISIBLE);

                            recyclerView
                                    .setVisibility(View.GONE);

                        }

                    }
                });

    }


    private void showPushNotification(Property updatedProperty) {

//        String title = "Property " + updatedProperty.getPropertyId();
//
//        String message = "Property  status changed to " + updatedProperty.getListingStatus();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),CHANNEL_ID);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), CHANNEL_ID);

//        notificationBuilder.setSmallIcon(R.drawable.icon_app)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .build();
//
//        notificationManagerCompat.notify(1,notificationBuilder.build());
    }
}
