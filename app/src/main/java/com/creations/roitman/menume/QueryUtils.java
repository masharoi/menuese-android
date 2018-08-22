package com.creations.roitman.menume;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.MenuDatabase;
import com.creations.roitman.menume.data.Order;
import com.creations.roitman.menume.viewModel.CustomViewModelFactory;
import com.creations.roitman.menume.viewModel.MainViewModel;
import com.creations.roitman.menume.viewModel.OrderViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * QueryUtils class deals with the queries that are sent via http request.
 */
public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Checks whether the device is connected to the internet.
     * @param connectivityManager manager
     * @return true if the device is connected to the internet
     */
    public static boolean checkConnectivity(ConnectivityManager connectivityManager) {

         return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .getState() == NetworkInfo.State.CONNECTED;

    }

    private static JSONArray convertDishesJSON(List<Dish> dishes) {
        JSONArray dishArray = new JSONArray();
        for (int i = 0; i < dishes.size(); i++) {
            JSONObject dish = new JSONObject();
            try {
                dish.put("dish", dishes.get(i).getDishId());
                dish.put("quantity", dishes.get(i).getQuantity());
                dishArray.put(dish);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return dishArray;
    }

    private static JSONObject createJSON(Order order, List<Dish> dishes) {

        JSONObject result = new JSONObject();
        try {
            result.put("restaurant", order.getRestId());
            result.put("payment_option", order.getPaymentOption());
            result.put("order_status", order.getOrderStatus());
            result.put("dishes", convertDishesJSON(dishes));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;


    }


    public static Order sendOrderData(String url, Order order, List<Dish> dishes) {
        URL urlClass = createUrl(url);
        JSONObject jsonToSend = createJSON(order, dishes);
        Order response = null;
        try {
            response = makePostHttpRequest(urlClass, jsonToSend);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making post request", e);
       }

        Log.e(LOG_TAG, jsonToSend.toString());

        return response;

    }
    /**
     * Fetches the restaurants data via http request.
     * @param requestUrl the target url
     * @return the list of restaurants
     */

    public static List<Restaurant> fetchRestaurantData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractRestaurantsFromJSON(jsonResponse);
    }


    /**
     * Fetches the menu data.
     * @param url the target url
     * @return the menu
     */

    public static Menu fetchMenuData(String url) {
        URL urlClass = createUrl(url);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(urlClass);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        return extractMenuFromJSON(jsonResponse);
    }

    /**
     * Extracts menu data from JSON.
     * @param menuJSON the json object
     * @return the menu
     */

    private static Menu extractMenuFromJSON(String menuJSON) {
        if (TextUtils.isEmpty(menuJSON)) {
            return null;
        }

        Menu output = new Menu();
        List<Dish> menuDishes = new ArrayList<Dish>();

        try {
            JSONObject response = new JSONObject(menuJSON);
            String restName =  response.getString("name");
            JSONArray menus =  response.optJSONArray("menu");
            JSONObject firstMenu = menus.getJSONObject(0);
            JSONArray dishes = firstMenu.optJSONArray("dishes");

            for (int i = 0; i < dishes.length(); i++) {
                JSONObject dish = dishes.getJSONObject(i);
                String name = dish.getString("name");
                String ingredients = dish.getString("ingredients");
                String desc = dish.getString("description");
                int id = dish.getInt("id");
                Double price = dish.getDouble("price");


                Dish outDish = new Dish(0, id, name, ingredients, desc, price);
                menuDishes.add(outDish);
            }

            output = new Menu(restName, menuDishes);


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the menu JSON results", e);
        }

        return output;
    }

    private static Order extractOrderFromJSON(String orderJSON) {
        if (TextUtils.isEmpty(orderJSON)) {
            return null;
        }
        Order order = null;
        try {
            JSONObject orderData = new JSONObject(orderJSON);
            int restId = orderData.getInt("restaurant");
            int paymentOpt = orderData.getInt("payment_option");
            int orderStat = orderData.getInt("order_status");
            order = new Order(restId, paymentOpt, orderStat);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return order;
    }


    /**
     * Extracts the restaurant data from JSON.
     * @param restaurantsJSON the JSON
     * @return the list of restaurants
     */

    private static List<Restaurant> extractRestaurantsFromJSON(String restaurantsJSON) {
        if (TextUtils.isEmpty(restaurantsJSON)) {
            return null;
        }

        List<Restaurant> finalList = new ArrayList<Restaurant>();

        try {
            JSONArray response = new JSONArray(restaurantsJSON);

            for (int i = 0; i < response.length(); i++) {
                JSONObject restaurant = response.getJSONObject(i);
                String name = restaurant.getString("name");
                String address = restaurant.getString("address");
                int id = restaurant.getInt("id");
                String url = restaurant.getString("image");

                finalList.add(new Restaurant(name, address, id, url));
            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the restaurant JSON results", e);
        }

        return finalList;
    }


    private static Order makePostHttpRequest(URL url, JSONObject jsonToSend) throws IOException {

        if (url == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        DataOutputStream os = null;
        InputStream inputStream = null;
        String jsonResponse = "";
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000000 /* milliseconds */);
            urlConnection.setConnectTimeout(1500000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Vary", "Accept");
            urlConnection.setRequestProperty("Authorization", "Token f9f753670794ed3b56b60ae7785eaf073ae3ea84");
            urlConnection.setRequestProperty("Allow", "POST, OPTIONS");
            urlConnection.setDoOutput(true);

            os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonToSend.toString());

            os.flush();
            Log.e("MSG" , urlConnection.getResponseMessage());

            if (urlConnection.getResponseCode() == 201) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.e(LOG_TAG, "This is json" + jsonResponse);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (os != null) {
                os.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return extractOrderFromJSON(jsonResponse);

    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000000 /* milliseconds */);
            urlConnection.setConnectTimeout(1500000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results." + url, e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


}
