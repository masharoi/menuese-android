package com.creations.roitman.menume;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
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
    private static final String LOG_TAG = RestaurantFragment.class.getName();
    private static final String RESTAURANTS_URL = "http://grython.pythonanywhere.com/api/restaurants";
    private static final String DATA_TYPE = "restaurant";
    private TextView empty;

    private RestaurantAdapter mAdapter;
    private RecyclerView mNumbersList;
    private List<Restaurant> restaurants = new ArrayList<Restaurant>();

    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "myprefs";
    public static final String APP_PREFERENCES_HOME = "isMenu";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bottom_bar, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        Log.e(LOG_TAG, "the Restaurant fragment is created");

        mNumbersList = (RecyclerView) rootView.findViewById(R.id.rv_numbers);
        mNumbersList.setLayoutManager(layoutManager);
        mNumbersList.setHasFixedSize(true);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.custom_divider));
        mNumbersList.addItemDecoration(itemDecorator);

        mAdapter = new RestaurantAdapter(restaurants, new RestaurantAdapter.OnItemClickListener() {
            @Override public void onItemClick(int id) {
                MenuFragment nextFrag = new MenuFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("REST_ID", id);
                nextFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_fragment_holder, nextFrag,"menuFragment")
                        .commit();
                mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean(APP_PREFERENCES_HOME, true);
                editor.apply();
            }
        });
        mNumbersList.setAdapter(mAdapter);

        //check the network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if(QueryUtils.checkConnectivity(connectivityManager)) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            empty = rootView.findViewById(R.id.emptyStateMessage);
            empty.setText("No internet connection.");
        }
        return rootView;
    }

    @Override
    public android.support.v4.content.Loader<List<Restaurant>> onCreateLoader(int i, Bundle bundle) {
        return new MenuLoader(getContext(), RESTAURANTS_URL, DATA_TYPE);
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