package com.creations.roitman.menume;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RestaurantFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Restaurant>> {
    public static final String ARG_TITLE = "arg_title";
    private static final String LOG_TAG = RestaurantFragment.class.getName();
    private static final String RESTAURANTS_URL = "http://grython.pythonanywhere.com/api/restaurants";
//    private static final String RESTAURANTS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private TextView empty;

    private RestaurantAdapter mAdapter;
    private RecyclerView mNumbersList;
    private List<Restaurant> restaurants = new ArrayList<Restaurant>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bottom_bar, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mNumbersList = (RecyclerView) rootView.findViewById(R.id.rv_numbers);
        mNumbersList.setLayoutManager(layoutManager);
        mNumbersList.setHasFixedSize(true);

        mAdapter = new RestaurantAdapter(restaurants, new RestaurantAdapter.OnItemClickListener() {
            @Override public void onItemClick(int id) {
                MenuFragment nextFrag = new MenuFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("REST_ID", id);
                nextFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_fragment_holder, nextFrag,"menuFragment")
                        .commit();
                ((MainActivity) getActivity()).resetRestaurantChosen();
            }
        });
        mNumbersList.setAdapter(mAdapter);

        //check the network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .getState() == NetworkInfo.State.CONNECTED) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            empty = rootView.findViewById(R.id.emptyStateMessage);
            empty.setText("No internet connection.");
        }
        return rootView;
    }

    @Override
    public android.support.v4.content.Loader<List<Restaurant>> onCreateLoader(int i, Bundle bundle) {

//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//        String minMagnitude = sharedPrefs.getString(
//                getString(R.string.settings_min_magnitude_key),
//                getString(R.string.settings_min_magnitude_default));
//        Uri baseUri = Uri.parse(RESTAURANTS_URL);
//        Uri.Builder uriBuilder = baseUri.buildUpon();
//
//        uriBuilder.appendQueryParameter("format", "geojson");
//        uriBuilder.appendQueryParameter("limit", "10");
//        uriBuilder.appendQueryParameter("minmag", minMagnitude);
//        uriBuilder.appendQueryParameter("orderby", "time");

        return new RestaurantLoader(getContext(), RESTAURANTS_URL);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<List<Restaurant>> loader,
                               List<Restaurant> restaurants) {
        Log.e(LOG_TAG, "Load is finished");
        if (restaurants != null && !restaurants.isEmpty()) {
            this.restaurants.clear();
            this.restaurants.addAll(restaurants);
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<List<Restaurant>> loader) {
        Log.e(LOG_TAG, "Loader is reset");
        this.restaurants.clear();
        mAdapter.notifyDataSetChanged();
    }

}