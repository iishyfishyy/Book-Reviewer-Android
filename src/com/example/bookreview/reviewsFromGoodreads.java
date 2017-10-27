package com.example.bookreview;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class reviewsFromGoodreads {

    private String ISBN;
    private static final String URL_BASE = "https://www.goodreads.com/book/isbn?format=json&isbn=";
    public ArrayList<String> reviews;
    
    public reviewsFromGoodreads(String isbn){
	this.ISBN = isbn;
	reviews = new ArrayList<String>();
	Log.d("JSOUP", this.ISBN);
    }
    
    public void accumulateReviews() throws IOException{
	String url = URL_BASE + ISBN;
	System.out.println(url);
	Document doc = Jsoup.connect(url).get();

	Elements elems = doc.getElementsByClass("bookReviewBody");
	Elements elems2 = doc.getElementsByClass("readable");
	
	for(Element e : elems){
	    Log.d("GOODREADS", "reached");
	    Log.d("Element", e.text());
	    reviews.add(e.text());
	}   
	
	for(Element e : elems2){
	    if(e == null)
		continue;
	    Log.d("GOODREADS", "reached 2");
	    Log.d("Element", e.text());
	    reviews.add(e.text());
	}   
	
	Log.d("GOODREADS", "end of accumulate");
    }
    
    public ArrayList<String> retrieveReviews(){
	return this.reviews;
    }
}
