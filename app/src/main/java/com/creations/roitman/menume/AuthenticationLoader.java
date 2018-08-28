package com.creations.roitman.menume;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import com.creations.roitman.menume.data.User;
import com.creations.roitman.menume.utilities.QueryUtils;

public class AuthenticationLoader extends AsyncTaskLoader<String> {
    private static final String LOG_TAG = AuthenticationLoader.class.getName();
    String DATA_TYPE;
    User user;
    String url;

    public AuthenticationLoader(@NonNull Context context, String url, String type, User user) {
        super(context);
        this.DATA_TYPE = type;
        this.url = url;
        this.user = user;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return QueryUtils.sendUserData(this.url, this.user);

    }

    @Override
    public void onStartLoading() {
        Log.e(LOG_TAG, "Loader started loading");
        forceLoad();
    }
}
