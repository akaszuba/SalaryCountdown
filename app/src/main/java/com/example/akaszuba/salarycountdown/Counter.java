package com.example.akaszuba.salarycountdown;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.Calendar;

import static java.util.Calendar.*;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CounterConfigureActivity CounterConfigureActivity}
 */
public class Counter extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            CounterConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //get payment day from settings
        int paymentDay = CounterConfigureActivity.loadTitlePref(context, appWidgetId);
        int currentDayofMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        int valueToDisplay = paymentDay - currentDayofMonth;
        
        if(valueToDisplay < 0) {//there was payment day in this month. Need to check how many days left to next one.
            Calendar rightNow = Calendar.getInstance();
            Calendar nextPayment = Calendar.getInstance();

            nextPayment.add(Calendar.MONTH, 1);
            nextPayment.set(Calendar.DAY_OF_MONTH, paymentDay);


            long diff = nextPayment.getTimeInMillis() - rightNow.getTimeInMillis();
            valueToDisplay = (int) (diff
                    / 3600000 // hours
                    / 24); //days

        }
        
        CharSequence widgetText = String.format("%d",valueToDisplay);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter);

        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}


