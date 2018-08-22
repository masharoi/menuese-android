package com.creations.roitman.menume;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.MenuDatabase;
import com.creations.roitman.menume.data.Order;
import com.creations.roitman.menume.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class OrderFragment  extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Order> {

    final private String LOG_TAG =  OrderFragment.class.getName();
    private static final String DATA_TYPE = "order";
    private DishesAdapter mAdapter;
    private RecyclerView orderList;
    private View beforeOrder, emptyState;

    private MenuDatabase mDb;
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "myprefs";
    private static final String APP_PREFERENCES_PRICE = "totalPrice";
    public static final String APP_PREFERENCES_RESTID = "restaurantID";
    private static final String APP_PREFERENCES_HOME = "isMenu";

    private List<Dish> order = new ArrayList<Dish>();
    private Button orderBtn;
    private TextView totalPrice;
    private RadioGroup rgroup;
    private int paymentOption;
    private Order orderData;

    private View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RadioButton r = (RadioButton) view;
            if (R.id.payment_type_cash == r.getId()) {
                paymentOption = 0;
            } else {
                paymentOption = 1;
            }
            orderBtn.setBackgroundColor(getResources().getColor(R.color.holo_red_dark));
        }
    };

    private void makePostRequest() {

        int restId = mSettings.getInt(APP_PREFERENCES_RESTID, 0);
        orderData = new Order(restId, paymentOption, 1);

        //check the network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if(QueryUtils.checkConnectivity(connectivityManager)) {
            getLoaderManager().initLoader(2, null, this);
        } else {
            //empty = rootView.findViewById(R.id.emptyStateMessage);
            //empty.setText("No internet connection.");
        }
    }

    private View.OnClickListener orderBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        makePostRequest();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(LOG_TAG, "The order fragment is created");
        View rootView = inflater.inflate(R.layout.general_order_fragment, container, false);
        beforeOrder = rootView.findViewById(R.id.before_order);
        emptyState = rootView.findViewById(R.id.empty_menu);
        emptyState.setVisibility(View.INVISIBLE);


        mDb = MenuDatabase.getInstance(getActivity().getApplicationContext());
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        orderBtn = (Button) rootView.findViewById(R.id.order_btn);
        orderBtn.setOnClickListener(orderBtnListener);
        orderList = (RecyclerView) rootView.findViewById(R.id.rv_order);
        totalPrice = (TextView) rootView.findViewById(R.id.total_price);
        rgroup = (RadioGroup) rootView.findViewById(R.id.rgroup);


        RadioButton cashButton = (RadioButton) rootView.findViewById(R.id.payment_type_cash);
        cashButton.setOnClickListener(radioButtonClickListener);

        RadioButton cardButton = (RadioButton) rootView.findViewById(R.id.payment_type_card);
        cardButton.setOnClickListener(radioButtonClickListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        orderList.setLayoutManager(layoutManager);
        orderList.setHasFixedSize(true);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.custom_divider));
        orderList.addItemDecoration(itemDecorator);

        mAdapter = new DishesAdapter(order, new DishesAdapter.OnItemClickListener() {
            @Override public void onItemClick() {
                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
            }
        }, totalPrice);
        mAdapter.setDb(mDb);
        mAdapter.setSettings(mSettings);
        orderList.setAdapter(mAdapter);

        MainViewModel vm = ViewModelProviders.of(this).get(MainViewModel.class);
        vm.getOrder().observe(this, new Observer<List<Dish>>() {
            @Override
            public void onChanged(@Nullable List<Dish> dishes) {
                order.clear();
                order.addAll(dishes);
                mAdapter.notifyDataSetChanged();
                if (order.isEmpty()) {
                    beforeOrder.setVisibility(GONE);
                    emptyState.setVisibility(View.VISIBLE);
                }
                else {
                    emptyState.setVisibility(View.GONE);
                    beforeOrder.setVisibility(View.VISIBLE);
                    double price = mSettings.getFloat(APP_PREFERENCES_PRICE, 0);
                    totalPrice.setText(String.valueOf(price) + "\u20BD");
                }
            }
        });


        return rootView;

    }

    @NonNull
    @Override
    public Loader<Order> onCreateLoader(int id, @Nullable Bundle args) {
        String POST_URL = "http://grython.pythonanywhere.com/api/orders/add";
        return  new MenuLoader(getContext(), POST_URL, DATA_TYPE, orderData, order);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Order> loader, Order data) {
        Log.e(LOG_TAG, "Load is finished");
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.daoAccess().deleteDish();
            }
        });
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_HOME, false);
        editor.apply();
        if (data != null) {
             beforeOrder.setVisibility(GONE);
             emptyState.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Order> loader) {

    }
}
