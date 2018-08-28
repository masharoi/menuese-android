package com.creations.roitman.menume;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.creations.roitman.menume.data.Order;
import com.creations.roitman.menume.utilities.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Order>>{

    final private String LOG_TAG =  ProfileFragment.class.getName();
    List<Order> orders = new ArrayList<Order>();
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

                PreferencesUtils.setToken(PreferencesUtils.INVALID_USER_TOKEN, getContext());
                Intent I = new Intent(getActivity(), ActivityLogin.class);
                startActivity(I);

            }
        });
        return rootView;
    }

    @NonNull
    @Override
    public Loader<List<Order>> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Order>> loader, List<Order> data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Order>> loader) {

    }
}
