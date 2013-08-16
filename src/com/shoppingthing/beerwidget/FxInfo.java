//
package com.shoppingthing.beerwidget;
//
import com.shoppingthing.beerwidget.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class FxInfo extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.info);
        
        // Set the email feedback link
        TextView feedback = (TextView) findViewById(R.id.txFeedBackEmail);
        feedback.setText(Html.fromHtml("<a href=\"mailto:BeerTimeApp@gmail.com\">Send Us Feedback</a>"));
        feedback.setMovementMethod(LinkMovementMethod.getInstance());

    }
}