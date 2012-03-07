package com.samsung.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class HomeActivity extends CustomActivity implements OnClickListener
{
	private int screenH = 0;
	private int screenW = 0;
	private Button showvideo = null;
	private Button testimg = null;
	private Button testanimfade = null;
	private Button testgallery = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenH = metrics.heightPixels;
        screenW = metrics.widthPixels;
        int screenDensity = metrics.densityDpi;
        
        Log.d("SamsungApp", "Height=" + screenH + "; width=" + screenW + "; density=" + screenDensity);
        
        showvideo = (Button) findViewById(R.id.testvideo);
        testimg = (Button) findViewById(R.id.testimage);
        testanimfade = (Button) findViewById(R.id.testanimfade);
        testgallery = (Button) findViewById(R.id.testgallery);
        
        showvideo.setOnClickListener(this);
        testimg.setOnClickListener(this);
        testanimfade.setOnClickListener(this);
        testgallery.setOnClickListener(this);
    }

	@Override
	public void onClick(View arg0) 
	{
		// TODO Auto-generated method stub
		Button clicked = (Button) arg0;
		Intent newintent = null;
		
		switch(clicked.getId())
		{
			case R.id.testvideo:
				newintent = new Intent(this, VideoActivity.class);
				break;
			case R.id.testimage:
				newintent = new Intent(this, ImageTestActivity.class);
				break;
			case R.id.testanimfade:
				newintent = new Intent(this, FadeAnimActivity.class);
				break;
			case R.id.testgallery:
				newintent = new Intent(this, GalleryActivity.class);
				break;
			default:
				break;
		}
		
		if(newintent != null)
			startActivity(newintent);
	}

	// Disable home button
	@Override
	public void onAttachedToWindow()
	{  
	    Log.i("MyLog", "onAttachedToWindow");
	    this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	    super.onAttachedToWindow();  
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// Intercept home button pressed (only if it's disabled first)
	    if (keyCode == KeyEvent.KEYCODE_HOME) 
	    {
	        Log.i("MyLog", "BOTAO HOME");
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);    
	}
}