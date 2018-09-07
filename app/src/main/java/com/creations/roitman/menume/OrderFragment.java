package com.creations.roitman.menume;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.DishItem;
import com.creations.roitman.menume.data.MenuDatabase;
import com.creations.roitman.menume.data.Order;
import com.creations.roitman.menume.data.OrderedDish;
import com.creations.roitman.menume.utilities.GenUtils;
import com.creations.roitman.menume.utilities.PreferencesUtils;
import com.creations.roitman.menume.utilities.QueryUtils;
import com.creations.roitman.menume.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class OrderFragment  extends Fragment {

    final private String LOG_TAG =  OrderFragment.class.getName();
    private static final String ORDER_URL_GET = "/api/orders/";
    private static final String ORDER_URL_POST = "/api/orders/add";
    private static final String DATA_TYPE_ORDER_POST = "order";
    public static final String DATA_TYPE_ORDER_GET = "check";
    public static final String DATA_TYPE_ORDER_PATCH = "order_patch";
    private static final int POST_LOADER = 2;
    private static final int GET_LOADER = 3;
    private DishesAdapter mAdapter;
    private RecyclerView orderList;
    private View beforeOrder, emptyState;

    private MenuDatabase mDb;
    private SharedPreferences mSettings;

    private List<DishItem> order = new ArrayList<DishItem>();
    private List<DishItem> dishesInCheck = new ArrayList<DishItem>();
    private Button orderBtn;
    private TextView totalPrice;
    private RadioGroup rgroup;
    private int paymentOption;
    private Order orderData;
    private TextView empty;
    private double receiptPrice;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(LOG_TAG, "The order fragment is created" + order.size());
        View rootView = inflater.inflate(R.layout.general_order_fragment, container, false);
        beforeOrder = rootView.findViewById(R.id.before_order);
        emptyState = rootView.findViewById(R.id.empty_menu);
        emptyState.setVisibility(View.INVISIBLE);

        empty = rootView.findViewById(R.id.empty_order_state);


        mDb = MenuDatabase.getInstance(getActivity().getApplicationContext());
        mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSettings.registerOnSharedPreferenceChangeListener(preferenceListener);

        if (!mSettings.getBoolean(PreferencesUtils.IS_REST_CHOSEN, false)) {
            emptyState.setVisibility(View.VISIBLE);
            beforeOrder.setVisibility(View.GONE);
            return rootView;
        }
        orderBtn = (Button) rootView.findViewById(R.id.order_btn);
        if (mSettings.getBoolean(PreferencesUtils.UPDATE_ORDER, false)) {
            orderBtn.setBackgroundColor(getResources().getColor(R.color.holo_red_dark));
        }
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

        for (int i = 0; i<order.size(); i++) {
            Log.e(LOG_TAG, "This is name when init " + order.get(i).getName());
        }

        MainViewModel vm = ViewModelProviders.of(this).get(MainViewModel.class);
        vm.getOrder().observe(this, orderObserver);

        if (mSettings.getBoolean(PreferencesUtils.IS_ORDERED, false)) {
            makeRequest(GET_LOADER, getRequestLoader);
        }

        return rootView;

    }

    /**
     * This is the observer for the data set that contains the order.
     */
    private Observer<List<Dish>> orderObserver = new Observer<List<Dish>>() {
        @Override
        public void onChanged(@Nullable List<Dish> dishes) {
            double price = mSettings.getFloat(PreferencesUtils.TOTAL_CURRENT_PRICE, 0);
            List<DishItem> temp = new ArrayList<>();
            int  totalSize = order.size();
            for (int i = 0; i < totalSize; i++) {
                if (order.get(i) instanceof OrderedDish) {
                    temp.add(order.get(i));
                }
            }
            order.clear();
            order.addAll(temp);
            order.addAll(dishes);
            mAdapter.notifyDataSetChanged();
            if (order.isEmpty() && !mSettings.getBoolean(PreferencesUtils.IS_ORDERED, false)) {
                beforeOrder.setVisibility(GONE);
                emptyState.setVisibility(View.VISIBLE);
            }
            else {
                Log.e(LOG_TAG, "Price: " + price);
                emptyState.setVisibility(View.GONE);
                beforeOrder.setVisibility(View.VISIBLE);
                totalPrice.setText(String.valueOf(price) + getString(R.string.ruble));
            }

            for (int i = 0; i<order.size(); i++) {
                Log.e(LOG_TAG, "This is name after the order changed " + order.get(i).getName());
            }
        }
    };

    /**
     * Loader for the GET http request, which occurs when the user has already created an order
     * and now in check tab. This is needed in order to update the check.
     */
    private android.support.v4.app.LoaderManager.LoaderCallbacks<Order> patchRequestLoader =
            new LoaderManager.LoaderCallbacks<Order>() {
                @NonNull
                @Override
                public Loader<Order> onCreateLoader(int id, @Nullable Bundle args) {
                    String PATCH_URL = QueryUtils.BASE_URL + ORDER_URL_GET +
                            mSettings.getInt(PreferencesUtils.ORDER_ID, 0);

                    return  new MenuLoader<Order>(getContext(), PATCH_URL,
                            DATA_TYPE_ORDER_PATCH, dishesToUpdate(), getToken());                }

                @Override
                public void onLoadFinished(@NonNull Loader<Order> loader, Order data) {
                    Log.e(LOG_TAG, "Load is finished");
                    updateList(data);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Order> loader) {
                    order.clear();
                    mAdapter.notifyDataSetChanged();
                }
            };


    /**
     * Loader for the GET http request, which occurs when the user has already created an order
     * and now in check tab. This is needed in order to update the check.
     */
    private android.support.v4.app.LoaderManager.LoaderCallbacks<Order> getRequestLoader =
            new LoaderManager.LoaderCallbacks<Order>() {
                @NonNull
                @Override
                public Loader<Order> onCreateLoader(int id, @Nullable Bundle args) {
                    String GET_URL = "http://grython.pythonanywhere.com/api/orders/" +
                            mSettings.getInt(PreferencesUtils.ORDER_ID, 0);
                    return  new MenuLoader<Order>(getContext(), GET_URL,
                            DATA_TYPE_ORDER_GET, getToken());                }

                @Override
                public void onLoadFinished(@NonNull Loader<Order> loader, Order data) {
                    emptyState.setVisibility(GONE);
                    Log.e(LOG_TAG, "Load is finished");
                    updateList(data);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Order> loader) {
                    order.clear();
                    mAdapter.notifyDataSetChanged();
                }
            };


    /**
     * Loader for the POST http request, which occurs when the user presses the order button for
     * the first time and a new order is created in the server database.
     */
    private android.support.v4.app.LoaderManager.LoaderCallbacks<Order> postRequestLoader =
            new LoaderManager.LoaderCallbacks<Order>() {
                @NonNull
                @Override
                public Loader<Order> onCreateLoader(int id, @Nullable Bundle args) {
                    String POST_URL = QueryUtils.BASE_URL + ORDER_URL_POST;
                    int restId = mSettings.getInt(PreferencesUtils.REST_ID, 0);
                    String restName = mSettings.getString(PreferencesUtils.REST_NAME, "");
                    Log.e(LOG_TAG, "rest name " + restName);
                    orderData = new Order(restId, paymentOption, 1, order);
                    return  new MenuLoader<Order>(getContext(), POST_URL, DATA_TYPE_ORDER_POST,
                            orderData, getToken());                }

                @Override
                public void onLoadFinished(@NonNull Loader<Order> loader, Order data) {
                    Log.e(LOG_TAG, "Load is finished");
                    emptyState.setVisibility(GONE);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.daoAccess().initializeMenu();
                        }
                    });
                    if (data != null) {
                        order.clear();
                        order.addAll(data.getItems());
                        mAdapter.notifyDataSetChanged();
                        PreferencesUtils.setOrderId(data.getOrderId(), getContext());
                        PreferencesUtils.setUpdateOrder(false, getContext());
                        PreferencesUtils.setTotal((int)data.getTotal(), getContext());
                        for (int i = 0; i<order.size(); i++) {
                            Log.e(LOG_TAG, "This is name after load " + order.get(i).getName());
                        }
                        mAdapter.updateTotal();

                    }
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Order> loader) {
                    order.clear();
                    mAdapter.notifyDataSetChanged();
                }
            };



    /**
     * Make the request.
     * @param loaderNum the number of the loader
     * @param loader the loader
     */
    private void makeRequest(int loaderNum, LoaderManager.LoaderCallbacks loader) {

        //check the network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if(QueryUtils.checkConnectivity(connectivityManager)) {
            emptyState.setVisibility(View.GONE);
            getLoaderManager().initLoader(loaderNum, null, loader);
        } else {
            beforeOrder.setVisibility(View.GONE);
            empty.setText(R.string.iconnection);
        }
    }

    /**
     * Listener for the order button.
     */
    private View.OnClickListener orderBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e(LOG_TAG, "This is IS_ORDERED " + mSettings.getBoolean(PreferencesUtils.IS_ORDERED, false));
            if (mSettings.getBoolean(PreferencesUtils.IS_ORDERED, false)) {
                if (dishesToUpdate() == null) {
                    Toast.makeText(getActivity(), "No items to add.", Toast.LENGTH_SHORT).show();
                } else {
                    makeRequest(POST_LOADER, patchRequestLoader);
                }
            } else {
                makeRequest(POST_LOADER, postRequestLoader);
                PreferencesUtils.setIsOrdered(true, getContext());
            }
            PreferencesUtils.setUpdateOrder(false, getContext());
        }
    };

    /**
     * Gets all the items in the list that needs to be included in the check, if there are none,
     * returns null.
     * @return the list of dishes that
     */
    private List<DishItem> dishesToUpdate() {
        List<DishItem> dishesToUpdate = new ArrayList<>();
        for (int i=0;i<order.size();i++) {
            if (order.get(i) instanceof Dish) {
                dishesToUpdate.add(order.get(i));
            }
        }

        if (dishesToUpdate.size() == 0) {
            return null;
        }
        return dishesToUpdate;
    }

    /**
     * Listener for shared preferences values.
     */
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    if(isAdded() && key.equals(PreferencesUtils.TOTAL_CURRENT_PRICE)) {
                        orderBtn.setBackgroundColor(getResources().getColor(R.color.holo_red_dark));
                       // PreferencesUtils.setUpdateOrder(true, getContext());
                        Log.e(LOG_TAG, "The preferences have been changed");
                    }
                }
            };

    /**
     * Listener for the radio Buttons.
     */
    private View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RadioButton r = (RadioButton) view;
            if (R.id.payment_type_cash == r.getId()) {
                paymentOption = 0;
            } else {
                paymentOption = 1;
            }
        }
    };

    /**
     * Update the list with data.
     * @param data the updated order
     */
    private void updateList(Order data) {
        if (data != null) {
            List<DishItem> temp = new ArrayList<>();
            for (int i = 0; i < order.size(); i++) {
                if (order.get(i) instanceof Dish) {
                    temp.add(order.get(i));
                }
            }
            order.clear();
            order.addAll(temp);
            order.addAll(data.getItems());
            mAdapter.notifyDataSetChanged();
            PreferencesUtils.setTotal((int)data.getTotal(), getContext());
            for (int i = 0; i<order.size(); i++) {
                Log.e(LOG_TAG, "This is name after load " + order.get(i).getName());
            }
            mAdapter.updateTotal();
        }
    }

    /**
     * Gets the token of the current user. If the user has invalid token, then the
     * {@link ActivityLogin} is started.
     * @return the token
     */
    private String getToken()  {
        String token = null;
        if (mSettings.getString(PreferencesUtils.USER_TOKEN, "")
                .equals(PreferencesUtils.INVALID_USER_TOKEN)) {
            Intent I = new Intent(getContext(), ActivityLogin.class);
            startActivity(I);
        } else {
            token = mSettings.getString(PreferencesUtils.USER_TOKEN, "");
        }
        return token;
    }



}
