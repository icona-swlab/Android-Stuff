package it.icona.hlvdemo;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MyImageView extends ImageView implements OnTouchListener 
{
	public static final String TAG = "Touch";
	
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	private int screenH = 0;
	private int screenW = 0;
	private float minScale = 0.5f;
	private float maxScale = 2.0f;
	private BaseAdapter adap = null;
	private int currentimg = -1;

	public MyImageView(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyImageView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setScreenSize(int height, int width)
	{
		screenH = height;
		screenW = width;
	}
	
	public void setMinScale(int objw, int objh)
	{
		float scaleW = (float) screenW / objw;
		float scaleH = (float) screenH / objh;

		if(scaleW <= scaleH)
			minScale = scaleW;
		else
			minScale = scaleH;
	}
	
	public void setStartMatrix(MyImageView view)
	{
		Matrix t_matrix = view.getImageMatrix();

		float px = (float) view.getWidth() / 2;
		float py = (float) view.getHeight() / 2;
		
		t_matrix.setScale(minScale, minScale, px, py);
		
		t_matrix.set(t_matrix);

		Drawable d = view.getDrawable();
		int dheight = d.getIntrinsicHeight();
		int dwidth = d.getIntrinsicWidth();
		float[] mvalues = new float[9];
		t_matrix.getValues(mvalues);
		float currentY = mvalues[Matrix.MTRANS_Y];
		float currentX = mvalues[Matrix.MTRANS_X];
		float realx = (float) dwidth * mvalues[Matrix.MSCALE_X];
		float realy = (float) dheight * mvalues[Matrix.MSCALE_Y];

		Log.d(TAG, "Values after scaling: top=" + currentY + "; left=" + currentX + "; image real width=" + realx + "; image real height=" + realy);

		float diffy = calculateDiff(realy, 0, currentY, realy + currentY, screenH);
		float diffx = calculateDiff(realx, 0, currentX, realx + currentX, screenW);

		t_matrix.postTranslate(diffx, diffy);
		
		matrix.set(t_matrix);
		
		view.setImageMatrix(t_matrix);
	}
	
	public void setAdapter(BaseAdapter adapter)
	{
		adap = adapter;
	}
	
	public void setMyPosition(int pos)
	{
		currentimg = pos;
	}
	
	public int getMyPosition()
	{
		return currentimg;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		// TODO Auto-generated method stub
		ImageView view = (ImageView) v;
		float [] mvalues = new float[9];

		// Codice mio aggiunto START
		Drawable d = view.getDrawable();
		int dheight = d.getIntrinsicHeight();
		int dwidth = d.getIntrinsicWidth();
		
		setMinScale(dwidth, dheight);

		/*
		float scaleW = (float) screenW / dwidth;
		float scaleH = (float) screenH / dheight;

		if(scaleW <= scaleH)
			minScale = scaleW;
		else
			minScale = scaleH;
		*/
		// Codice mio aggiunto END

		// Dump touch event to log
		dumpEvent(event);

		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			Log.d(TAG, "mode=DRAG");
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			Log.d(TAG, "mode=NONE");
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				// ...

				matrix.set(savedMatrix);
				matrix.getValues(mvalues);

				/*
				Log.d(TAG, "Matrix before move:");
				Log.d(TAG, "ScaleX=" + mvalues[matrix.MSCALE_X]);
				Log.d(TAG, "SkewX=" + mvalues[matrix.MSKEW_X]);
				Log.d(TAG, "TransX=" + mvalues[matrix.MTRANS_X]);
				Log.d(TAG, "ScaleY=" + mvalues[matrix.MSCALE_Y]);
				Log.d(TAG, "SkewY=" + mvalues[matrix.MSKEW_Y]);
				Log.d(TAG, "TransY=" + mvalues[matrix.MTRANS_Y]);
				Log.d(TAG, "Persp0=" + mvalues[matrix.MPERSP_0]);
				Log.d(TAG, "Persp1=" + mvalues[matrix.MPERSP_1]);
				Log.d(TAG, "Persp2=" + mvalues[matrix.MPERSP_2]);
				*/

				// Codice mio aggiunto START
				float currentY = mvalues[Matrix.MTRANS_Y];
				float currentX = mvalues[Matrix.MTRANS_X];
				float diffx = event.getX() - start.x;
				float diffy = event.getY() - start.y;
				float realx = (float) dwidth * mvalues[Matrix.MSCALE_X];
				float realy = (float) dheight * mvalues[Matrix.MSCALE_Y];
				float newx = currentX + diffx;
				float newy = currentY + diffy;
				float rightlimit = newx + realx;
				float bottomlimit = newy + realy;

				Log.d(TAG, "rightlimit=" + rightlimit + "; currentX=" + currentX + "; newx=" + newx);
				Log.d(TAG, "currentimg=" + currentimg + "; nimages=" + adap.getCount());
				
				if(currentimg < adap.getCount() && rightlimit < screenW)
				{
					return false;
				}
				
				if(currentimg > 0 && newx > 0)
				{
					return false;
				}

				Log.d(TAG, "realy=" + realy + "; diffy=" + diffy + "; newy=" + newy + "; screenH" + screenH);
				Log.d(TAG, "realx=" + realx + "; diffx=" + diffx + "; newx=" + newx + "; screenW" + screenW);
				
				diffy = calculateDiff(realy, diffy, newy, bottomlimit, screenH);
				diffx = calculateDiff(realx, diffx, newx, rightlimit, screenW);

				Log.d(TAG, "diffy=" + diffy + "; diffx=" + diffx);
				
				/*
				if(realy < screenH)
				{
					float filler = (screenH - realy) / 2;
					
					if(newy < 0)
					{
						if(bottomlimit < screenH)
							diffy = diffy - newy + filler;
					}
					else if(bottomlimit > screenH)
					{
						if(newy > 0)
							diffy = diffy + (screenH - bottomlimit) - filler;
					}
				}
				else
				{
					if(newy > 0)
						diffy = diffy - newy;
					else if(bottomlimit < screenH)
						diffy = diffy + (screenH - bottomlimit);
				}

				if(realx < screenW)
				{
					float filler = (screenW - realx) / 2;

					if(newx < 0 && rightlimit < screenW)
						diffx = diffx - newx + filler;
					else if(newx > 0 && rightlimit > screenW)
						diffx = diffx + (screenW - rightlimit) - filler;
				}
				else
				{
					if(newx > 0)
						diffx = diffx - newx;
					else if(rightlimit < screenW)
						diffx = diffx + (screenW - rightlimit);
				}
				*/

				matrix.postTranslate(diffx, diffy);
				// Codice mio aggiunto END

				// matrix.postTranslate(event.getX() - start.x,
						//      event.getY() - start.y);
				matrix.getValues(mvalues);

				/*
				Log.d(TAG, "Matrix after move:");
				Log.d(TAG, "ScaleX=" + mvalues[matrix.MSCALE_X]);
				Log.d(TAG, "SkewX=" + mvalues[matrix.MSKEW_X]);
				Log.d(TAG, "TransX=" + mvalues[matrix.MTRANS_X]);
				Log.d(TAG, "ScaleY=" + mvalues[matrix.MSCALE_Y]);
				Log.d(TAG, "SkewY=" + mvalues[matrix.MSKEW_Y]);
				Log.d(TAG, "TransY=" + mvalues[matrix.MTRANS_Y]);
				Log.d(TAG, "Persp0=" + mvalues[matrix.MPERSP_0]);
				Log.d(TAG, "Persp1=" + mvalues[matrix.MPERSP_1]);
				Log.d(TAG, "Persp2=" + mvalues[matrix.MPERSP_2]);
				*/

				view.setImageMatrix(matrix);
				return true; // indicate event was handled
			}
			else if (mode == ZOOM) {
				float newDist = spacing(event);
				Log.d(TAG, "newDist=" + newDist);
				if (newDist > 10f) 
				{
					matrix.set(savedMatrix);
					matrix.getValues(mvalues);
					float scale = newDist / oldDist;
					// Codice mio aggiunto START
					float currentScale = mvalues[Matrix.MSCALE_X];

					if((scale * currentScale) > maxScale)
						scale = maxScale / currentScale;
					else if((scale * currentScale) < minScale)
						scale = minScale / currentScale;
					// Codice mio aggiunto END

					matrix.postScale(scale, scale, mid.x, mid.y);

					// Codice mio aggiunto START
					// Codice per centrare l'immagine se lo zoom porta una delle due dimensioni sotto la dimensione dello schermo corrispondente
					matrix.getValues(mvalues);

					float currentY = mvalues[Matrix.MTRANS_Y];
					float currentX = mvalues[Matrix.MTRANS_X];
					float realx = (float) dwidth * mvalues[Matrix.MSCALE_X];
					float realy = (float) dheight * mvalues[Matrix.MSCALE_Y];
					float rightlimit = realx + currentX;
					float bottomlimit = realy + currentY;
					float dx = 0, dy = 0;

					if(realy <= screenH)
					{
						float diffy = screenH - realy;
						float filler = diffy / 2;
							
						dy = filler - currentY;
					}
					else
					{
						if(currentY > 0)
						{
							dy = -currentY;
						}
						else if(bottomlimit < screenH)
						{
							dy = screenH - bottomlimit;
						}
					}

					if(realx <= screenW)
					{
						float diffx = screenW - realx;
						float filler = diffx / 2;
							
						dx = filler - currentX;
					}
					else
					{
						if(currentX > 0)
						{
							dx = -currentX;
						}
						else if(rightlimit < screenW)
						{
							dx = screenW - rightlimit;
						}
					}

					matrix.postTranslate(dx, dy);
					// Codice mio aggiunto END

					view.setImageMatrix(matrix);
					return true; // indicate event was handled
				}
			}
			break;
		}

		return false; // indicate event was NOT handled
	}

	/** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
				"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				|| actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(
					action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";");
		}
		sb.append("]");
		Log.d(TAG, sb.toString());
	}

	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
	
	public float calculateDiff(float realdim, float diffpos, float newpos, float limit, int screendim)
	{
		float diff = 0;
		
		if(realdim < screendim)
		{
			float filler = (screendim - realdim) / 2;
			
			if(newpos < 0)
			{
				if(limit < screendim)
					diff = diffpos - newpos + filler;
			}
			else if(limit > screendim)
			{
				if(newpos > 0)
					diff = diffpos + (screendim - limit) - filler;
			}
			else
				diff = filler - newpos;
		}
		else
		{
			if(newpos > 0)
				diff = diffpos - newpos;
			else if(limit < screendim)
				diff = diffpos + (screendim - limit);
			else
				diff = diffpos;
		}
		
		return diff;
	}
}
