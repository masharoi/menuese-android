package com.creations.roitman.menume.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * This is the utility class that contains static methods to update values in SharedPreferences.
 */
public class PreferencesUtils {

    public static final String IS_REST_CHOSEN = "isMenu";
    public static final String REST_ID = "restaurantID";
    public static final String UPDATE_ORDER = "isUpdateDue";
    public static final String TOTAL_CURRENT_PRICE = "totalPrice";
    public static final String IS_ORDERED = "isOrdered";
    public static final String ORDER_ID = "orderId";
    public static final String REST_NAME = "restName";
    public static final String TOTAL_PRICE = "totalPriceInCheck";



    /**
     * Set the total price of the current order.
     * @param total price of the order
     * @param c context
     */
    public static void setTotal(int total, Context c) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(TOTAL_PRICE, total);
        editor.apply();
    }


    /**
     * Set the id of the current order.
     * @param id of the order
     * @param c context
     */
    public static void setOrderId(int id, Context c) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(ORDER_ID, id);
        editor.apply();
    }

    /**
     * Set whether the restaurant is chosen in shared preferences in order to show the right fragment
     * in the first tab.
     * @param bool boolean to set in shared preferences
     * @param c context
     */
    public static void setRestaurantChosen(boolean bool, Context c) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(IS_REST_CHOSEN, bool);
        editor.apply();
    }

    /**
     * Set the restaurant ID for the POST REQUEST in OrderFragment.
     * @param i id of the restaurant
     * @param c context
     */
    public static void setRestId(int i, Context c) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(REST_ID, i);
        editor.apply();
    }

    /**
     * Sets true if the user changed order and a new update is needed.
     * @param b boolean that represents whether a new update is needed
     * @param c context
     */
    public static void setUpdateOrder(boolean b, Context c) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(UPDATE_ORDER, b);
        editor.apply();
    }

    public static void setIsOrdered(boolean b, Context c) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(IS_ORDERED, b);
        editor.apply();
    }

    public static void setRestName(String restName, Context c) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(REST_NAME, restName);
        editor.apply();
    }
}
