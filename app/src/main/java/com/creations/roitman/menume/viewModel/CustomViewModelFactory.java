//package com.creations.roitman.menume.viewModel;
//
//import android.arch.lifecycle.ViewModel;
//import android.arch.lifecycle.ViewModelProvider;
//
//import com.creations.roitman.menume.data.MenuDatabase;
//
//public class CustomViewModelFactory extends ViewModelProvider.NewInstanceFactory {
//
//    private final MenuDatabase mDb;
//    private final int id;
//
//    public CustomViewModelFactory(MenuDatabase mDb, int id) {
//        this.mDb = mDb;
//        this.id = id;
//    }
//
//    @Override
//    public <T extends ViewModel> T create(Class<T> modelClass) {
//        return (T) new OrderViewModel(mDb, id);
//    }
//
//}
