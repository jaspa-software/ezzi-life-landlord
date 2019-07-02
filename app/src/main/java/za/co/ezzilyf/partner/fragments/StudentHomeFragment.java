package za.co.ezzilyf.partner.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.utils.SharedPrefConfig;

public class StudentHomeFragment extends Fragment {

    private TextView tvRoomId, tvLandlordName, tvMonthlyRental, tvStartDate, tvEndDate,
            tvNextDueDate,tvStatus;

    private String currentUserId;

    private CardView cardView;

    private Button btnSearch;

    private TextView textView;

    private LinearLayout report, chat, pay;

    public StudentHomeFragment() {
        // Required empty public constructor
    }

    public static StudentHomeFragment getInstance(){

        StudentHomeFragment fragment = new StudentHomeFragment();

        return  fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        SharedPrefConfig sharedPrefConfig = new SharedPrefConfig(getContext());

        currentUserId = sharedPrefConfig.readUid();

        initViews();

        displayRoomDetails();
    }

    private void initViews() {

        if (getActivity() !=null) {

            cardView = getActivity().findViewById(R.id.student_home_cardView);

            tvRoomId = getActivity().findViewById(R.id.fragment_student_home_tvRoomId);

            tvLandlordName = getActivity().findViewById(R.id.fragment_student_home_tvLandlordName);

            tvMonthlyRental = getActivity().findViewById(R.id.fragment_student_home_tvMonthlyRental);

            tvStartDate = getActivity().findViewById(R.id.fragment_student_home_tvStartDate);

            tvEndDate = getActivity().findViewById(R.id.fragment_student_home_tvEndDate);

            tvNextDueDate = getActivity().findViewById(R.id.fragment_student_home_tvNextDueDate);

            tvStatus = getActivity().findViewById(R.id.fragment_student_home_tvStatus);

            textView = getActivity().findViewById(R.id.student_home_textView);

            btnSearch = getActivity().findViewById(R.id.student_home_btnSearch);

            report = getActivity().findViewById(R.id.student_home_reportFault);

            pay = getActivity().findViewById(R.id.student_home_payRental);

            chat = getActivity().findViewById(R.id.student_home_chat);
        }

    }

    private void displayRoomDetails(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Please wait ....");

        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();

        FirebaseFirestore accountRef = FirebaseFirestore.getInstance();

        accountRef.collection("rentals")
                .document(currentUserId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            tvRoomId.setText(documentSnapshot.get("referenceNumber").toString().trim());

                            tvLandlordName.setText(documentSnapshot.get("landlordUid").toString().trim());

                            tvMonthlyRental.setText(documentSnapshot.get("rental").toString().trim());

                            tvStartDate.setText(documentSnapshot.get("startDate").toString().trim());

                            tvEndDate.setText(documentSnapshot.get("endDate").toString().trim());

                            tvNextDueDate.setText(documentSnapshot.get("nextDueDate").toString().trim());

                            tvStatus.setText(documentSnapshot.get("status").toString().trim());

                            cardView.setVisibility(View.VISIBLE);

                            chat.setVisibility(View.VISIBLE);

                            report.setVisibility(View.VISIBLE);

                            pay.setVisibility(View.VISIBLE);

                            progressDialog.dismiss();

                        }else {

                            progressDialog.dismiss();

                            cardView.setVisibility(View.GONE);

                            btnSearch.setVisibility(View.VISIBLE);

                            textView.setVisibility(View.VISIBLE);

                            chat.setVisibility(View.GONE);

                            report.setVisibility(View.GONE);

                            pay.setVisibility(View.GONE);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();

                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
