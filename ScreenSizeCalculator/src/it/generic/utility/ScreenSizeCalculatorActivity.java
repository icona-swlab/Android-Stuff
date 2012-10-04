package it.generic.utility;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ScreenSizeCalculatorActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView altezza = (TextView) findViewById(R.id.height);
        TextView altdens = (TextView) findViewById(R.id.heightden);
        TextView larghezza = (TextView) findViewById(R.id.width);
        TextView largdens = (TextView) findViewById(R.id.widthden);
        TextView diagonale = (TextView) findViewById(R.id.inches);
        
        int actionbarheight = 0;
        if( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB )
        {
        	ActionBar bar = getActionBar();
        	
        	if(bar != null)
        		actionbarheight = bar.getHeight();
        }
        
        MainApplication.calculateScreenSizeInInches(getWindowManager(), actionbarheight);
        
        altezza.setText("Altezza (in pixel) = " + MainApplication.getScreenH());
        altdens.setText("Densità altezza (in pollici) = " + String.valueOf(MainApplication.getScreenDh()));
        larghezza.setText("Larghezza (in pixel) = " + MainApplication.getScreenW());
        largdens.setText("Densità larghezza (in pollici) = " + String.valueOf(MainApplication.getScreenDw()));
        diagonale.setText("Diagonale (in pollici) = " + String.valueOf(MainApplication.getScreen_size_in_inches()));
    }
}