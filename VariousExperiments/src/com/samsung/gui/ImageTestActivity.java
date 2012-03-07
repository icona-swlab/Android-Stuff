package com.samsung.gui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageTestActivity extends CustomActivity implements OnClickListener
{
	String [] imgname = {
			"i01_video",
			"i02_pagina_intro",
			"i03_pagina_top_smartphone",
			"i04_pagina_scheda",
			"i05_pagina_amoled",
			"i06_pagina_android"
			};
	private int showed = 0;
    private ImageView img = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imgtest);
        
        img = (ImageView) findViewById(R.id.ambiente);
        String pckgname = getApplicationContext().getPackageName();

		Resources res = getResources();
		
		String reslocation = pckgname + ":drawable/" + imgname[0];
		int imgId = res.getIdentifier(reslocation, null, null);
		
		img.setImageResource(imgId);
		img.setOnClickListener(this);
    }

	@Override
	public void onClick(View arg0) 
	{
		// TODO Auto-generated method stub
        ImageView img = (ImageView) arg0;
        String pckgname = getApplicationContext().getPackageName();

		Resources res = getResources();
		showed++;
		
		if(showed >= imgname.length)
		{
			showed = 0;
		}
		
		String reslocation = pckgname + ":drawable/" + imgname[showed];
		int imgId = res.getIdentifier(reslocation, null, null);
		
		img.setImageResource(imgId);
	}
}