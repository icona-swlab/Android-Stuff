package com.samsung.gui;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class FadeAnimActivity extends CustomActivity implements AnimationListener
{
	ImageView imgone = null;
	ImageView imgtwo = null;
	ImageView imgthree = null;
	Animation fadingone = null;
	Animation fadingtwo = null;
	Animation fadingthree = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animatedhome);
		
		imgone = (ImageView) findViewById(R.id.img1);
		imgtwo = (ImageView) findViewById(R.id.img2);
		imgthree = (ImageView) findViewById(R.id.img3);
		
		fadingone = AnimationUtils.loadAnimation(this,R.anim.fade);
		fadingtwo = AnimationUtils.loadAnimation(this,R.anim.fade);
		fadingthree = AnimationUtils.loadAnimation(this,R.anim.fade);
		fadingone.setAnimationListener(this);
		fadingtwo.setAnimationListener(this);
		fadingthree.setAnimationListener(this);
		
		imgone.startAnimation(fadingone);
	}

	@Override
	public void onAnimationEnd(Animation animation) 
	{
		// TODO Auto-generated method stub
		if(imgone.getVisibility() != ImageView.VISIBLE)
		{
			imgone.setVisibility(ImageView.VISIBLE);
			imgtwo.startAnimation(fadingtwo);
		}
		else if(imgtwo.getVisibility() != ImageView.VISIBLE)
		{
			imgtwo.setVisibility(ImageView.VISIBLE);
			imgthree.startAnimation(fadingthree);
		}
		else
			imgthree.setVisibility(ImageView.VISIBLE);
	}

	@Override
	public void onAnimationRepeat(Animation animation) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) 
	{
		// TODO Auto-generated method stub
		
	}

}
