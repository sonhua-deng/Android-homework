package com.example.lenovo.market;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Lenovo on 2017/10/27.
 */

public class brocast2 extends BroadcastReceiver {
    static int num=2;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extra=intent.getBundleExtra("item");
        String name=extra.getString("name");
        int imageId=extra.getInt("imageId");
        Bundle  b=new Bundle();
        b.putString("change","1");
        Intent intenActivity=new Intent(context,MainActivity.class);
        intenActivity.putExtras(b);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intenActivity,0);
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=new NotificationCompat.Builder(context)
                .setContentTitle("马上下单")
                .setAutoCancel(true)
                .setContentText(name+"已经添加到购物车")
                .setSmallIcon(imageId)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),imageId))
                .setContentIntent(pendingIntent).build();
        notificationManager.notify(++num,notification);
    }
}
