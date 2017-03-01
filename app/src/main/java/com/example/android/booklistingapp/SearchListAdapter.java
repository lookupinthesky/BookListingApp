package com.example.android.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.booklistingapp.R;
import com.example.android.booklistingapp.SearchResultsItem;

import java.util.List;

import static android.R.attr.resource;

/**
 * Created by Shubham on 1/28/2017.
 */

public class SearchListAdapter extends ArrayAdapter<SearchResultsItem> {

    //constructor

    public SearchListAdapter(Context context, List<SearchResultsItem> objects) {
        super(context, 0, objects);
    }
    //implement get view method
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.searchresultslistitem, parent, false);
        }

        SearchResultsItem currentItem = getItem(position);

        TextView bookTitle = (TextView) convertView.findViewById(R.id.bookTitle);
        bookTitle.setText(currentItem.getBookTitle());
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.bookAuthor);
        bookAuthor.setText(currentItem.getBookAuthor());
        return convertView;
    }
}
