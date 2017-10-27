package com.example.bookreview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    
    public JSONParser(){
	Log.d("JSONParser: " , "Constructor");
    }
    
    public JSONObject getJSON(String URL){
	
	try{
	    HttpClient httpClient = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet(URL);
	    
	    HttpResponse httpResponse = httpClient.execute(httpGet);
	    HttpEntity httpEntity = httpResponse.getEntity();
	    is = httpEntity.getContent();
	    if(is == null){
		Log.d("IS NULL" , "in jsonparser");
	    }
	} catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	
	try{
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while((line = reader.readLine()) != null){
		Log.d("Line is: ", line);
		sb.append(line + "\n");
	    }
	    is.close();
	    json = sb.toString();
	} catch (Exception e){
	    Log.e("Buffer Error", "Error parsing data");
	}
	
	try{
	    jObj = new JSONObject(json);
	} catch (JSONException e){
	    e.printStackTrace();
	}
	
	return jObj;
    }
    
}