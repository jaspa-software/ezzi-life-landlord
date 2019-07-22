package za.co.ezzilyf.partner.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import za.co.ezzilyf.partner.R;

public class StudentAccountFragment extends Fragment {


    public StudentAccountFragment() {
        // Required empty public constructor
    }

    public static StudentAccountFragment getInstance(){

        StudentAccountFragment fragment = new StudentAccountFragment();

        return  fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_account, container, false);
    }

}
