//
package com.shoppingthing.beerwidget;
import com.shoppingthing.beerwidget.R;
//
import java.util.Calendar;
//import java.util.Random;
//
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class FxMain extends Activity 
{
	// Nine part curtain 
    private static final int ALL_PARTS = 9;
    private TextView lbStatus = null;
    //private static Random randInt = new Random();
    
	// Timer
    private Handler mHandler = new Handler();
    //
    private Runnable mUpdateTimeTask = new Runnable()
    {
       public void run()
       {
           // Do something
           CxLog.d( this.toString(), "Update curtain, secs=" + Calendar.getInstance().get(Calendar.SECOND) );
           // update the view 
           updateImage();

           // timer, 60 secs
           mHandler.postDelayed(mUpdateTimeTask, ( 60 * 1000 ) ) ;
       }
    };
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        //
		this.lbStatus = (TextView) findViewById(R.id.lbStatus);
		// Beer light is off, the first 3 secs
		this.setView( ALL_PARTS );

        // timer starts in 3 
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, ( 3 * 1000 )  );
    }
    
    
    @Override
    public void onStart() 
    {
    	super.onStart();
    }
    
    @Override
    public void onStop()
    {
        super.onStop();
       
        // remove the timer
        mHandler.removeCallbacks(mUpdateTimeTask);
    }
    
    // Updates the view depending on the time of day 
    private void updateImage()
    {
    	// What hour is it
    	int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    	CxLog.d( this.toString(), " hour=" + hours );
    	
    	// For testing, random times
    	//this.setView( randInt.nextInt( 8 ) );
    	//if( true == true ) return;
    	
    	// It's a weekend, beer light ON 
    	if( Calendar.SATURDAY == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) ||
			Calendar.SUNDAY == Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
    	{
    		CxLog.d( this.toString(), ", it's a weekend, beer light is ON" );
    		this.setView( 0 );
    		return;
    	}
    	
    	// Check the clock, set the view
    	if( hours > 8 && hours < 25 )
    	{
    		// Full at 5pm
    		this.setView( 17 - hours );
    		// Testing
    		//this.setView( 1 );
    	}
    	// Full into the night, until 3
    	else if( hours > 0 && hours < 3 )
    	{
    		this.setView( 0 );
    	}
    	// It's night / after 3
    	else
    	{
    		// Beer light is off, go to sleep
    		this.setView( ALL_PARTS );
    	}
    	
    }
    
    // Sets the curtain depending on how many parts are asked for by caller
    private void setView( int piParts )
    {
    	DisplayMetrics metrics = new DisplayMetrics(); 
    	getWindowManager().getDefaultDisplay().getMetrics(metrics);
    	CxLog.d( this.toString(), " height pixels=" + metrics.heightPixels );

    	//
        View overlay = (View) findViewById(R.id.overlay);
        // Is the margin of the overlay, in order for the label to be seen
        int iStatusLabelHight = this.lbStatus.getHeight();
        int opacity = 210; // from 0 to 255
        int onePart = ( metrics.heightPixels - iStatusLabelHight ) / ALL_PARTS;
        
        CxLog.d( this.toString(), " parts=" + piParts + ", curtain=" + onePart * piParts );
        
        // Calc the curtain
        RelativeLayout.LayoutParams params = null;
        
        // turns off after it passes the drinking hours 
    	// zero means full image, otherwise it's a part
    	if( piParts <= 0 ) 
    		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 1 );
    	else
    		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, (onePart * piParts)  );

    	// black with a variable alpha
        overlay.setBackgroundColor(opacity * 0x1000000); 
        //params.gravity = Gravity.TOP;
        // Set margin on top in order for the status label to show
        params.topMargin = iStatusLabelHight;
        overlay.setLayoutParams(params);
        // update the view
        overlay.invalidate(); 
        //
        lbStatus.setText( CxUtil.getTimeString() );
   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mainmenu, menu);
	    //CxLog.d("Showing the menu");
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Which menu clicked 
		switch(item.getItemId())
	    {
		    case R.id.mnuInfo:
		    	startActivity(new Intent( this.lbStatus.getContext(), FxInfo.class));
		        CxLog.d( this.toString(), "mnuInfo clicked");
		        break;
	    }
		//	
		return super.onOptionsItemSelected(item);
	}

    
}  // EOC

