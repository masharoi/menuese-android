package com.creations.roitman.menume.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * This the database access interface that contains methods for the CRUD operations.
 */

@Dao
public interface DaoAccess {

    @Insert
    void insertDish(Dish dish);

    @Query("SELECT * FROM Dish")
    List<Dish> fetchAllDishFromTheOrder();

}
