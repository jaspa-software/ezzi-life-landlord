package za.co.ezzilyf.partner.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.adapters.AppointmentsAdapter;
import za.co.ezzilyf.partner.models.Appointment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    private TextView textView;


    public AppointmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointments, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        recyclerView = getActivity().findViewById(R.id.appointments_recyclerView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = getActivity().findViewById(R.id.appointments_progressBar);

        textView = getActivity().findViewById(R.id.appointments_textView);


        getAppointments(auth.getCurrentUser().getUid());


    }

    private void getAppointments(String uid) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final List<Appointment> appointmentList = new ArrayList<>();

        db.collection("appointments")
                .whereEqualTo("landlordUid", uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots.size() > 0) {

                            for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                                Appointment appointment = doc.toObject(Appointment.class);

                                appointmentList.add(appointment);

                            }

                            AppointmentsAdapter appointmentsAdapter =
                                    new AppointmentsAdapter(appointmentList, getContext());

                            recyclerView.setAdapter(appointmentsAdapter);

                            appointmentsAdapter.notifyDataSetChanged();

                            recyclerView.setVisibility(View.VISIBLE);

                            progressBar.setVisibility(View.GONE);

                            textView.setVisibility(View.GONE);


                        }else {

                            recyclerView.setVisibility(View.GONE);

                            progressBar.setVisibility(View.GONE);

                            textView.setText("You do not have any appointments yet");

                            textView.setVisibility(View.VISIBLE);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressBar.setVisibility(View.GONE);

                        textView.setText(e.getMessage());

                        textView.setVisibility(View.VISIBLE);

                        recyclerView.setVisibility(View.GONE);

                    }
                });
    }
}
