package com.creations.roitman.menume;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.MenuDatabase;
import com.creations.roitman.menume.utilities.PreferencesUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final String TAG_FRAGMENT_HOME = "tag_frag_home";
    private static final String TAG_FRAGMENT_PROFILE = "tag_frag_profile";
    private static final String TAG_FRAGMENT_MENU = "tag_frag_menu";
    private static final String TAG_FRAGMENT_ORDER = "tag_frag_order";
    private List<Fragment> fragments = new ArrayList<>(3);

    private static final String LOG_TAG = MainActivity.class.getName();

    private MenuDatabase mDb;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = MenuDatabase.getInstance(getApplicationContext());
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (!mSettings.getBoolean(PreferencesUtils.IS_REST_CHOSEN, true)) {
                    mDb.daoAccess().deleteDish();
                }
            }
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bNListener);
        buildFragmentsList();

        // Set the 0th Fragment to be displayed by default.
        if (mSettings.getBoolean(PreferencesUtils.IS_REST_CHOSEN, true)) {
            switchFragment(1, TAG_FRAGMENT_MENU);
        } else {
            switchFragment(0,  TAG_FRAGMENT_HOME);
        }


    }


    /**
     * Builds the list of fragments.
     */
    private void buildFragmentsList() {
        Fragment homeFragment = buildFragment("HOME");
        Fragment menuFragment = buildFragment("MENU");
        Fragment orderFragment = buildFragment("ORDER");
        Fragment profileFragment = buildFragment("PROFILE");

        fragments.add(homeFragment);
        fragments.add(menuFragment);
        fragments.add(orderFragment);
        fragments.add(profileFragment);

    }

    /**
     * Creates the fragment.
     * @param type of the fragment
     * @return the fragment
     */
    private Fragment buildFragment(String type) {
        switch (type) {
            case "PROFILE":
                return new ProfileFragment();
            case "ORDER":
                return new OrderFragment();
            case "MENU":
                return new MenuFragment();
            default:
                return new RestaurantFragment();
        }
    }

    /**
     * Switches the fragments in the activity.
     * @param pos of the fragment in the list
     * @param tag of the fragment
     */
    private void switchFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_holder, fragments.get(pos), tag)
                .commit();
    }

    /**
     * Listener for the Bottom navigation view.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener bNListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottombaritem_home:
                    if (mSettings.getBoolean(PreferencesUtils.IS_REST_CHOSEN, true)) {
                        switchFragment(1, TAG_FRAGMENT_MENU);
                    } else  {
                        switchFragment(0,  TAG_FRAGMENT_HOME);
                        Toast.makeText(MainActivity.this,
                                "Your Message", Toast.LENGTH_LONG).show();
                    }
                    return true;
                case R.id.bottombaritem_profile:
                    Log.e(LOG_TAG, "HERE");
                    switchFragment(3, TAG_FRAGMENT_PROFILE);
                    return true;
                case R.id.bottombaritem_receipt:
                    switchFragment(2, TAG_FRAGMENT_ORDER);
                    return true;
            }
            return false;
        }
    };


}
