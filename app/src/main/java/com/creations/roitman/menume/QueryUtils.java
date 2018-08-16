package com.creations.roitman.menume;

import android.text.TextUtils;
import android.util.Log;

import com.creations.roitman.menume.data.Dish;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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

    private static String sampleMenu = "{\n" +
            "    \"id\": 4,\n" +
            "    \"name\": \"GrishJourno\",\n" +
            "    \"address\": \"Kuntsevo\",\n" +
            "    \"type\": {\n" +
            "        \"name\": \"Asian\"\n" +
            "    },\n" +
            "    \"image\": \"http://127.0.0.1:8000/media/default.png\",\n" +
            "    \"menu\": [\n" +
            "        {\n" +
            "            \"id\": 2,\n" +
            "            \"name\": \"SummerMenu\",\n" +
            "            \"dishes\": [\n" +
            "                {\n" +
            "                    \"id\": 2,\n" +
            "                    \"name\": \"Pasta Carbonara\",\n" +
            "                    \"ingredients\": \"Pasta, Bacon, Cream\",\n" +
            "                    \"description\": \"The best carbonara you have ever tried!\",\n" +
            "                    \"type\": {\n" +
            "                        \"name\": \"Pasta\"\n" +
            "                    }\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    private static String sampleMenuForOrder =
            "{\n" +
                    "    \"id\": 4,\n" +
                    "    \"name\": \"GrishJourno\",\n" +
                    "    \"address\": \"Kuntsevo\",\n" +
                    "    \"type\": {\n" +
                    "        \"name\": \"Asian\"\n" +
                    "    },\n" +
                    "    \"image\": \"http://127.0.0.1:8000/media/default.png\",\n" +
                    "    \"menu\": [\n" +
                    "        {\n" +
                    "            \"id\": 2,\n" +
                    "            \"name\": \"SummerMenu\",\n" +
                    "            \"dishes\": [\n" +
                    "                {\n" +
                    "                    \"id\": 2,\n" +
                    "                    \"name\": \"Pasta Carbonara\",\n" +
                    "                    \"ingredients\": \"Pasta, Bacon, Cream\",\n" +
                    "                    \"description\": \"The best carbonara you have ever tried!\",\n" +
                    "                    \"type\": {\n" +
                    "                        \"name\": \"Pasta\"\n" +
                    "                    }\n" +
                    "                }\n" +
                    "\n" +
                    "{\n" +
                    "                    \"id\": 3,\n" +
                    "                    \"name\": «Surf and Turf»,\n" +
                    "                    \"ingredients\": «Steak, Lobster, Salat»,\n" +
                    "                    \"description\": \"The best Lobster and Steak you have ever tried!\",\n" +
                    "                    \"type\": {\n" +
                    "                        \"name\": «Main»\n" +
                    "                    }\n" +
                    "                }\n" +
                    "\n" +
                    "{\n" +
                    "                    \"id\": 4,\n" +
                    "                    \"name\": «Beef Tartar»,\n" +
                    "                    \"ingredients\": «Beef, onion, garlic, egg»,\n" +
                    "                    \"description\": \"The best tartar you have ever tried!\",\n" +
                    "                    \"type\": {\n" +
                    "                        \"name\": «Starters»\n" +
                    "                    }\n" +
                    "                }\n" +
                    "\n" +
                    "\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";

    private static String sampleJSON = "[\n" +
            "    {\n" +
            "        \"id\": 3,\n" +
            "        \"name\": \"BurgerFirst\",\n" +
            "        \"address\": \"Arbat street 10\",\n" +
            "        \"image\": \"http://127.0.0.1:8000/media/default.png\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 4,\n" +
            "        \"name\": \"GrishJourno\",\n" +
            "        \"address\": \"Kuntsevo\",\n" +
            "        \"image\": \"http://127.0.0.1:8000/media/default.png\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 5,\n" +
            "        \"name\": \"FedyasCupCakes\",\n" +
            "        \"address\": \"Grabskaya street 97\",\n" +
            "        \"image\": \"http://127.0.0.1:8000/media/default.png\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 6,\n" +
            "        \"name\": \"New Restaurant\",\n" +
            "        \"address\": \"Arbat street 67\",\n" +
            "        \"image\": \"http://127.0.0.1:8000/media/default.png\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 7,\n" +
            "        \"name\": \"Sushi and Pasta\",\n" +
            "        \"address\": \"Bolshaya Ordynka 15\",\n" +
            "        \"image\": \"http://127.0.0.1:8000/media/restaurants_images/restoran-babel-v-1-m-nikoloschepovskom-pereulke_19f83_full-36554_qC8tzzS.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 10,\n" +
            "        \"name\": \"testImageResty\",\n" +
            "        \"address\": \"jkdajksd\",\n" +
            "        \"image\": \"http://127.0.0.1:8000/media/restaurants_images/restoran-babel-v-1-m-nikoloschepovskom-pereulke_19f83_full-36554_7SAlsue.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 11,\n" +
            "        \"name\": \"testImageResty2\",\n" +
            "        \"address\": \"nbm\",\n" +
            "        \"image\": \"http://127.0.0.1:8000/media/restaurants_images/default.png\"\n" +
            "    }\n" +
            "]";


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


                Dish outDish = new Dish(name, ingredients, desc);
                menuDishes.add(outDish);
            }

            output = new Menu(restName, menuDishes);


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the menu JSON results", e);
        }

        return output;
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

                finalList.add(new Restaurant(name, address, id));
            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the restaurant JSON results", e);
        }

        return finalList;
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
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
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
            Log.e(LOG_TAG, "Opened the connection");
            urlConnection.connect();
            Log.e(LOG_TAG, "Connected to the server");
            Log.e(LOG_TAG, "This is response code: " + urlConnection.getResponseCode());

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.e(LOG_TAG, "This is json: " + jsonResponse);
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
