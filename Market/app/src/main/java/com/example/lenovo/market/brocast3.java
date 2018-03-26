package com.example.lenovo.market;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class brocast3 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Bundle bundle =intent.getExtras();
        String name=bundle.getString("name");
        int imageId=bundle.getInt("imageId");
        views.setTextViewText(R.id.appwidget_text,name+"已添加到购物车");
        views.setImageViewResource(R.id.widgetImage,imageId);

        Bundle  b=new Bundle();
        b.putString("change","1");
        Intent intenActivity=new Intent(context,MainActivity.class);
        intenActivity.putExtras(b);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intenActivity,PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
        ComponentName me=new ComponentName(context,NewAppWidget.class);
        appWidgetManager.updateAppWidget(me,views);
    }
}
