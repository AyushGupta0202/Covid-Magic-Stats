package com.eggdevs.covidquerytester;

import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// Query class to fetch covid data from the url.
public class QueryFetchCovidData {

    private static final String LOG_TAG = QueryFetchCovidData.class.getSimpleName();

    // private constructor so no one can make its object.
    private QueryFetchCovidData() {}

    // Method to fetch the data.
    public static List<PeopleData> fetchQueryData(String apiUrl) {

        // Create URL object
        URL url = createUrl(apiUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);

        }
        // Extract relevant fields from the JSON response and create a list of {@link CovidData}s
        // Return the list of {@link CovidData}s
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url;
        url = null;
        try {
            url = new URL(stringUrl);

        }
        catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
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
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            }
            else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the covid19 JSON results.", e);

        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * <p>
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link PeopleData} objects that has been built up from
     * <p>
     * parsing the given JSON response.
     */
    private static List<PeopleData> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding state data to
        List<PeopleData> peopleDataList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // Extract the JSONArray associated with the key called "statewise",
            // which represents a list of features (or states).
            JSONArray statewiseArray = baseJsonResponse.getJSONArray("statewise");

            // For each state in the statewiseArray, create an {@link state} object.

            for (int i = 0; i < statewiseArray.length(); i++) {

                // Get a single state at position i within the list of states.
                JSONObject currentState = statewiseArray.getJSONObject(i);

                // Extract the value for the key called "active"
                String activeCases = currentState.getString("active");

                // Confirmed cases.
                String confirmedCases = currentState.getString("confirmed");

                // last updated.
                String lastUpdatedTime = currentState.getString("lastupdatedtime");

                // deaths.
                String peopleDied = currentState.getString("deaths");

                // people recovered.
                String peopleRecovered = currentState.getString("recovered");

                // name of state.
                String stateName = currentState.getString("state");

                //Adding data of each state to the peopleDataList.
                PeopleData currentData = new PeopleData("Active cases : " + activeCases,
                        "Last updated : " + lastUpdatedTime, stateName,
                        "People recovered : " + peopleRecovered,
                        "People died : " + peopleDied,
                        "Total confirmed cases : " + confirmedCases);

                peopleDataList.add(currentData);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of states.
        return peopleDataList;
    }
}
