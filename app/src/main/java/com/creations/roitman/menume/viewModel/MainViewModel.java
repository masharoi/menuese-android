package com.creations.roitman.menume.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.MenuDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainViewModel.class.getSimpleName();
    private LiveData<List<Dish>> dishes;
    private LiveData<List<Dish>> order;

    public MainViewModel(@NonNull Application application) {
        super(application);
        MenuDatabase db = MenuDatabase.getInstance(this.getApplication());
        Log.e(LOG_TAG, "The data is loading from the database");
        dishes = db.daoAccess().fetchAllDishFromTheMenu();
        order = db.daoAccess().fetchDishFromOrder();
    }

    public LiveData<List<Dish>> getDishes() {
        return dishes;
    }

    public LiveData<List<Dish>> getOrder() {
        return order;
    }
}
