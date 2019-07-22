package za.co.ezzilyf.partner.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import za.co.ezzilyf.partner.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentNoticesFragment extends Fragment {


    public StudentNoticesFragment() {
        // Required empty public constructor
    }

    public static StudentNoticesFragment getInstance(){

        StudentNoticesFragment fragment = new StudentNoticesFragment();

        return  fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_notices, container, false);
    }

}
