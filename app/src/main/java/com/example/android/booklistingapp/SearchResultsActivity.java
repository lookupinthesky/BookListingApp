package com.example.android.booklistingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


public class SearchResultsActivity extends AppCompatActivity {

    public static String QUERY;
    public static final String LOG_TAG = SearchListAdapter.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ListView list = (ListView) findViewById(R.id.list);
        TextView empty_view = (TextView) findViewById(R.id.empty_view);
        list.setEmptyView(empty_view);
        //retrieving query as typed by the user
        Bundle bundle = getIntent().getExtras();
        QUERY = bundle.getString("query");
        if (QUERY == null) {
            Log.v(QUERY, "No value received");
        }

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
        BackgroundProcessing task = new BackgroundProcessing();
        task.execute();}
        else {empty_view.setText(getResources().getString(R.string.no_internet));}


    }

    //extending asynctask to perform network operations on the background thread
    private class BackgroundProcessing extends AsyncTask<URL, Void, ArrayList<SearchResultsItem>> {

        @Override
        protected ArrayList<SearchResultsItem> doInBackground(URL... urls) {

            URL url = createURL(QUERY);
            String jsonResponse = "";
            try {
                jsonResponse = makeHTTPRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }
            QueryData queryData = new QueryData();

            ArrayList<SearchResultsItem> finalList = queryData.extractSearchArray(jsonResponse);

            return finalList;
        }

        //updates the UI after the execution is complete
        @Override
        protected void onPostExecute(ArrayList<SearchResultsItem> finalList) {
            if (finalList == null)
                return;
            else updateUI(finalList);
        }
    }

    //method to create the URL
    public URL createURL(String query) {


        String part1 = "https://www.googleapis.com/books/v1/volumes?q=";
        String part2 = "&maxResults=10&key=AIzaSyCBI44kHpm0dD0yWmHGKW1a0dOUX-R-OoM";

        while (query.indexOf(" ") != -1) {
            int i = query.indexOf(" ");
            String _query = query.substring(0, i - 1);
            String _add = "%20";
            String _query1 = query.substring(i + 1, query.length() - 1);
            query = _query + _add + _query1;
        }

        query = part1.concat(query).concat(part2);
        Log.d(SearchResultsActivity.class.getName(), "The final URL" + query);
        URL url;
        try {
            url = new URL(query);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    //method for sending HTTPRequest
    public String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
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

    //method to update the UI
    public void updateUI(ArrayList<SearchResultsItem> finalList) {
        ListView listview = (ListView) findViewById(R.id.list);
        SearchListAdapter adapter = new SearchListAdapter(this, finalList);
        listview.setAdapter(adapter);
    }
}
