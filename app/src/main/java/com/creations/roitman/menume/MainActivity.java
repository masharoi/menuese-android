package com.creations.roitman.menume;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final String TAG_FRAGMENT_HOME = "tag_frag_home";
    private static final String TAG_FRAGMENT_MENU = "tag_frag_menu";
    private static final String TAG_FRAGMENT_ORDER = "tag_frag_order";
    private List<Fragment> fragments = new ArrayList<>(3);

    private boolean isRestaurantChosen = false;


    private void buildFragmentsList() {
        Fragment homeFragment = buildFragment("HOME");
        Fragment menuFragment = buildFragment("MENU");
        Fragment orderFragment = buildFragment("ORDER");

        fragments.add(homeFragment);
        fragments.add(menuFragment);
        fragments.add(orderFragment);
    }

    private Fragment buildFragment(String type) {
        if (type == "MENU") {
            return new MenuFragment();
        }
        Fragment fragment = new RestaurantFragment();
        Bundle bundle = new Bundle();
        bundle.putString(RestaurantFragment.ARG_TITLE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void switchFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_holder, fragments.get(pos), tag)
                .commit();
    }

    public void resetRestaurantChosen() {
        this.isRestaurantChosen = !isRestaurantChosen;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottombaritem_home:
                                if (isRestaurantChosen) {
                                    switchFragment(1, TAG_FRAGMENT_MENU);
                                } else  {
                                    switchFragment(0,  TAG_FRAGMENT_HOME);
                                    Toast.makeText(MainActivity.this,
                                            "Your Message", Toast.LENGTH_LONG).show();
                                }
                                return true;
                            case R.id.bottombaritem_menu:
                                switchFragment(1, TAG_FRAGMENT_MENU);
                                return true;
                            case R.id.bottombaritem_receipt:
                                switchFragment(2, TAG_FRAGMENT_ORDER);
                                return true;
                        }
                        return false;
                    }
                });
        buildFragmentsList();

        // Set the 0th Fragment to be displayed by default.
        switchFragment(0, TAG_FRAGMENT_HOME);

    }

}
