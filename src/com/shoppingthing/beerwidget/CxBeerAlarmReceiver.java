//
package com.shoppingthing.beerwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CxBeerAlarmReceiver extends BroadcastReceiver 
{

   // onReceive must be very quick and not block, so it just fires up a Service
   @Override
   public void onReceive(Context context, Intent intent) 
   {
      CxLog.d( this.toString(), "**** CxBeerAlarmReceiver invoked, starting CxUpdateWidgetService in background");
      context.startService( new Intent(context, CxUpdateWidgetService.class ) );
   }
}
