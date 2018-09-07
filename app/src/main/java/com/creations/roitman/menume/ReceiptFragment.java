package com.creations.roitman.menume;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.creations.roitman.menume.data.DishItem;
import com.creations.roitman.menume.data.Order;
import com.creations.roitman.menume.utilities.PreferencesUtils;
import com.creations.roitman.menume.utilities.QueryUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;



public class ReceiptFragment extends Fragment {

    private static String LOG_TAG =  ReceiptFragment.class.getName();
    private TextView totalPrice, restName, date, empty;
    private ImageButton exit;
    private ReceiptAdapter dishesAdapter;
    private RecyclerView dishList;
    private List<DishItem> dishes = new ArrayList<DishItem>();
    private int id;
    private View emptyState, nonEmpty;
    private String PROFILE_URL = "/api/orders/";
    final public static String PROFILE_SINGLE_TYPE = "singleOrder";
    private SharedPreferences mSettings;
    private ProgressBar spinner;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(LOG_TAG, "The fragment is created");
        View rootView = inflater.inflate(R.layout.general_receipt_fragment, container, false);
        nonEmpty = rootView.findViewById(R.id.clicked_order);
        emptyState = rootView.findViewById(R.id.empty_menu);

        totalPrice = (TextView) rootView.findViewById(R.id.total_order_price);
        restName = (TextView) rootView.findViewById(R.id.restaurant_name);
        date = (TextView) rootView.findViewById(R.id.date);
        exit = (ImageButton) rootView.findViewById(R.id.exit_button);
        spinner = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("ORDER_ID");
        }

        dishList = (RecyclerView) rootView.findViewById(R.id.rv_check);

        LinearLayoutManager dishesLayoutManager = new LinearLayoutManager(getActivity());
        dishList.setLayoutManager(dishesLayoutManager);
        dishList.setHasFixedSize(true);

        dishesAdapter = new ReceiptAdapter(dishes);
        dishList.setAdapter(dishesAdapter);
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if(QueryUtils.checkConnectivity(connectivityManager)) {
            emptyState.setVisibility(GONE);
            getLoaderManager().initLoader(7, null, getSingleOrderLoader);
        } else {
            nonEmpty.setVisibility(GONE);
            empty = rootView.findViewById(R.id.empty_order_state);
            empty.setText(R.string.iconnection);
        }

        return rootView;
    }



    /**
     * Loader to get the order that the user has chosen from the list of orders.
     */
    private android.support.v4.app.LoaderManager.LoaderCallbacks getSingleOrderLoader =
            new LoaderManager.LoaderCallbacks<Order>() {

                @NonNull
                @Override
                public Loader<Order> onCreateLoader(int id, @Nullable Bundle args) {
                    String token =  mSettings.getString(PreferencesUtils.USER_TOKEN, "");
                    return new MenuLoader<Order>(getContext(),
                            QueryUtils.BASE_URL + PROFILE_URL + id, PROFILE_SINGLE_TYPE,
                            token);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Order> loader, Order data) {
                    Log.e(LOG_TAG, "the load is finished");
                    nonEmpty.setVisibility(VISIBLE);
                    spinner.setVisibility(GONE);
                    SimpleDateFormat fullStamp =
                            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'", Locale.ENGLISH);
                    Date d1 = null;
                    try {
                        d1 = fullStamp.parse(data.getTimestamp());
                    } catch (ParseException e) {
                        Log.e(LOG_TAG, "Error parsing the timestamp");
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    String dateWithoutTime = sdf.format(d1);
                    date.setText(dateWithoutTime);
                    totalPrice.setText(String.valueOf(data.getTotal()) + "\u20BD");
                    restName.setText("RESTAURANT NAME");
                    if (data.getItems() != null) {
                        dishes.clear();
                        dishes.addAll(data.getItems());
                        dishesAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Order> loader) {
                    dishes.clear();
                }
            };

}
