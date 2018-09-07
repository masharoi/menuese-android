package com.creations.roitman.menume.utilities;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.creations.roitman.menume.data.DishItem;
import com.creations.roitman.menume.data.Menu;
import com.creations.roitman.menume.data.OrderedDish;
import com.creations.roitman.menume.data.Restaurant;
import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.Order;
import com.creations.roitman.menume.data.User;

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
    public static final String BASE_URL = "http://grython.pythonanywhere.com/";
    private static String token;

    //MAIN METHODS

    // POST REQUESTS
    /**
     * Send the user data via post request.
     * @param url the url of the request
     * @param user the data
     * @return the token of the user
     */
    public static String sendUserData(String url, User user) {
        URL urlClass = createUrl(url);
        JSONObject jsonToSend = convertUserJSON(user);
        String response = null;
        String token = null;
        Log.e(LOG_TAG, "This is the url: " + url);
        Log.e(LOG_TAG, "This is json to send: " + jsonToSend);
        try {
            response = makePostHttpRequest(urlClass, jsonToSend);
            Log.e(LOG_TAG, response);
            JSONObject userData = new JSONObject(response);
            token = userData.getString("token");
        } catch (IOException | JSONException e) {
            Log.e(LOG_TAG, "Error making post request", e);
        }

        return token;
    }

    /**
     * Send the Order data via the post request.
     * @param url the url where to send the data to
     * @param order the data
     * @param t token for authorization
     * @return the order that the user is currently updating
     */
    public static Order sendOrderData(String url, Order order, String t) {
        token = t;
        URL urlClass = createUrl(url);
        JSONObject jsonToSend = convertOrderJSON(order);
        JSONObject jsonObj = null;
        Log.e(LOG_TAG, "This is url " + url);
        Log.e(LOG_TAG, "This is json to send: " + jsonToSend);
        try {
            String jsonResponse = makePostHttpRequest(urlClass, jsonToSend);
            Log.e(LOG_TAG, "This is the json resp " + jsonResponse);
            jsonObj = new JSONObject(jsonResponse);
        } catch (IOException | JSONException e) {
            Log.e(LOG_TAG, "Error making post request", e);
        }
        return extractOrderFromJSON(jsonObj);

    }

    // GET REQUESTS

    /**
     * Makes the get request to receive the order that is already in the receipt.
     * @param url the url for the request
     * @param tk the token of the user
     * @return the order
     */
    public static Order fetchReceiptData(String url, String tk) {
        token = tk;
        URL urlClass = createUrl(url);
        JSONObject jsonObj = null;
        Log.e(LOG_TAG, "this is url " + url);
        try {
            String jsonResponse = makeHttpRequest(urlClass);
            Log.e(LOG_TAG, "JSON RESPONSE: " + jsonResponse);
            jsonObj = new JSONObject(jsonResponse);

        } catch (IOException | JSONException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractOrderFromJSON(jsonObj);
    }

    /**
     * Make the get request to receive all the orders of the user.
     * @param url the url for the request
     * @param t the token of the user
     * @return the list of orders
     */
    public static List<Order> fetchOrders(String url, String t) {
        token = t;
        URL urlClass = createUrl(url);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(urlClass);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        Log.e(LOG_TAG, "json response " + jsonResponse);
        List<Order> orders = extractOrders(jsonResponse);
        Log.e(LOG_TAG, "the orders have just been extracted " + orders.toString());
        return orders;
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

    // PATCH

    /**
     * Send the updated information about the order.
     * @param url the url of the request
     * @param dishes the dishes to add to the order
     * @param tk the token of the user
     * @return the updated version of the order
     */
    public static Order sendOrderPatch(String url, List<DishItem> dishes, String tk) {
        token = tk;
        URL urlClass = createUrl(url);
        String jsonResponse = null;
        JSONObject jsonObj = null;
        JSONArray jsonToSend = convertDishesJSON(dishes);
        try {
            JSONObject json = new JSONObject();
            json.put("items", jsonToSend);
            jsonResponse = makePatchHttpRequest(urlClass, json);
            jsonObj = new JSONObject(jsonResponse);
        } catch (IOException | JSONException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        return extractOrderFromJSON(jsonObj);
    }


    // CONVERSIONS

    // FROM CLASSES TO JSON


    /**
     * Given a list of dishes, return a json representation of that list.
     * @param dishes the dishes to convert
     * @return the json representation
     */
    private static JSONArray convertDishesJSON(List<DishItem> dishes) {
        JSONArray dishArray = new JSONArray();
        for (int i = 0; i < dishes.size(); i++) {
            JSONObject dish = new JSONObject();
            try {
                dish.put("dish", ((Dish)dishes.get(i)).getDishId());
                dish.put("quantity", dishes.get(i).getQuantity());
                dishArray.put(dish);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return dishArray;
    }

    /**
     * Given {@link Order}, convert it to JSON.
     * @param order the order to convert
     * @return the JSON representation of the dish
     */
    private static JSONObject convertOrderJSON(Order order) {

        JSONObject result = new JSONObject();
        try {
            result.put("restaurant", order.getRestId());
            result.put("payment_option", order.getPaymentOption());
            result.put("order_status", order.getOrderStatus());
            result.put("table", 2);
            result.put("items", convertDishesJSON(order.getItems()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;


    }


    /**
     * Represent the user data in form of JSON.
     * @param user the data
     * @return the JSONObject, which represents the user
     */
    private static JSONObject convertUserJSON(User user) {
        JSONObject result = new JSONObject();
        try {
            if (user.getUsername() != null) {
                result.put("username", user.getUsername());
            }
            result.put("password", user.getPassword());
            result.put("email", user.getMail());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

    }


    // FROM JSON TO CLASSES


    /**
     * Convert the JSON representation of the List of Orders into {@link List<Order>}
     * @param jsonResponse the json to convert to orders
     * @return the list of orders
     */
    private static List<Order> extractOrders(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<Order> orders = new ArrayList<>();
        try {
            JSONArray ordersJSON = new JSONArray(jsonResponse);
            for (int i = 0; i < ordersJSON.length(); i++) {
                JSONObject orderJSON = ordersJSON.getJSONObject(i);
                Order order = extractOrderFromJSON(orderJSON);
                orders.add(order);
                Log.e(LOG_TAG, "This is single order " + order.getRestName());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return orders;

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
        List<DishItem> menuDishes = new ArrayList<DishItem>();

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

    /**
     * Convert a single order from json representation into Order
     * @param orderData the json representation
     * @return the order
     */
    private static Order extractOrderFromJSON(JSONObject orderData) {
        if (orderData == null) {
            return null;
        }
        Log.e(LOG_TAG, "This is orderJSON " + orderData);
        List<DishItem> order = new ArrayList<>();
        Order result = null;
        try {
            double totalPrice = orderData.getDouble("total_price");
            String restName = orderData.getString("restaurant");
            int id = orderData.getInt("id");
            String timestamp = orderData.getString("timestamp");
            int paymentOpt = orderData.getInt("payment_option");
            int orderStat = orderData.getInt("order_status");
            JSONArray items = orderData.optJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject dishData = items.getJSONObject(i);
                JSONObject dish = dishData.getJSONObject("dish");
                String name = dish.getString("name");
                double price = dish.getDouble("price");
                int stat = dishData.getInt("status");
                int quant = dishData.getInt("quantity");
                OrderedDish resultDish = new OrderedDish(stat, name, quant, price);
                order.add(resultDish);
            }

            result = new Order(id, restName, paymentOpt, orderStat, order, totalPrice, timestamp);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing order data");
            e.printStackTrace();
        }

        return result;
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


    // REQUESTS


    private static String makePatchHttpRequest(URL urlClass, JSONObject jsonToSend) throws IOException {

        if (urlClass == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        DataOutputStream os = null;
        InputStream inputStream = null;
        String jsonResponse = "";

        try {
            urlConnection = (HttpURLConnection) urlClass.openConnection();
            urlConnection.setReadTimeout(1000000 /* milliseconds */);
            urlConnection.setConnectTimeout(1500000 /* milliseconds */);
            urlConnection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Vary", "Accept");
            if (token != null) {
                urlConnection.setRequestProperty("Authorization", "Token " + token);
            }
            urlConnection.setRequestProperty("Allow", "POST, OPTIONS");
            urlConnection.setDoOutput(true);

            os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonToSend.toString());

            os.flush();
            Log.e("MSG" , urlConnection.getResponseMessage());

            if (urlConnection.getResponseCode() == 200) {
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
            token = null;
        }

        return jsonResponse;
    }

    /**
     * Make the post request.
     * @param url the url for the request
     * @param jsonToSend the json to post
     * @return response in form of json
     * @throws IOException
     */
    private static String makePostHttpRequest(URL url, JSONObject jsonToSend) throws IOException {

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
            if (token != null) {
                urlConnection.setRequestProperty("Authorization", "Token " + token);
            }
            urlConnection.setRequestProperty("Allow", "POST, OPTIONS");
            urlConnection.setDoOutput(true);

            os = new DataOutputStream(urlConnection.getOutputStream());
            os.writeBytes(jsonToSend.toString());

            os.flush();
            Log.e("MSG" , urlConnection.getResponseMessage());
            if (urlConnection.getResponseCode() == 201 || urlConnection.getResponseCode() == 200) {
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
            token = null;
        }

        return jsonResponse;

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
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Vary", "Accept");
            urlConnection.setRequestProperty("Allow", "GET, POST, HEAD, OPTIONS");
            if (token != null) {
                urlConnection.setRequestProperty("Authorization", "Token " + token);
            }
            urlConnection.connect();
            Log.e(LOG_TAG, "I am here");

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
            token = null;
        }
        return jsonResponse;
    }

    // HELPER

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
