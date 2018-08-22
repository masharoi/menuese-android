package com.creations.roitman.menume.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.MenuDatabase;
import com.creations.roitman.menume.data.Order;

public class OrderViewModel extends ViewModel {

    private LiveData<Order> order;

    public OrderViewModel(MenuDatabase db, int id) {
        order = db.daoAccess().loadOrderById(id);
    }

    public LiveData<Order> getOrder() {
        return order;
    }
}
