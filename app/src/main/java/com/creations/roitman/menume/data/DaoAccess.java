package com.creations.roitman.menume.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * This the database access interface that contains methods for the CRUD operations.
 */

@Dao
public interface DaoAccess {

    @Query("SELECT * FROM `ORDER` WHERE orderId =:id")
    LiveData<Order> loadOrderById(int id);

    @Query("SELECT * FROM DISH WHERE dishId =:id")
    LiveData<Dish> loadDishByID(int id);


    @Update
    void updateDish(Dish dish);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDish(Dish dish);

    @Query("SELECT * FROM Dish")
    LiveData<List<Dish>> fetchAllDishFromTheMenu();

    @Query("Delete from Dish")
    void deleteDish();

    @Query("SELECT * FROM DISH WHERE QUANTITY != 0")
    LiveData<List<Dish>> fetchDishFromOrder();

}
