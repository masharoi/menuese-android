package com.creations.roitman.menume;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * The Loader class that gets the data about the available restaurants.
 */

public class RestaurantLoader<D> extends android.support.v4.content.AsyncTaskLoader<List<Restaurant>> {

    private static final String LOG_TAG = RestaurantLoader.class.getName();
    private String url;

    /**
     * The public constructor.
     */
    public RestaurantLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public void onStartLoading() {
        Log.e(LOG_TAG, "Loader started loading");
        forceLoad();
    }

    @Override
    public List<Restaurant> loadInBackground() {
        Log.e(LOG_TAG, "the data is loading");
        if (url == null) {
            return null;
        }
        return QueryUtils.fetchRestaurantData(this.url);
    }
}