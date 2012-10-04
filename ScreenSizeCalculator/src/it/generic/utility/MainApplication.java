package it.generic.utility;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class MainApplication extends Application
{
	private static int screenW = 0;
	private static int screenH = 0;
	private static float screenDh = 0.0F;
	private static float screenDw = 0.0F;
	private static double screen_size_in_inches = 0.0F;
	
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	public static double calculateScreenSizeInInches(WindowManager wm, int abh)
	{
		double diagonale = 0.0F;
		
		Display display = wm.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		
		screenH = dm.heightPixels + abh;
		screenW = dm.widthPixels;
		screenDh = dm.ydpi;
		screenDw = dm.xdpi;
		
		float larghezza = screenW / screenDw;
		float altezza = screenH / screenDh;
		
		diagonale = Math.sqrt(Math.pow(larghezza, 2.0) + Math.pow(altezza, 2.0));
		
		screen_size_in_inches = diagonale;
		
		return diagonale;
	}

	public static int getScreenW()
	{
		return screenW;
	}

	public void setScreenW(int screenW)
	{
		this.screenW = screenW;
	}

	public static int getScreenH()
	{
		return screenH;
	}

	public void setScreenH(int screenH)
	{
		this.screenH = screenH;
	}

	public static float getScreenDh()
	{
		return screenDh;
	}

	public void setScreenDh(float screenDh)
	{
		this.screenDh = screenDh;
	}

	public static float getScreenDw()
	{
		return screenDw;
	}

	public void setScreenDw(float screenDw)
	{
		this.screenDw = screenDw;
	}

	public static double getScreen_size_in_inches()
	{
		return screen_size_in_inches;
	}

	public void setScreen_size_in_inches(double screen_size_in_inches)
	{
		this.screen_size_in_inches = screen_size_in_inches;
	}

}
