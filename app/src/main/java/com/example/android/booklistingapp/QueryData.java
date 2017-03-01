package com.example.android.booklistingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.android.booklistingapp.SearchResultsActivity.LOG_TAG;

/**
 * Created by Shubham on 1/28/2017.
 */

public class QueryData {

    public QueryData() {

    }

//parsing JSON
    public ArrayList<SearchResultsItem> extractSearchArray(String jsonResponse) {

        ArrayList<SearchResultsItem> resultsArray = new ArrayList<>();

        try {
            JSONObject queryResults = new JSONObject(jsonResponse);
            if (queryResults.toString().contains("items")) {
                JSONArray array = queryResults.getJSONArray("items");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject arrayItem = array.getJSONObject(i);

                    JSONObject volumeInfo = arrayItem.getJSONObject("volumeInfo");

                    String title = volumeInfo.getString("title");

                    JSONArray authors = volumeInfo.getJSONArray("authors");

                    String authorLine = authors.toString();
                    Log.d(LOG_TAG, "print author0" + authorLine);

//formatting the string by applying the remove method
                    authorLine = remove(authorLine, "[");
                    Log.d(LOG_TAG, "print author2" + authorLine);
                    authorLine = remove(authorLine, "]");
                    Log.d(LOG_TAG, "print author3" + authorLine);
                    resultsArray.add(new SearchResultsItem(title, authorLine));
                }
            } else resultsArray.add(new SearchResultsItem("Error 404: Not Found", ""));
        } catch (JSONException e) {
            Log.e("QueryData", "Problem parsing the earthquake JSON results", e);
        }

        return resultsArray;

    }
//remove method to remove strings from the JSON and format it
    public String remove(String parentString, String cleanable) {
        int i = parentString.indexOf(cleanable);
        Log.d(LOG_TAG, "print index " + i);
        Log.d(LOG_TAG, "print substring " + parentString.substring(1, 1));
        String child1, child2;


        while (parentString.indexOf(cleanable) != -1) {
            if (parentString.indexOf(cleanable) == 0) {
                child1 = "";
            } else {
                child1 = parentString.substring(0, (i - 1));
            }
            Log.d(LOG_TAG, "print child1 " + child1);
            child2 = parentString.substring(i + cleanable.length(), parentString.length() - 1);
            Log.d(LOG_TAG, "print child2 " + child2);
            parentString = child1 + child2;
            Log.d(LOG_TAG, "print parent " + parentString);
        }

        return parentString;

    }

}

