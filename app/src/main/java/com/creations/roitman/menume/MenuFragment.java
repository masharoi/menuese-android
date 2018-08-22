package com.creations.roitman.menume;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.MenuDatabase;
import com.creations.roitman.menume.viewModel.MainViewModel;

import java.util.List;

import static android.content.SharedPreferences.*;


public class MenuFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Menu> {

    private static final String LOG_TAG = MenuFragment.class.getName();
    private static final String Menu_URL = "http://grython.pythonanywhere.com/api/restaurants/";
    private static final String DATA_TYPE = "menu";
    private TextView empty;
    private int restId;

    private DishesAdapter mAdapter;
    private RecyclerView dishList;
    private Menu menu = new Menu();

    private MenuDatabase mDb;
    private ImageButton ibutton;
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "myprefs";
    public static final String APP_PREFERENCES_HOME = "isMenu";
    public static final String APP_PREFERENCES_PRICE = "totalPrice";
    public static final String APP_PREFERENCES_RESTID = "restaurantID";


    private DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    Toast.makeText(getActivity(), "Yes is clicked", Toast.LENGTH_SHORT).show();
                    RestaurantFragment nextFrag= new RestaurantFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_fragment_holder, nextFrag,"findThisFragment")
                            .commit();
                    Editor editor = mSettings.edit();
                    editor.putBoolean(APP_PREFERENCES_HOME, false);
                    editor.putFloat(APP_PREFERENCES_PRICE, 0);
                    editor.apply();

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.daoAccess().deleteDish();
                        }
                    });
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    Toast.makeText(getActivity(), "No is clicked", Toast.LENGTH_SHORT).show();
                    break;
//                case Dialog.BUTTON_NEUTRAL:
//                    Toast.makeText(getActivity(), "Cancel is clicked", Toast.LENGTH_SHORT).show();
//                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dishes, container, false);

        Log.e(LOG_TAG, "the Menu fragment is created");

        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        mDb = MenuDatabase.getInstance(getActivity().getApplicationContext());

        ibutton =  (ImageButton) rootView.findViewById(R.id.exit_button);
        ibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Exit");
                adb.setMessage("Are you sure you want to exit? (All the menu data will be lost.)");
                adb.setIcon(android.R.drawable.ic_dialog_info);
                adb.setPositiveButton("YES", myClickListener);
                adb.setNegativeButton("NO", myClickListener);
//                adb.setNeutralButton("CANCEL", myClickListener);
                adb.create();
                adb.show();
            }
        });

        //set up the adapter and the recycler view
        dishList = (RecyclerView) rootView.findViewById(R.id.rv_dishes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        dishList.setLayoutManager(layoutManager);
        dishList.setHasFixedSize(true);

        mAdapter = new DishesAdapter(menu.getDishes(), new DishesAdapter.OnItemClickListener() {
            @Override public void onItemClick() {
                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
            }
        } );
        mAdapter.setDb(mDb);
        mAdapter.setSettings(mSettings);
        dishList.setAdapter(mAdapter);

        Bundle bundle = getArguments();
        //if the user has already chosen the restaurant
        if (bundle == null) {
            menu.getDishes().clear();
            MainViewModel vm = ViewModelProviders.of(this).get(MainViewModel.class);
            vm.getDishes().observe(this, new Observer<List<Dish>>() {
                @Override
                public void onChanged(@Nullable List<Dish> dishes) {
                    menu.getDishes().clear();
                    Log.e(LOG_TAG, "Receiving database update from LiveData in viewModel");
                    menu.getDishes().addAll(dishes);
                    mAdapter.notifyDataSetChanged();
                }
            });
            //the restaurant is not yet chosen
        } else {
            restId = bundle.getInt("REST_ID");
            //initialize the restaurant ID for the POST REQUEST in OrderFragment
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_RESTID, restId);
            editor.apply();
            //check the network connectivity
            ConnectivityManager connectivityManager = (ConnectivityManager)getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if(QueryUtils.checkConnectivity(connectivityManager)) {
                getLoaderManager().initLoader(1, null, this);
            } else {
                empty = rootView.findViewById(R.id.emptyStateMessage);
                empty.setText("No internet connection.");
            }
        }

        return rootView;
    }

    @NonNull
    @Override
    public Loader<Menu> onCreateLoader(int id, @Nullable Bundle args) {
        return new MenuLoader(getContext(), Menu_URL + String.valueOf(this.restId), DATA_TYPE);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Menu> loader, Menu data) {
        Log.e(LOG_TAG, "Load is finished");
        if (data != null) {
            menu.getDishes().clear();
            menu.getDishes().addAll(data.getDishes());
            mAdapter.notifyDataSetChanged();

            //save all the data to the local database
            for (int i = 0; i < menu.getDishes().size(); i++) {
                final Dish dish = menu.getDishes().get(i);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.daoAccess().insertDish(dish);
                    }
                });
            }

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Menu> loader) {
        Log.e(LOG_TAG, "Loader is reset");
        this.menu.getDishes().clear();
        mAdapter.notifyDataSetChanged();
    }
}
