	package com.example.bookreview;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class BookInfoActivity extends Activity {

    private String theISBN;
    private String theURL;
    private TextView bookInfoTV;
    private TextView titleTV;
    private ImageView bookImg;
    private JSONParser jParser;
    private JSONObject json;
    private JSONArray jArray;
    private String desc;
    private Bitmap bmp;
    private long start, end;
    private reviewsFromGoodreads theReviews;
    private ArrayList<String> reviews;
    private TextView reviewTV;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_book_info);
	
	Intent temp = getIntent();
	Log.d("BOOKINFOACT", "started bookinfoactivity");
	
	bookInfoTV = (TextView)findViewById(R.id.book_info_textview);
	titleTV = (TextView)findViewById(R.id.book_title);
	bookImg = (ImageView)findViewById(R.id.book_photo);
	reviewTV = (TextView)findViewById(R.id.review_textview);
	reviewTV.setText("");
	
	theISBN = temp.getStringExtra("isbn");
	theURL = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + theISBN;
	Log.d("isbn", theISBN);
	Log.d("url", theURL);
	new ASYNCTASK().execute();
	Log.d("ASYNCTASK execution time", Integer.toString((int) (end - start)));
	
	theReviews = new reviewsFromGoodreads(theISBN);
    }

    private void retrieveReviews() {

	new AsyncTask<Void, Void, Void>(){

	    @Override
	    protected Void doInBackground(Void... params) {
		try {
		    theReviews.accumulateReviews();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return null;
	    }
	    
	    protected void onPostExecute(Void rez){
		int i = 1;
		String x = "<b>REVIEWS:</b>";
		reviewTV.setText(Html.fromHtml(x) + "\n\n");
		for(String s : theReviews.reviews){
		    reviewTV.append(Html.fromHtml("<b>#" + i + ":</b>") + "\n");
		    reviewTV.append(s + "\n\n\n");
		    ++i;
		}
	    }
	    
	}.execute();
	
    }

    class ASYNCTASK extends AsyncTask<Void, Void, Void>{

	private ProgressDialog progD = new ProgressDialog(BookInfoActivity.this);
	InputStream inputStream = null;
	String result = "";
	
	protected void onPreExecute(){
	    progD.setMessage("Working");
	    progD.show();
	    progD.setOnCancelListener(new OnCancelListener(){
		@Override
		public void onCancel(DialogInterface dialog) {
		    ASYNCTASK.this.cancel(true);
		}
	    });
	}
	
	@Override
	protected Void doInBackground(Void... params) {

	    start = System.currentTimeMillis();
	    jParser = new JSONParser();
	    json = jParser.getJSON(theURL);

	    return null;
	}
	
	protected void onPostExecute(Void v){
	    String title, subtitle, fullTitle = null;
	    try{	
		    jArray = json.getJSONArray("items");
		    final JSONObject t = jArray.getJSONObject(0);
		    
		    desc = t.getJSONObject("volumeInfo").getString("description");
		    
		    title = t.getJSONObject("volumeInfo").getString("title");
		    if(t.has("subtitle")){
			subtitle = t.getJSONObject("volumeInfo").getString("subtitle");
			fullTitle = title + ": " + subtitle;
		    } else {
			fullTitle = title;
		    }
		    titleTV.setText(fullTitle + '\n');
		    bookInfoTV.setText(desc + "\n\n\n");
		    
		    new AsyncTask<Void, Void, Void>(){
			protected Void doInBackground(Void... params){
			    try{
				String url = t.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail");
				url = url.replace("zoom=1", "zoom=100");
				InputStream in = new URL(url).openStream();
				bmp = BitmapFactory.decodeStream(in);
			    } catch (Exception e){
				e.printStackTrace();
			    }
			    return null;
			}
			protected void onPostExecute(Void rez){
			    if(bmp != null){
				bookImg.setImageBitmap(bmp);
			    }
			}
		    }.execute();
		   
	    	} catch (JSONException e){
		    Log.d("JSONException: ", e.toString());
		} finally {
		    progD.dismiss();
		retrieveReviews();
		}
	 end = System.currentTimeMillis();
	}
	
    }
    
    
}
