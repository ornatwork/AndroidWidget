//
package com.shoppingthing.beerwidget;
//

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.RemoteViews;
//
import com.shoppingthing.beerwidget.R;


public class FxBeerWidget extends AppWidgetProvider
{
	// Updates every ...
	static int WIDGET_UPDATE_INTERVAL = 60 * 1000;
	
    public void onReceive(Context context, Intent intent)
    {
    	// Look for  update action 	
        String action = intent.getAction();
        CxLog.d( this.toString(), "action=" + action  );
        
        // Do the update
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action))
        {
            // Extract the version number and advertise 
            try
            {
    	        PackageManager manager = context.getPackageManager();
    	        PackageInfo info = manager.getPackageInfo( context.getPackageName(), 0 );
    	        //
    	        String version = info.versionName;
    	        CxLog.d( this.toString(), ", version="  + version );
    	        //views.setTextViewText(R.id.lbVersion, "Version: "  + version );
        	    CxLog.d( this.toString(), " Debuggable=" + (( context.getPackageManager().getApplicationInfo( context.getPackageName(), 0 ).flags &= ApplicationInfo.FLAG_DEBUGGABLE ) != 0 ) );
            }
            catch (NameNotFoundException e){}

            // update the widget
            updateAllWidgets(context);

            // Setup receiver that will call the widgetService	
            CxLog.d( this.toString(), "set the alarm, CxBeerAlarmReceiver ");
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent2 =  PendingIntent.getBroadcast(context, 0, 
			                    		 					new Intent(context, CxBeerAlarmReceiver.class),
			                              					PendingIntent.FLAG_CANCEL_CURRENT);

            // Use inexact repeating which is easier on battery (system can phase events and not wake at exact times)
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 500, WIDGET_UPDATE_INTERVAL, pendingIntent2);
        }
    }
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
    	// Look for  update action 	
        CxLog.d( this.toString(), " OnUpdate widget" );
        updateAllWidgets(context);
    }

    public static void updateAllWidgets(Context context)
    {
        //
    	final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, FxBeerWidget.class));

    	// Remote view for the widget
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.beerwidget );
        
        // Setup the beer main Activity as Intent
        Intent beerIntent = new Intent();
        beerIntent.setClass( context, FxMain.class);
        beerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Setup pending intent, when widget clicked show main screen
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, beerIntent, 0);
        views.setOnClickPendingIntent(R.id.Widget, pendingIntent);
       	
    	// Time status 
		int iCurrentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		// Format it in clock like format, leading zero
        views.setTextViewText( R.id.lbStatus, CxUtil.getTimeString() ); 
        
        // Update image only if needed 
       	//int plusNum = randInt.nextInt( 8 );	
    	int plusNum = whichIcon( iCurrentHour);
    	CxLog.d( CxLog.TOKEN, "updating beer image, currhour=" + iCurrentHour + ", plusNum=" + plusNum   );
    	
    	// Set the current image 
    	views.setImageViewResource( R.id.widgetImage, R.drawable.beer72_0  + plusNum );
    	
     	// Associate the update views - Loop all id's 
        for(int i=0; i<appWidgetIds.length; ++i) 
        	AppWidgetManager.getInstance(context).updateAppWidget( appWidgetIds[i], views );

    }
    
    
	// Returns icon index to use for getting the correct 
	// icon image from resource storage
	// Default 0 is the empty beer glass, the beer in the 
	// glass fills from there 9 images total
    private static int whichIcon( int hours )
    {
    	int iRet = 9;
    	CxLog.d( CxLog.TOKEN, " hour=" + hours );
    	
    	
    	// It's a weekend, beer light ON 
    	if( Calendar.SATURDAY == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) ||
			Calendar.SUNDAY == Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
    	{
    		CxLog.d( CxLog.TOKEN, ", it's a weekend, beer light is ON" );
    		return iRet;
    	}
    	
    	
    	// For testing
    	//if( true == true )
    		//return 0;
    	
    	// Check the clock, set the view
    	if( hours > 8 && hours < 24 )
    	{
    		// Full at 5pm
    		iRet = hours -8;
    		if( iRet > 9 ) iRet = 9;
    	}
    	// Full into the night, until 3
    	else if( hours >= 0 && hours < 3 )
    	{
    		iRet = 9;
    	}
    	// It's night / after 3
    	else
    	{
    		// Beer light is off, go to sleep
    		iRet = 0;
    	}
    	
    	return iRet;
    }	

}  // EOC
