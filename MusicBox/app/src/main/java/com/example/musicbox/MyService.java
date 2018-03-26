package com.example.musicbox;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;
import java.text.SimpleDateFormat;

public class MyService extends Service {
    public static MediaPlayer mp=new MediaPlayer();;
    private IBinder mBinder=new MyBinder();
    public MyService() {
        Log.d("TAG","myservice1");

    }

    @Override
    public int onStartCommand(Intent intent, int flags,int startId) {

        super.onStartCommand(intent,flags,startId);
        try{
            Log.d("TAG","onStart");
            File file=new File(Environment.getExternalStorageDirectory(),"melt.mp3");
            mp.setDataSource(file.getPath());
            //mp=MediaPlayer.create(this,R.raw.melt);
            //mp.setDataSource("/data/melt.mp3");
            mp.prepare();
            mp.setLooping(true);

        }catch (Exception e) {
            Log.d("TAG","bug");
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String times= sdf.format(mp.getDuration());
        Log.d("TAG","aaaaa"+times);
        Log.d("TAG","onStart2");
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("TAG","onBind");

        return mBinder;
    }

    public class MyBinder extends Binder {

        @Override
        protected boolean onTransact(int code, Parcel data,Parcel reply,int flags) throws RemoteException {
            switch(code) {
                case 101:
                    //播放
                    Log.d("TAG","101");
                    mp.start();
                    break;
                case 102:
                    //暂停按钮
                    Log.d("TAG","102");
                    mp.pause();
                    break;
                case 103:
                    //停止
                    Log.d("TAG","103");
                    mp.stop();
                    try {

                        mp.prepare();
                        mp.seekTo(0);
                    }catch (Exception e) {
                        Log.d("TAG","stop_prepare");
                        e.printStackTrace();
                    }
                    break;
                case 104:
                    //界面刷新
                    //Log.d("TAG","104");
                    int timeCurrent =mp.getCurrentPosition();
                    reply.writeInt(timeCurrent);
                    break;
                case 105:
                    //拖动进度条
                    int current;
                    current=data.readInt();
                    mp.seekTo(current);
                    mp.start();
                    Log.d("TAG","105");
                    break;
                case 106:
                    //获知音乐长度
                    Log.d("TAG","106");
                    int timeTotal =mp.getDuration();
                    reply.writeInt(timeTotal);
                    break;
                default:
                        break;

            }
           // Log.d("TAG","ontransact");
            return super.onTransact(code,data,reply,flags);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG","destroyService");
    }
}
