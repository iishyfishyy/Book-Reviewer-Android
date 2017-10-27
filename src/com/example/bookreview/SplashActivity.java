package com.example.bookreview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

    private static int SPLASH_TIMEOUT = 1750;
    
    public void onCreate(Bundle osc){
	super.onCreate(osc);
	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
	setContentView(R.layout.activity_splash);
	
	new Handler().postDelayed(new Runnable(){
	    public void run(){
		Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(myIntent);
		finish();
	    }
	}, SPLASH_TIMEOUT);
    }
    
}
