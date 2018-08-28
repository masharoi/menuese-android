package com.creations.roitman.menume;

import android.content.Context;
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

import com.creations.roitman.menume.data.Restaurant;
import com.creations.roitman.menume.utilities.PreferencesUtils;
import com.creations.roitman.menume.utilities.QueryUtils;

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
            @Override public void onItemClick(int id, String name) {
                MenuFragment nextFrag = new MenuFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("REST_ID", id);
                bundle.putString("REST_NAME", name);
                nextFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_fragment_holder, nextFrag,"menuFragment")
                        .commit();
                PreferencesUtils.setRestaurantChosen(true, getContext());
                PreferencesUtils.setIsOrdered(false, getContext());
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
        return new MenuLoader<List<Restaurant>>(getContext(), RESTAURANTS_URL, DATA_TYPE);
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