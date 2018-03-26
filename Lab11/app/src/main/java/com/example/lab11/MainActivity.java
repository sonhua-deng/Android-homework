package com.example.lab11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import com.example.lab11.service.UserService;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<User> users=new ArrayList<User>();
    private ItemsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=(RecyclerView)findViewById(R.id.recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter= new ItemsAdapter(users);
        ScaleInAnimationAdapter animationAdapter=new ScaleInAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        recyclerView.setItemAnimator(animator);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        adapter.setClickListener(new ItemsAdapter.OnItemClickListener() {
            public void onClick(View view,User flag, int position) {
                Intent intent=new Intent(MainActivity.this,ReposActivity.class);
                intent.putExtra("name",flag.getLogin());
                startActivity(intent);
            }
        });
        adapter.setLongClickListener(new ItemsAdapter.OnItemLongClickListener(){
            public void onLongClick(View view, User flag,int position) {
                int pos=position+1;
                Toast.makeText(getApplicationContext(),"移除第"+pos+"个用户",Toast.LENGTH_SHORT).show();
                users.remove(position);
            }
        });

        Button fetch=(Button) findViewById(R.id.fetch);
        fetch.setOnClickListener(getUser);

        Button clear=(Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText editText=(EditText)findViewById(R.id.edit);
                editText.setText("");
            }
        });
    }

    View.OnClickListener getUser=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ProgressWheel wheel=(ProgressWheel)findViewById(R.id.progress_wheel);
            wheel.setVisibility(View.VISIBLE);
            RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler);
            recyclerView.setVisibility(View.GONE);
            getUser();
        }
    };

    //进行网络请求
    private void getUser(){
        String baseUrl = "https://api.github.com/users/";
        String name="";
        EditText editText=(EditText)findViewById(R.id.edit);
        name=editText.getText().toString();
        baseUrl=baseUrl+name+"/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkhttp())
                .build();

        UserService userService = retrofit.create(UserService.class);

        userService.getTopUser(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                        adapter.notifyDataSetChanged();
                        ProgressWheel wheel=(ProgressWheel)findViewById(R.id.progress_wheel);
                        wheel.setVisibility(View.GONE);
                        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this,"传输失败",Toast.LENGTH_SHORT).show();
                        Log.e("TAG","传输user错误");
                        ProgressWheel wheel=(ProgressWheel)findViewById(R.id.progress_wheel);
                        wheel.setVisibility(View.GONE);
                        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(User userEntity) {
                        users.add(userEntity);
                    }
                });
    }

    //createokHtttp
    private static OkHttpClient createOkhttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                         .connectTimeout(10, TimeUnit.SECONDS)
                                         .readTimeout(30,TimeUnit.SECONDS)
                                         .writeTimeout(10,TimeUnit.SECONDS).build();
        return okHttpClient;
    }
}
