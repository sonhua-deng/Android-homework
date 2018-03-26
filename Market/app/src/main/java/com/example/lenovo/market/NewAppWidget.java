package com.example.lenovo.market;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;


public class NewAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews updateViews=new RemoteViews(context.getPackageName(),R.layout.new_app_widget);
        Intent i=new Intent(context,MainActivity.class);
        PendingIntent pi= PendingIntent.getActivity(context,0,i, PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.widget,pi);
        ComponentName me=new ComponentName(context,NewAppWidget.class);
        appWidgetManager.updateAppWidget(me,updateViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Bundle bundle =intent.getExtras();
        String name=bundle.getString("name");
        String price=bundle.getString("price");
        int imageId=bundle.getInt("imageId");
        if(intent.getAction().equals("android.appwidget.action.open")) {
            views.setTextViewText(R.id.appwidget_text,name+"仅售"+price);
            views.setImageViewResource(R.id.widgetImage,imageId);
            Intent i=new Intent(context,detail.class);
            i.putExtras(bundle);
            PendingIntent pi= PendingIntent.getActivity(context,0,i, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget,pi);
            ComponentName me=new ComponentName(context,NewAppWidget.class);
            appWidgetManager.updateAppWidget(me,views);
        }
    }
}

