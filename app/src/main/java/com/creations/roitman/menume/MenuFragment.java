package com.creations.roitman.menume;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


public class MenuFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Menu> {

    private static final String LOG_TAG = RestaurantFragment.class.getName();
    private static final String Menu_URL = "http://grython.pythonanywhere.com/api/restaurants/";
    private TextView empty;
    private int restId;

    private DishesAdapter mAdapter;
    private RecyclerView dishList;
    private Menu menu = new Menu();

    private ImageButton ibutton;

    private DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    Toast.makeText(getActivity(), "Yes is clicked", Toast.LENGTH_SHORT).show();
                    RestaurantFragment nextFrag= new RestaurantFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_fragment_holder, nextFrag,"findThisFragment")
                            .commit();
                    ((MainActivity) getActivity()).resetRestaurantChosen();
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

        Bundle bundle = getArguments();
        restId = bundle.getInt("REST_ID");

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

        dishList = (RecyclerView) rootView.findViewById(R.id.rv_dishes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        dishList.setLayoutManager(layoutManager);
        dishList.setHasFixedSize(true);

        mAdapter = new DishesAdapter(menu.getDishes(), new DishesAdapter.OnItemClickListener() {
            @Override public void onItemClick() {
                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
            }
        });
        dishList.setAdapter(mAdapter);

        //check the network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .getState() == NetworkInfo.State.CONNECTED) {
            getLoaderManager().initLoader(1, null, this);
        } else {
            empty = rootView.findViewById(R.id.emptyStateMessage);
            empty.setText("No internet connection.");
        }
        return rootView;
    }

    @NonNull
    @Override
    public Loader<Menu> onCreateLoader(int id, @Nullable Bundle args) {
        return new MenuLoader(getContext(), Menu_URL + String.valueOf(this.restId));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Menu> loader, Menu data) {
        Log.e(LOG_TAG, "Load is finished");
        if (data != null) {
            menu.getDishes().clear();
            menu.getDishes().addAll(data.getDishes());
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Menu> loader) {

    }
}
