package it.icona.hlvdemo;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

public class MyGestureDetector extends GestureDetector
{

	public MyGestureDetector(Context context, OnGestureListener listener) 
	{
		super(context, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) 
	{
		// TODO Auto-generated method stub
		// super.onTouchEvent(ev);
		// return false;
		return super.onTouchEvent(ev);
	}

}
