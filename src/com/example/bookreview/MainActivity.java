package com.example.bookreview;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
	private String ISBNno;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private Bitmap img;
	private Uri imageUri;
	String photoPath;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void getImage(View v){
	    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
	}

	public void scanBar(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(MainActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}

	public void scanQR(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(MainActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}

	private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
		downloadDialog.setTitle(title);
		downloadDialog.setMessage(message);
		downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				try {
					act.startActivity(intent);
				} catch (ActivityNotFoundException anfe) {

				}
			}
		});
		downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
			}
		});
		return downloadDialog.show();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				if(format.contains("EAN")){
					ISBNno = contents;
					Log.d("MAIN", "before starting bookinfoactivity");
					startBookInfoActivity();
					Log.d("MAIN", "after starting bookinfoactivity");

				} else {
				    Toast.makeText(this, "Please scan a book!", Toast.LENGTH_LONG).show();
				    try {
					Thread.sleep(2);
				    } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				    }
				}
			
			}
		} else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
		    img = (Bitmap) intent.getExtras().get("data");
		    //img = Bitmap.createScaledBitmap(img, 100, 100, false);
		    Intent myint = new Intent(MainActivity.this, displayPhoto.class);
		    myint.putExtra("photo", img);
		    startActivity(myint);
		}
	}

	private void startBookInfoActivity() {
	    Intent myint = new Intent(this, BookInfoActivity.class);
	    Log.d("isbn main", ISBNno);
	    myint.putExtra("isbn", ISBNno);
	    startActivity(myint);
	}
}