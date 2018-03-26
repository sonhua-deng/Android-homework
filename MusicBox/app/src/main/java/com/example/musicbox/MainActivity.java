package com.example.musicbox;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private IBinder binder;
    private SeekBar seekBar;
    private TextView state;
    private int musicMills;
    private  int currentMills;
    private int currentProgress;
    private ImageView ima;
    private Button play;
    private ObjectAnimator objectAnimator;
    private TextView startTime;
    private TextView endTime;
    private boolean hasPermission=false;
    private boolean isPlaying=false;
    private float roate=0;
    private ServiceConnection sc;

   // public static MediaPlayer mp=new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ima=(ImageView)findViewById(R.id.image);
        play=(Button) findViewById(R.id.pause);
        Button stop=(Button) findViewById(R.id.stop);
        Button quit= (Button)findViewById(R.id.quit);
        endTime=(TextView)findViewById(R.id.endTime);
        startTime =(TextView) findViewById(R.id.currentTime);
        state=(TextView)findViewById(R.id.state);
        seekBar= (SeekBar) findViewById(R.id.sbr);
        currentMills=0;
        currentProgress=0;
        seekBar.setProgress(currentProgress);
        seekBar.setOnSeekBarChangeListener(seekListener);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        quit.setOnClickListener(this);
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name , IBinder s) {
                binder=s;
                Parcel rely=Parcel.obtain();
                Parcel data=Parcel.obtain();
                try {
                    Log.d("TAG","activity to 106");
                    binder.transact(106,data,rely,0);

                }catch (Exception e) {
                    e.printStackTrace();
                }
                if(rely!=null) {

                    musicMills=rely.readInt();
                    String time=changeTime(musicMills);
                    endTime.setText(time);
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                binder=null;
            }
        };
        //请求权限

        if( ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            hasPermission=true;
            Intent intent =new Intent(this,MyService.class);
            startService(intent);
            bindService(intent,sc,Context.BIND_AUTO_CREATE);
        }
    }
    public SeekBar.OnSeekBarChangeListener seekListener= new SeekBar.OnSeekBarChangeListener(){
        //实现当进度变化的时候的响应函数
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }
        //当用户还是点击进度条时的响应函数
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if(objectAnimator.isRunning()) {
                objectAnimator.pause();
            }
            isPlaying=false;

            Parcel rely=Parcel.obtain();
            Parcel data=Parcel.obtain();
            try {
                binder.transact(102,data,rely,0);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        //当用户离开进度条时的响应函数
        @Override
        public void onStopTrackingTouch(SeekBar sBar) {
            isPlaying=true;
            currentProgress=sBar.getProgress();

            currentMills=(int)( ((float)currentProgress/1000.00)* (float)musicMills);

            startTime.setText(changeTime(currentMills));
            Parcel rely=Parcel.obtain();
            Parcel data=Parcel.obtain();
            data.writeInt( currentMills);
            try {
                binder.transact(105,data,rely,0);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //响应请求权限
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[]grantResults) {
        switch(requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent =new Intent(this,MyService.class);
                    hasPermission=true;
                    startService(intent);
                    bindService(intent,sc,Context.BIND_AUTO_CREATE);
                } else {
                    Toast.makeText(this, " No permission", Toast.LENGTH_LONG);
                }
                break;
            default:
                break;

        }
    }



    @Override
    public  void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause:
                if(!isPlaying) {
                    if(objectAnimator==null) {
                        objectAnimator = ObjectAnimator.ofFloat(ima, "rotation", roate,360);
                        // 设置持续时间
                        objectAnimator.setDuration(10000);
                        // 设置循环播放
                        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                        LinearInterpolator lir = new LinearInterpolator();
                        objectAnimator.setInterpolator(lir);

                    }
                    if(!objectAnimator.isStarted()) {
                        objectAnimator.start();
                    }
                    if(objectAnimator.isPaused()){
                        objectAnimator.resume();
                    }

                    isPlaying=true;
                    play.setText("Pause");
                    state.setText("Playing");
                   Parcel rely=Parcel.obtain();
                    Parcel data=Parcel.obtain();
                    try {
                        binder.transact(101,data,rely,0);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    mThread.start();

                }
                else {
                    if(objectAnimator.isRunning()) {
                        objectAnimator.pause();
                    }
                    isPlaying=false;
                    play.setText("Play");
                    state.setText("Pause");
                    Parcel rely=Parcel.obtain();
                    Parcel data=Parcel.obtain();
                    try {
                        binder.transact(102,data,rely,0);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.stop:
                if(objectAnimator.isStarted()) {
                    objectAnimator.end();
                }
                isPlaying=false;
                seekBar.setProgress(0);
                currentMills=0;
                currentProgress=0;
                play.setText("Play");
                state.setText("Stopped");
                roate=0;
                startTime.setText(changeTime(currentMills));
                Parcel rely=Parcel.obtain();
                Parcel data=Parcel.obtain();
                try {
                    binder.transact(103,data,rely,0);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.quit:
                unbindService(sc);
                sc=null;
                try {
                    MainActivity.this.finish();
                    System.exit(0);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public String  changeTime(int dates) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String times= sdf.format(dates);
        return times;
    }
    final Handler mHandler =new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 100://过了100毫秒
                    int add_time=(int)(1000.00*((float)currentMills/(float)musicMills));
                    currentProgress=add_time;
                    if(currentProgress>1000) {
                        currentProgress=currentProgress-1000;
                    }
                    seekBar.setProgress(currentProgress);
                    startTime.setText(changeTime(currentMills));
            }
        }
    };
    Thread mThread =new Thread() {
        @Override
        public void run() {
            while(true) {
                Parcel rely=Parcel.obtain();
                Parcel data=Parcel.obtain();

               try {
                    binder.transact(104,data,rely,0);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                if(rely!=null) {

                    currentMills=rely.readInt();
                }
                //currentMills+=1000;
                try {
                        Thread.sleep(100);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isPlaying&&sc !=null &&hasPermission==true) {
                    mHandler.obtainMessage(100).sendToTarget();
                }
            }
        }
    };

    /** * 停止动画 * */
    public void stopAnimation() {
        objectAnimator.end();
        ima.clearAnimation();
        roate = 0;// 重置起始位置
    }/** * 暂停动画 * */

    public void pauseAnimation() {
        objectAnimator.cancel();
        ima.clearAnimation();// 清除此ImageView身上的动画
    }
    /*@Override
    public void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        // 控件被移除时，取消动画
        objectAnimator.cancel();
        ima.clearAnimation();
        // 清除此ImageView身上的动画
        ima.clearAnimation();// 清除此ImageView身上的动画
        //
    }*/
    @Override
    public void onDestroy() {
        Log.d("TAG","destroyMainactivity");
        super.onDestroy();
        objectAnimator.cancel();
        ima.clearAnimation();
        unbindService(sc);
    }
}
