package com.creations.roitman.menume;

import android.content.Context;
import android.util.Log;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.DishItem;
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
    private String token;
    private List<DishItem> dishes;

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


    public MenuLoader(Context context, String patch_url, String type,
                      List<DishItem> dishes, String token) {
        super(context);
        this.url = patch_url;
        this.DATA_TYPE = type;
        this.dishes = dishes;
        this.token = token;
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
            case RestaurantFragment.DATA_TYPE:
                return (D) QueryUtils.fetchRestaurantData(this.url);
            case MenuFragment.DATA_TYPE:
                return (D) QueryUtils.fetchMenuData(this.url);
            case OrderFragment.DATA_TYPE_ORDER_GET:
            case ReceiptFragment.PROFILE_SINGLE_TYPE:
                return (D) QueryUtils.fetchReceiptData(this.url, this.token);
            case ProfileFragment.PROFILE_TYPE:
                return (D) QueryUtils.fetchOrders(this.url, this.token);
            case OrderFragment.DATA_TYPE_ORDER_PATCH:
                return (D) QueryUtils.sendOrderPatch(this.url, this.dishes, this.token);
            default:
                return (D) QueryUtils.sendOrderData(this.url, this.order, this.token);
        }
    }
}