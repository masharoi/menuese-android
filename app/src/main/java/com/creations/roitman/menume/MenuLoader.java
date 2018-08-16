package com.creations.roitman.menume;

import android.content.Context;
import android.util.Log;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.List;

/**
 * The Loader class for the menu.
 */

public class MenuLoader extends android.support.v4.content.AsyncTaskLoader<Menu> {

    private static final String LOG_TAG = MenuLoader.class.getName();
    private String url;

    /**
     * The public constructor.
     */
    public MenuLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public void onStartLoading() {
        Log.e(LOG_TAG, "Loader started loading");
        forceLoad();
    }

    @Override
    public Menu loadInBackground() {
        Log.e(LOG_TAG, "the data is loading");
        if (url == null) {
            return null;
        }
        return QueryUtils.fetchMenuData(this.url);
    }
}
