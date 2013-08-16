//
package com.shoppingthing.beerwidget;

import java.text.DecimalFormat;
import java.util.Calendar;

public class CxUtil 
{
	// Returns time string as hours and minutes with leading zero as needed
    public static String getTimeString()
    {
    	// Time status 
    	String sRet = "";
		int iCurrentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int iCurrentMinute = Calendar.getInstance().get(Calendar.MINUTE);
		DecimalFormat timeFormat= new DecimalFormat("00");
		
		// Format it in clock like format, leading zero
        if( iCurrentHour < 4  || 
        		iCurrentHour > 16 || 
        		Calendar.SATURDAY == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) ||
    			Calendar.SUNDAY == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        	)  
        	sRet = "Beer time: ";

        
        // Add the time 
        sRet = sRet + timeFormat.format( iCurrentHour ) + ":" + timeFormat.format( iCurrentMinute ) ;
        
        return sRet;
    }

}  // EOC