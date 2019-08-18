package za.co.ezzilyf.partner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import za.co.ezzilyf.partner.R;
import za.co.ezzilyf.partner.activities.HelpActivity;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment getInstance(){

        SettingsFragment fragment = new SettingsFragment();

        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        LinearLayout viewAccount = getActivity().findViewById(R.id.settings_viewAccount);

        viewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Functionality not implemented yet", Toast.LENGTH_LONG).show();

            }
        });

        LinearLayout viewHelp = getActivity().findViewById(R.id.settings_viewHelp);

        viewHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), HelpActivity.class);

                startActivity(intent);

            }
        });

        Button btnSignOut = getActivity().findViewById(R.id.settings_btnSignOut);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.signOut();

                getActivity().getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Toast.makeText(getContext(), "Thank you for using Ezzi Life", Toast.LENGTH_SHORT).show();

                getActivity().finish();


            }
        });
    }
}
