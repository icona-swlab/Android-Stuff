package it.icona.bardimcalc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.widget.TextView;

public class BarDimensionCalcActivity extends Activity 
{
	private int screenH = 0;
	private int screenW = 0;
	private int screenDensity = 0;
	private TextView tvContent = null;

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
        screenDensity = metrics.densityDpi;

        getRealDimensions();
        
	    tvContent = (TextView) findViewById(R.id.tvContent);
	    tvContent.postDelayed(new Runnable() 
	    {

			@Override
			public void run() 
			{
				 String display = String.format("Status Bar Height = %d\nTitle Bar Height = %d\nScreen Height = %d\nScreen Width = %d\nScreen Density = %d", 
						 getStatusBarHeight(), getTitleBarHeight(), screenH, screenW, screenDensity);
			     tvContent.setText(display);
			}
	    }, 2000);

	}

	public int getStatusBarHeight() 
	{
		int height = 0;
		
		Rect r = new Rect();
	  	Window w = getWindow();
	  	w.getDecorView().getWindowVisibleDisplayFrame(r);
	  	
	  	if(r.top == 0)
	  		height = screenH - r.bottom;
	  	else
	  		height = r.top;
	  	
	  	return height;
	}

	public int getTitleBarHeight() 
	{
		Rect r = new Rect();
	  	Window w = getWindow();
	  	w.getDecorView().getWindowVisibleDisplayFrame(r);

	  	int viewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
	  	return (viewTop - r.top);
	}
	
	public void getRealDimensions()
	{
		Display d = getWindowManager().getDefaultDisplay();
		int width = 0, height = 0;

		Method mGetRawH;
		Method mGetRawW;
		try {
		    mGetRawH = Display.class.getMethod("getRawWidth");
		    mGetRawW = Display.class.getMethod("getRawHeight");
		    width = (Integer) mGetRawW.invoke(d);
		    height = (Integer) mGetRawH.invoke(d);
		} catch (NoSuchMethodException e) {
		    e.printStackTrace();
		} catch (IllegalArgumentException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		} catch (InvocationTargetException e) {
		    e.printStackTrace();
		}

		//You should care about orientation here
		int o = getResources().getConfiguration().orientation;
		if (o == Configuration.ORIENTATION_PORTRAIT) {
		    if (width > height) {
		        int tmp = width;
		        width = height;
		        height = tmp;
		    }
		} else if (o == Configuration.ORIENTATION_LANDSCAPE) {
		    if (width < height) {
		        int tmp = width;
		        width = height;
		        height = tmp;
		    }
		}
		
		if(width > 0 && height > 0)
		{
			screenH = height;
			screenW = width;
		}

		Log.d("d", "w=" + width + " h=" + height);
	}
}