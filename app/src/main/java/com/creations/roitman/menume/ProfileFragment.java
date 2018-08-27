package com.creations.roitman.menume;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    final private String LOG_TAG =  ProfileFragment.class.getName();
    Button btnLogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(LOG_TAG, "The fragment is created");
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        btnLogOut = (Button) rootView.findViewById(R.id.btn_log_out);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(getActivity(), ActivityLogin.class);
                startActivity(I);

            }
        });
        return rootView;
    }

}
