package com.creations.roitman.menume;

import android.content.Context;
import android.util.Log;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.Order;
import com.creations.roitman.menume.data.User;
import com.creations.roitman.menume.utilities.QueryUtils;

import java.util.List;

/**
 * The Loader class that gets the data about the available restaurants.
 */

public class MenuLoader<D> extends android.support.v4.content.AsyncTaskLoader<D> {

    private static final String LOG_TAG = MenuLoader.class.getName();
    private String url;
    private Order order;
    private String DATA_TYPE;
    private User user;
    private String token;

    /**
     * The public constructor for the queries that require tokens.
     */
    public MenuLoader(Context context, String url, String type, String token) {
        super(context);
        this.url = url;
        this.DATA_TYPE = type;
        this.token = token;
    }

    /**
     * The public constructor.
     */
    public MenuLoader(Context context, String url, String type) {
        super(context);
        this.url = url;
        this.DATA_TYPE = type;
    }

    /**
     * The public constructor for order.
     */
    public MenuLoader(Context context, String url, String type, Order order, String token) {
        super(context);
        this.url = url;
        this.DATA_TYPE = type;
        this.order = order;
        this.token = token;
    }

    /**
     * The public constructor for sign up.
     */
    public MenuLoader(Context context, String url, String type, User user) {
        super(context);
        this.url = url;
        this.DATA_TYPE = type;
        this.user = user;
    }


    @Override
    public void onStartLoading() {
        Log.e(LOG_TAG, "Loader started loading");
        forceLoad();
    }

    @Override
    public D loadInBackground() {
        Log.e(LOG_TAG, "the data is loading");
        if (url == null) {
            return null;
        }
        switch (DATA_TYPE) {
            case "restaurant":
                return (D) QueryUtils.fetchRestaurantData(this.url);
            case "menu":
                return (D) QueryUtils.fetchMenuData(this.url);
            case OrderFragment.DATA_TYPE_ORDER_GET:
                return (D) QueryUtils.fetchReceiptData(this.url, this.token);
            case ProfileFragment.PROFILE_TYPE:
                return (D) QueryUtils.fetchOrders(this.url, this.token);
            default:
                return (D) QueryUtils.sendOrderData(this.url, this.order, this.token);
        }
    }
}