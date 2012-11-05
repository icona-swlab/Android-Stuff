package it.icona.gui;

import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class MainApplication extends Application
{

	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		
		removeBar();
	}

	static public void showBar()
	{
		Process proc;

		try 
		{
			proc = Runtime.getRuntime().exec(
					new String[]{"am","startservice","-n","com.android.systemui/.SystemUIService"});

			proc.waitFor();

		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void removeBar()
	{
		Process proc;

		try 
		{
			proc = Runtime.getRuntime().exec(
					new String[]{"su","-c","service call activity 79 s16 com.android.systemui"});

			proc.waitFor();

			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Name_Lock");
			wl.acquire();

		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}

	}

}
