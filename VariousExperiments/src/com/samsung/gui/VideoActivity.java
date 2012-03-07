package com.samsung.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.VideoView;

public class VideoActivity extends CustomActivity
{
    private ImageView img = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);
        
        introVideo();
        img = (ImageView) findViewById(R.id.ambiente);
    }

    private void introVideo()
    {
        final VideoView myvideo = (VideoView) findViewById(R.id.costruzione);

		InputStream origin = null;
		OutputStream destination = null;
		File filedir = null;

		try 
		{
			origin = this.getAssets().open("prova.mp4");
			
			String destname = null;
			String sdstate = Environment.getExternalStorageState();

			if( Environment.MEDIA_MOUNTED.equals(sdstate) )
			{
				File basedir = Environment.getExternalStorageDirectory();
				filedir = new File( basedir.getAbsolutePath() + "/Android/data/com.samsung.gui/video" );
				filedir.mkdirs();
				destname = filedir.getAbsolutePath() + "/prova.mp4";
				
				destination = new FileOutputStream(destname);
			}
			else
			{
	        	filedir = this.getFilesDir();

				destination = openFileOutput("prova.mp4", MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
			}

		    byte[] buffer = new byte[65536];
		    int length;
		    while ( ( length = origin.read(buffer) ) > 0 )
		    {
		    	destination.write(buffer, 0, length);
		    }
			 
		    destination.flush();
		    destination.close();
		    origin.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    String directory = filedir.getAbsolutePath() + "/";
        
        myvideo.setVideoPath(directory + "prova.mp4");
		
		myvideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
		{
			@Override
			public void onCompletion(MediaPlayer mp) 
			{
				// TODO Auto-generated method stub
				// myvideo.setVisibility(VideoView.INVISIBLE);
				img.setVisibility(ImageView.VISIBLE);
			}
		});
		
		myvideo.start();
    }
}