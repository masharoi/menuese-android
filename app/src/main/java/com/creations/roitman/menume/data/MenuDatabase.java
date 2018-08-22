package com.creations.roitman.menume.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

/**
 * The local database for the items in the order.
 */

@Database(entities = {Dish.class, Order.class}, version = 4, exportSchema = false)
public abstract class MenuDatabase extends RoomDatabase {

    private static final String LOG_TAG = MenuDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "dishesList";
    private static MenuDatabase sInstance;

    public static MenuDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MenuDatabase.class, MenuDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract DaoAccess daoAccess();
}
