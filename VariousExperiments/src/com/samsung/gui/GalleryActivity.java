package com.samsung.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryActivity extends CustomActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
		
		Gallery products = (Gallery) findViewById(R.id.gallery1);
		ImageAdapter adap = new ImageAdapter(this);
		products.setAdapter(adap);
		products.setSelection(1);
		
		products.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
		{
		    public void onItemSelected (AdapterView<?>  parent, View  v, int position, long id) 
		    {
		        Animation grow = AnimationUtils.loadAnimation(GalleryActivity.this, R.anim.grow);

		        View sideView = parent.findViewById(position - 1);
		        if (sideView != null)
		        	sideView.clearAnimation();

		        sideView = parent.findViewById(position + 1);
		        if (sideView != null)
		        	sideView.clearAnimation();

		        v.startAnimation(grow);
		    }

		    public void  onNothingSelected  (AdapterView<?>  parent) 
		    {
		        System.out.println("NOTHING SELECTED");

		    }
		});
	}

	public class ImageAdapter extends BaseAdapter 
	{
	    private Context mContext;

	    private Integer[] mImageIds = {
	            R.drawable.galaxy_mini2,
	            R.drawable.galaxy_s2,
	            R.drawable.galaxy_tab,
	            R.drawable.galaxy_mini2,
	            R.drawable.galaxy_s2,
	            R.drawable.galaxy_tab,
	            R.drawable.finale
	    };

	    public ImageAdapter(Context c) 
	    {
	        mContext = c;
	    }

	    public int getCount() 
	    {
	        return mImageIds.length;
	    }

	    public Object getItem(int position) 
	    {
	        return position;
	    }

	    public long getItemId(int position) 
	    {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
	        Animation fade = AnimationUtils.loadAnimation(GalleryActivity.this, R.anim.fade);
	        ImageView imageView = new ImageView(mContext);

	        imageView.setImageResource(mImageIds[position]);
	        imageView.setLayoutParams(new Gallery.LayoutParams(400, 500));
	        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	        imageView.setBackgroundResource(R.color.transparent);
	        imageView.setId(position);
	        imageView.clearAnimation();
	        imageView.setAnimation(fade);

	        return imageView;
	    }
	}
}
