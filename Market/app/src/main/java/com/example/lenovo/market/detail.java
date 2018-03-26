package com.example.lenovo.market;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class detail extends AppCompatActivity{
    private List<detailItem> dItems=new ArrayList<detailItem>();
    private  detailListAdapter adapterd;
    private brocast2 brocast;
    private brocast3 brocastw;
    private Bundle extra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        extra=getIntent().getExtras();
        String name=extra.getString("name");
        String price=extra.getString("price");
        String style=extra.getString("style");
        String message=extra.getString("message");
        int imageId=extra.getInt("imageId");
        final int rp=extra.getInt("rPosition");
        final int lp=extra.getInt("lPosition");
        TextView nameDetail=(TextView) findViewById(R.id.nameDetail);
        nameDetail.setText(name);
        ImageView imageDetail =(ImageView) findViewById(R.id.imageDetail);
        imageDetail.setImageResource(imageId);
        TextView styleDetail=(TextView) findViewById(R.id.styleDetail);
        styleDetail.setText(style);
        TextView messageDetail=(TextView) findViewById(R.id.messageDetail);
        messageDetail.setText(message);
        TextView priceDetail=(TextView) findViewById(R.id.priceDetail);
        priceDetail.setText(price);
        ImageView starDetail=(ImageView) findViewById(R.id.imageStar);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
        starDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ImageView starDetail=(ImageView) findViewById(R.id.imageStar);
                final Bitmap bmap = ((BitmapDrawable)starDetail.getDrawable()).getBitmap();
                Drawable myDrawable = getDrawable(R.drawable.empty_star);
                final Bitmap myStar = ((BitmapDrawable) myDrawable).getBitmap();
                if (bmap.sameAs(myStar)) {
                    starDetail.setImageResource(R.drawable.full_star);
                }
                else {
                    starDetail.setImageResource(R.drawable.empty_star);
                }
            }
        });

        IntentFilter dynamic=new IntentFilter();
        dynamic.addAction("com.example.lenovo.broast2");
        brocast=new brocast2();
        registerReceiver(brocast,dynamic);

        IntentFilter bwidget=new IntentFilter();
        bwidget.addAction("com.example.lenovo.bw");
        brocastw =new brocast3();
        registerReceiver(brocastw, bwidget);

        ImageView shop=(ImageView) findViewById(R.id.shopDetail);
        shop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MyEvent(rp,lp));
                Toast.makeText(view.getContext(),"商品已加到购物车",Toast.LENGTH_SHORT).show();
                Intent intentBroast =new Intent("com.example.lenovo.broast2");
                intentBroast.putExtra("item",extra);
                sendBroadcast(intentBroast);
                Intent intentBroastw =new Intent("com.example.lenovo.bw");
                intentBroastw.putExtras(extra);
                sendBroadcast(intentBroastw);
            }
        });
        ImageView back=(ImageView)findViewById(R.id.imageView3);
        initItem();
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapterd=new detailListAdapter(dItems,detail.this);
        ListView listView=(ListView) findViewById(R.id.detailList);
        listView.setAdapter(adapterd);
    }
    public void initItem() {
        detailItem item1=new detailItem("一键下单");
        dItems.add(item1);
        detailItem item2=new detailItem("分享商品");
        dItems.add(item2);
        detailItem item3=new detailItem("不感兴趣");
        dItems.add(item3);
        detailItem item4=new detailItem("查看更多促销信息");
        dItems.add(item4);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(brocast);
    }
    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}
