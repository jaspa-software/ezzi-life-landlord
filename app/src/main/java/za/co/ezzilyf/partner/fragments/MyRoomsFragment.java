package za.co.ezzilyf.partner.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.activities.ListPropertyActivity;
import za.co.ezzilyf.partner.adapters.MyPropertiesAdapter;
import za.co.ezzilyf.partner.adapters.MyRoomsAdapter;
import za.co.ezzilyf.partner.models.Property;
import za.co.ezzilyf.partner.models.Room;
import za.co.ezzilyf.partner.utils.SharedPrefConfig;

public class MyRoomsFragment extends Fragment {

    private ProgressBar progressBar;

    private RecyclerView recyclerView;

    private TextView textView, textView2;

    private ImageView imageView;


    public MyRoomsFragment() {
        // Required empty public constructor
    }

    public static MyRoomsFragment getInstance(){

        MyRoomsFragment fragment = new MyRoomsFragment();

        return  fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_rooms, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        SharedPrefConfig sharedPrefConfig = new SharedPrefConfig(getContext());

        String currentUserId =  sharedPrefConfig.readUid();

        if (getActivity() !=null) {

            textView = getActivity().findViewById(R.id.my_rooms_textView);

            textView2 = getActivity().findViewById(R.id.my_rooms_textView2);

            recyclerView = getActivity().findViewById(R.id.my_rooms_recyclerView);

            progressBar = getActivity().findViewById(R.id.my_rooms_progressBar);

            imageView = getActivity().findViewById(R.id.my_rooms_imageView);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            recyclerView.setHasFixedSize(true);

            getRooms(currentUserId);

        }

    }

    private void getRooms(String currentUserId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("rooms")
                .whereEqualTo("propertyOwnerUid", currentUserId)
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

                            List<Room> rooms = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                                Room room = doc.toObject(Room.class);

                                rooms.add(room);

                            }

                            MyRoomsAdapter adapter = new MyRoomsAdapter(rooms,getContext());

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);

                            textView.setVisibility(View.GONE);

                            textView2.setVisibility(View.GONE);

                            imageView.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.VISIBLE);

                        }else if (queryDocumentSnapshots !=null && queryDocumentSnapshots.size() == 0) {

                            progressBar.setVisibility(View.GONE);

                            textView.setText("No rooms listed");

                            textView2.setText("When you have listed rooms, you'll see them listed here.");

                            textView.setVisibility(View.VISIBLE);

                            textView2.setVisibility(View.VISIBLE);

                            imageView.setVisibility(View.VISIBLE);

                            recyclerView.setVisibility(View.GONE);

                        }

                    }
                });

    }

}
