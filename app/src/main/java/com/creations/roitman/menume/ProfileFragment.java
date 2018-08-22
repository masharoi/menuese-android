package com.creations.roitman.menume;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends Fragment {

    final private String LOG_TAG =  ProfileFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(LOG_TAG, "The fragment is created");
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }
}
