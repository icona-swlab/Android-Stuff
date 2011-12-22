package it.icona.roundcorners;

import android.app.AlertDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class MySpinner extends Spinner
{
	private AlertDialog mPopup;
	
	public MySpinner(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MySpinner(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MySpinner(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onAttachedToWindow() 
	{
	    super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() 
	{
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();

	    if (mPopup != null && mPopup.isShowing()) 
	    {
	        mPopup.dismiss();
	        mPopup = null;
	    }
	}

	@Override
	public boolean performClick() 
	{
		// TODO Auto-generated method stub
	    Context context = getContext();

	    AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    CharSequence prompt = getPrompt();
	    if (prompt != null) 
	    {
	        builder.setTitle(prompt);
	    }

	    mPopup = builder.setSingleChoiceItems(
	            new DropDownAdapter(getAdapter()),
	            getSelectedItemPosition(), this).show();

	    WindowManager.LayoutParams WMLP = mPopup.getWindow().getAttributes();

	            //width and height must be set to anything other than WRAP_CONTENT!
	    WMLP.x = 0; // x position
	    WMLP.y = 70; // y position
	    WMLP.height = 390 ; //LayoutParams.WRAP_CONTEN
	    WMLP.width = 250;
	    WMLP.horizontalMargin = 0; 
	    WMLP.verticalMargin = 0;
	    WMLP.gravity = Gravity.TOP | Gravity.LEFT;

	    mPopup.getWindow().setAttributes(WMLP);
		
	    return true;

	}

	private static class DropDownAdapter implements ListAdapter, SpinnerAdapter 
	{
		private SpinnerAdapter mAdapter;

		public DropDownAdapter(SpinnerAdapter adapter) 
		{
			this.mAdapter = adapter;
		}

		public int getCount() 
		{
			return mAdapter == null ? 0 : mAdapter.getCount();
		}

		public Object getItem(int position) 
		{
			return mAdapter == null ? null : mAdapter.getItem(position);
		}

		public long getItemId(int position) 
		{
			return mAdapter == null ? -1 : mAdapter.getItemId(position);
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
			return getDropDownView(position, convertView, parent);
		}

		public View getDropDownView(int position, View convertView, ViewGroup parent) 
		{
			return mAdapter == null ? null :
				mAdapter.getDropDownView(position, convertView, parent);
		}

		public boolean hasStableIds() 
		{
			return mAdapter != null && mAdapter.hasStableIds();
		}

		public void registerDataSetObserver(DataSetObserver observer) 
		{
			if (mAdapter != null) 
			{
				mAdapter.registerDataSetObserver(observer);
			}
		}

		public void unregisterDataSetObserver(DataSetObserver observer) 
		{
			if (mAdapter != null) 
			{
				mAdapter.unregisterDataSetObserver(observer);
			}
		}

		 public boolean areAllItemsEnabled() 
		 {
			 return true;
		 }

		 public boolean isEnabled(int position) 
		 {
			 return true;
		 }

		 public int getItemViewType(int position) 
		 {
			 return 0;
		 }

		 public int getViewTypeCount() 
		 {
			 return 1;
		 }

		 public boolean isEmpty() 
		 {
			 return getCount() == 0;
		 }

	}
}
