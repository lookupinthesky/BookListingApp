package com.example.android.booklistingapp;

/**
 * Created by Shubham on 1/28/2017.
 */
//class for the list item of search results

public class SearchResultsItem {


    public SearchResultsItem( String bookTitle, String bookAuthor){

        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
    }


    String bookTitle;
    String bookAuthor;


    public String getBookTitle(){
        return bookTitle;
    }

    public String getBookAuthor(){
        return bookAuthor;
    }
}
