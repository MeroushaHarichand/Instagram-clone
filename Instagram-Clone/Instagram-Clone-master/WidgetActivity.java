package merousha.com.instagramclone2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import merousha.com.instagramclone2.Home.HomeActivity;

/**
 * Created by Vanil-Singh on 10/13/2017.
 */

public class WidgetActivity extends AppWidgetProvider {

    DateFormat df = new SimpleDateFormat( "hh:mm:ss" );
    public void onUpdate(Context context, AppWidgetManager
            appWidgetManager, int [] appWidgetIds) {
        final int N = appWidgetIds. length ;
        Log.i( "ExampleWidget" , "Updating widgets " +
                Arrays.asList(appWidgetIds));

// Perform this loop procedure for each App Widget that belongs to this
// provider

        for ( int i = 0 ; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
// Create an Intent to launch ExampleActivity

            Intent intent = new Intent(context, HomeActivity. class );
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 0 , intent, 0 );

// Get the layout for the App Widget and attach an on-clicklistener
// to the button
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout. widget_layout );
            views.setOnClickPendingIntent(R.id. button , pendingIntent);
// To update a label
            views.setTextViewText(R.id. widget1label , df .format( new
                    Date()));
// Tell the AppWidgetManager to perform an update on the current app
// widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}