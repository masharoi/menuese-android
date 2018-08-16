package com.creations.roitman.menume.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * The local database for the items in the order.
 */

@Database(entities = {Dish.class}, version = 1, exportSchema = false)
public abstract class MenuDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();
}
