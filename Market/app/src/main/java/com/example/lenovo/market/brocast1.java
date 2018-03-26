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

public class brocast1 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extra=intent.getExtras();
        String name=extra.getString("name");
        String price=extra.getString("price");
        int imageId=extra.getInt("imageId");
        Intent intenActivity=new Intent(context,detail.class);
        intenActivity.putExtras(extra);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intenActivity,0);
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=new NotificationCompat.Builder(context)
                .setContentTitle("新商品热卖")
                .setAutoCancel(false)
                .setContentText(name+"仅售"+price+"!")
                .setSmallIcon(imageId)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),imageId))
                .setContentIntent(pendingIntent).build();
        notificationManager.notify(1,notification);
    }
}
