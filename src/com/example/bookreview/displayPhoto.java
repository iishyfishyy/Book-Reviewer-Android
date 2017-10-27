package com.example.bookreview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

public class displayPhoto extends Activity {

    private Bitmap img;
    private ImageView iView;
    
    @Override
    public void onCreate(Bundle b){
	super.onCreate(b);
	setContentView(R.layout.activity_camera);
	Intent temp = getIntent();
	Bundle extra = temp.getExtras();
	img = (Bitmap) extra.get("photo");
	img = Bitmap.createScaledBitmap(img, img.getWidth() * 5, img.getHeight() * 5, false);
	img = Rotate(img, 90);
	iView = (ImageView) findViewById(R.id.imgview_camera);
	iView.setImageBitmap(img);
    }
    
    public Bitmap Rotate(Bitmap src, float angle){
	Matrix matrix = new Matrix();
	matrix.postRotate(angle);
	return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }
    
}