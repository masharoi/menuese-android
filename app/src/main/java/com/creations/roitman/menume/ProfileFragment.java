package com.creations.roitman.menume;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.creations.roitman.menume.data.Order;
import com.creations.roitman.menume.utilities.PreferencesUtils;
import com.creations.roitman.menume.utilities.QueryUtils;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProfileFragment extends Fragment {

    final private String LOG_TAG =  ProfileFragment.class.getName();
    private String PROFILE_URL = "/api/orders";
    final public static String PROFILE_TYPE = "profile";
    private List<Order> orders = new ArrayList<Order>();
    private Button btnLogOut;
    private OrdersAdapter mAdapter;
    private RecyclerView orderList;
    private SharedPreferences mSettings;
    private View emptyState, nonEmptyState;
    private TextView empty;
    private ProgressBar spinner;
    private int orderId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(LOG_TAG, "The fragment is created");
        final View rootView = inflater.inflate(R.layout.general_profile_fragment, container, false);
        nonEmptyState = rootView.findViewById(R.id.profile_fragment);
        emptyState = rootView.findViewById(R.id.empty_menu);

        spinner = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        spinner.setVisibility(VISIBLE);

        orderList = (RecyclerView) rootView.findViewById(R.id.rv_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        orderList.setLayoutManager(layoutManager);
        orderList.setHasFixedSize(true);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.custom_divider));
        orderList.addItemDecoration(itemDecorator);

        mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        Log.e(LOG_TAG, mSettings.getString(PreferencesUtils.USER_TOKEN, ""));
        mAdapter = new OrdersAdapter(orders, new OrdersAdapter.OnItemClickListener() {
            @Override public void onItemClick(int id) {
                orderId = id;
                ReceiptFragment receipt = new ReceiptFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("ORDER_ID", id);
                receipt.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_fragment_holder, receipt,"receiptFragment")
                        .commit();
            }
        } );
        orderList.setAdapter(mAdapter);
        btnLogOut = (Button) rootView.findViewById(R.id.btn_log_out);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PreferencesUtils.setToken(PreferencesUtils.INVALID_USER_TOKEN, getContext());
                Intent I = new Intent(getActivity(), ActivityLogin.class);
                startActivity(I);

            }
        });

        makeHttpRequest(rootView, getOrdersLoader);
        return rootView;
    }

    private void makeHttpRequest(View rootView, android.support.v4.app.LoaderManager.LoaderCallbacks loader) {
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if(QueryUtils.checkConnectivity(connectivityManager)) {
            emptyState.setVisibility(GONE);
            getLoaderManager().initLoader(7, null, loader);
        } else {
            nonEmptyState.setVisibility(GONE);
            empty = rootView.findViewById(R.id.empty_order_state);
            empty.setText(R.string.iconnection);
        }
    }

    /**
     * Loader to get the list of orders of the current user.
     */
    private android.support.v4.app.LoaderManager.LoaderCallbacks getOrdersLoader =
            new LoaderManager.LoaderCallbacks<List<Order>>() {

                @NonNull
                @Override
                public Loader<List<Order>> onCreateLoader(int id, @Nullable Bundle args) {
                    if (mSettings.getString(PreferencesUtils.USER_TOKEN, "")
                            .equals(PreferencesUtils.INVALID_USER_TOKEN)) {
                        Intent I = new Intent(getContext(), ActivityLogin.class);
                        startActivity(I);
                    }
                    Log.e(LOG_TAG, "this is url " + QueryUtils.BASE_URL + PROFILE_URL);
                    return new MenuLoader<List<Order>>(getContext(),
                            QueryUtils.BASE_URL + PROFILE_URL, PROFILE_TYPE,
                            mSettings.getString(PreferencesUtils.USER_TOKEN, ""));
                }

                @Override
                public void onLoadFinished(@NonNull Loader<List<Order>> loader, List<Order> data) {
                    Log.e(LOG_TAG, "the load is finished");
                    spinner.setVisibility(GONE);
                    if (data != null) {
                        orders.clear();
                        orders.addAll(data);
                        for (int i = 0; i<orders.size(); i++) {
                            Log.e(LOG_TAG, "This is name after load " + orders.get(i).getRestName());
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<List<Order>> loader) {
                    orders.clear();
                }
            };

}
