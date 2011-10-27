package it.icona.hlvdemo;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class DemoActivity extends Activity 
{
	private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
	private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
	private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;
	
	private int screenH = 0;
	private int screenW = 0;
	HorizontalListView listview = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.listviewdemo);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenH = metrics.heightPixels;
        screenW = metrics.widthPixels;
        
        switch (metrics.densityDpi) 
        {
	        case DisplayMetrics.DENSITY_HIGH:
	            screenH -= HIGH_DPI_STATUS_BAR_HEIGHT;
	            break;
	        case DisplayMetrics.DENSITY_MEDIUM:
	        	screenH -= MEDIUM_DPI_STATUS_BAR_HEIGHT;
	            break;
	        case DisplayMetrics.DENSITY_LOW:
	        	screenH -= LOW_DPI_STATUS_BAR_HEIGHT;
	            break;
	        default:
	        	screenH -= MEDIUM_DPI_STATUS_BAR_HEIGHT;
        }
		
		listview = (HorizontalListView) findViewById(R.id.listview);
		listview.setScreenLimits(screenH, screenW);
		listview.setAdapter(mAdapter);

		if(mAdapter.getCount() > 0)
			listview.setSelection(3);
		
	}
	
	private static String[] dataObjects = new String[]{ "Text #1",
		"Text #2",
		"Text #3" }; 

	private Integer[] mImageIds = {
            R.drawable.sample_1,
            R.drawable.sample_2,
            R.drawable.sample_3,
            R.drawable.sample_4,
            R.drawable.sample_5,
            R.drawable.sample_6,
            R.drawable.sample_7,
            R.drawable.sample_8,
            R.drawable.sample_9,
            R.drawable.sample_10,
            R.drawable.sample_11,
            R.drawable.sample_12,
            R.drawable.sample_13,
            R.drawable.sample_14,
            R.drawable.sample_15,
            R.drawable.sample_16,
            R.drawable.sample_17,
            R.drawable.sample_18,
            R.drawable.sample_19,
            R.drawable.sample_20,
            R.drawable.sample_21,
            R.drawable.sample_22,
            R.drawable.sample_23,
            R.drawable.sample_24,
            R.drawable.sample_25,
            R.drawable.sample_26,
            R.drawable.sample_27,
            R.drawable.sample_28,
            R.drawable.sample_29,
            R.drawable.sample_30,
            R.drawable.sample_31,
            R.drawable.sample_32,
            R.drawable.sample_33,
            R.drawable.sample_34,
            R.drawable.sample_35,
            R.drawable.sample_36,
            R.drawable.sample_37,
            R.drawable.sample_38
    };
	
	private BaseAdapter mAdapter = new BaseAdapter() 
	{

		@Override
		public int getCount() 
		{
			return mImageIds.length;
		}

		@Override
		public Object getItem(int position) 
		{
			return null;
		}

		@Override
		public long getItemId(int position) 
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewitem, null);
			MyImageView img = (MyImageView) retval.findViewById(R.id.image);
			// TextView title = (TextView) retval.findViewById(R.id.title);
			// title.setText(dataObjects[position]);
			img.setImageResource(mImageIds[position]);
			// img.setScaleType(ImageView.ScaleType.CENTER_CROP);
			img.setScaleType(ImageView.ScaleType.MATRIX);
			img.setScreenSize(screenH, screenW);

			Drawable d = img.getDrawable();
			int dheight = d.getIntrinsicHeight();
			int dwidth = d.getIntrinsicWidth();
			
			img.setMinScale(dwidth, dheight);
			img.setStartMatrix(img);
			img.setAdapter(this);
			img.setMyPosition(position);
			
			return retval;
		}
		
	};
}
