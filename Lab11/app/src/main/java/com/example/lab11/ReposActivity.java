package com.example.lab11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lab11.service.ReposService;
import com.example.lab11.service.UserService;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReposActivity extends AppCompatActivity {
    private List<Repos> repos=new ArrayList<Repos>();
    private ItemsListAdapter adapter1;
    String name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);

        adapter1=new ItemsListAdapter(repos,ReposActivity.this);
        ListView listView=(ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter1);

        Intent intetn=getIntent();
        name=intetn.getStringExtra("name");
        getRepos();
    }

    //进行网络请求
    private void getRepos(){
        String baseUrl = "https://api.github.com/users/";

        baseUrl=baseUrl+name+"/repos/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkhttp())
                .build();

        ReposService reposService = retrofit.create(ReposService.class);

        reposService.getTopRepos(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repos>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("TAG","传输repos成功");
                        ProgressWheel wheel=(ProgressWheel)findViewById(R.id.l_wheel);
                        wheel.setVisibility(View.GONE);
                        ListView listView=(ListView)findViewById(R.id.listview);
                        listView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ReposActivity.this,"传输失败",Toast.LENGTH_SHORT).show();
                        Log.e("TAG","传输repos错误");
                        ProgressWheel wheel=(ProgressWheel)findViewById(R.id.l_wheel);
                        wheel.setVisibility(View.GONE);
                        ListView listView=(ListView)findViewById(R.id.listview);
                        listView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(List<Repos> r) {
                        for(int i=0;i<r.size();i++) {
                            repos.add(r.get(i));
                        }
                        adapter1.notifyDataSetChanged();
                        Log.e("TAG","next"+r.size()+"aa"+repos.size());
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
