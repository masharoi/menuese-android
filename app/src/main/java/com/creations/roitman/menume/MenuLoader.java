package com.creations.roitman.menume;

import android.content.Context;
import android.util.Log;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.Order;

import java.util.List;

/**
 * The Loader class that gets the data about the available restaurants.
 */

public class MenuLoader<D> extends android.support.v4.content.AsyncTaskLoader<D> {

    private static final String LOG_TAG = MenuLoader.class.getName();
    private String url;
    private Order order;
    private List<Dish> dishes;
    private String DATA_TYPE;

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
    public MenuLoader(Context context, String url, String type, Order order, List<Dish> dishes) {
        super(context);
        this.url = url;
        this.DATA_TYPE = type;
        this.order = order;
        this.dishes = dishes;
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
            default:
                return (D) QueryUtils.sendOrderData(this.url, this.order, this.dishes);
        }
    }
}