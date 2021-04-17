package com.eggdevs.covidquerytester;
/**
 * Covid 19 data fetching Project by the Ayush Gupta.
 * Date - 11-07-2020.
 * Time - 17:00
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CovidAdapter mAdapter;
    private View loadingScreen;
    private ListView peopleList;

    // The url to make HTTP request and fetch JSON data.
    private static final String API_URL = "https://api.covid19india.org/data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Loading screen before data loads.
        loadingScreen = findViewById(R.id.loading_progress);

        // List view that holds all the data to display.
        peopleList = findViewById(R.id.list_detail);

        // Adapter used to populate view of list view.
        mAdapter = new CovidAdapter(this, new ArrayList<PeopleData>());

        // Setting data in adapter to the list view.
        peopleList.setAdapter(mAdapter);

        // Running data fetch task in background.
        new FetchDataClass().execute(API_URL);
    }

    // Background thread to fetch data.
    public class FetchDataClass extends AsyncTask<String, Void, List<PeopleData>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Making listView gone while the async task executes.
            peopleList.setVisibility(View.GONE);
            loadingScreen.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<PeopleData> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            // Result will go on to on post execute.
            return QueryFetchCovidData.fetchQueryData(urls[0]);
        }

        @Override
        protected void onPostExecute(List<PeopleData> peopleData) {
            super.onPostExecute(peopleData);
            // Clear the adapter of previous data
            mAdapter.clear();
            // If there is a valid list of {@link peopleData}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (peopleData != null && !peopleData.isEmpty()) {
                mAdapter.addAll(peopleData);
                loadingScreen.setVisibility(View.GONE);
                // List view visible again.
                peopleList.setVisibility(View.VISIBLE);
            }
        }
    }
}