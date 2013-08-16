//
package com.shoppingthing.beerwidget;
//
import android.app.IntentService;
import android.content.Intent;


public class CxUpdateWidgetService extends IntentService 
{
	//private static Random randInt = new Random();

	public CxUpdateWidgetService() 
	{
		super("CxUpdateWidgetService");
	}

	public CxUpdateWidgetService(String name) 
	{
	    super(name);
	}

	@Override
	public void onCreate() 
	{
	    super.onCreate();
	}
	
	@Override
	public void onHandleIntent(Intent intent) 
	{
		// update widget
		CxLog.d( this.toString(), ", HandleIntent..."  );
		FxBeerWidget.updateAllWidgets( this.getApplicationContext() );
	}
	
    @Override
    public void onDestroy()
    {
    	CxLog.d( this.toString(), ", Destroy..."  );
        super.onDestroy();
    }

}// EOC