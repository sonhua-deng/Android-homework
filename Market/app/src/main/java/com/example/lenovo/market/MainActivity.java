package com.example.lenovo.market;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class MainActivity extends AppCompatActivity {
    private List<Items> rItems=new ArrayList<Items>();
    private List<Items> lItems=new ArrayList<Items>();
    private int floa=0;
    private  ItemsListAdapter adapter1;
    private ItemsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.itemsRecyclyer);
        initItems();
        Random random=new Random();
        int n=random.nextInt(10);
        Items tag=rItems.get(n);
        Bundle bundle= new Bundle();
        bundle.putString("name",tag.getName());
        bundle.putInt("rPosition",n);
        bundle.putInt("lPosition",-1);
        bundle.putString("message",tag.getMessage());
        bundle.putString("price",tag.getPrice());
        bundle.putInt("imageId",tag.getImageId());
        bundle.putString("style",tag.getStyle());

        Intent intentBroast =new Intent("com.example.lenovo.WhenOpenTheApp");
        Intent brocastW1=new Intent("android.appwidget.action.open");
        brocastW1.putExtras(bundle);
        intentBroast.putExtras(bundle);
        sendBroadcast(intentBroast);
        sendBroadcast(brocastW1);

        EventBus.getDefault().register(this);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter= new ItemsAdapter(rItems);

        ScaleInAnimationAdapter animationAdapter=new ScaleInAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));

        recyclerView.setItemAnimator(animator);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        adapter.setClickListener(new ItemsAdapter.OnItemClickListener() {
            public void onClick(View view, Items flag,int position) {
                Intent intent=new Intent(MainActivity.this,detail.class);
                intent.putExtra("name",flag.getName());
                intent.putExtra("rPosition",position);
                intent.putExtra("lPosition",-1);
                intent.putExtra("message",flag.getMessage());
                intent.putExtra("price",flag.getPrice());
                intent.putExtra("imageId",flag.getImageId());
                intent.putExtra("style",flag.getStyle());
                startActivity(intent);
            }
        });
        adapter.setLongClickListener(new ItemsAdapter.OnItemLongClickListener(){
            public void onLongClick(View view, Items flag,int position) {
                int pos=position+1;
            Toast.makeText(getApplicationContext(),"移除第"+pos+"个商品",Toast.LENGTH_SHORT).show();
                rItems.remove(position);
            }
        });

        adapter1=new ItemsListAdapter(lItems,MainActivity.this);
        ListView listView=(ListView) findViewById(R.id.list);
        listView.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Items flag=lItems.get(position);
                Intent intent=new Intent(MainActivity.this,detail.class);
                intent.putExtra("name",flag.getName());
                intent.putExtra("lPosition",position);
                intent.putExtra("rPosition",-1);
                intent.putExtra("message",flag.getMessage());
                intent.putExtra("price",flag.getPrice());
                intent.putExtra("imageId",flag.getImageId());
                intent.putExtra("style",flag.getStyle());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("移除商品");
                final int pos=position;
               dialog.setMessage("从购物车移除"+lItems.get(position).getName()+"?");
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick( DialogInterface dialogInterface,int which) {
                    }
                });
                dialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick( DialogInterface dialogInterface,int which) {
                        lItems.remove(pos);
                        adapter1.notifyDataSetChanged();
                    }
                });
                dialog.show();
                return true;
            }
        });

        FloatingActionButton floatingActionButton=(FloatingActionButton) findViewById(R.id.floatButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(floa==1) {
                    FloatingActionButton floatingActionButton=(FloatingActionButton) findViewById(R.id.floatButton);
                    floatingActionButton.setImageResource(R.drawable.shoplist);
                    RecyclerView recyclerView=(RecyclerView) findViewById(R.id.itemsRecyclyer);
                    recyclerView.setVisibility(View.VISIBLE);
                    LinearLayout linearLayout=(LinearLayout)findViewById(R.id.layoutList);
                    linearLayout.setVisibility(View.GONE);
                    floa=0;
                }
                else {
                    FloatingActionButton floatingActionButton=(FloatingActionButton) findViewById(R.id.floatButton);
                    floatingActionButton.setImageResource(R.drawable.mainpage);
                    RecyclerView recyclerView=(RecyclerView) findViewById(R.id.itemsRecyclyer);
                    recyclerView.setVisibility(View.GONE);
                    LinearLayout linearLayout=(LinearLayout)findViewById(R.id.layoutList);
                    linearLayout.setVisibility(View.VISIBLE);
                    floa=1;
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(MyEvent my) {
        if(my!=null) {
            int position=my.r;
            if(position !=-1) {
                Items items=rItems.get(position);
                lItems.add(items);
            }
            else {
                position=my.l;
                Items items=lItems.get(position);
                lItems.add(items);
            }
            adapter1.notifyDataSetChanged();
        }
    }
    public void initItems() {
        Items item1=new Items("Enchated Forest","¥ 5.00","作者","Johanna Basford",R.mipmap.enchatedforest);
        rItems.add(item1);
        Items item2=new Items("Arla Milk","¥ 59.00","产地","德国",R.mipmap.arla);
        rItems.add(item2);
        Items item3=new Items("Devondale Milk","¥ 79.00","产地","澳大利亚",R.mipmap.devondale);
        rItems.add(item3);
        Items item4=new Items("Kindle Oasis","¥ 2399.00","版本","8GB",R.mipmap.kindle);
        rItems.add(item4);
        Items item5=new Items("waitrose 早餐麦片","¥ 179.00","重量","2Kg",R.mipmap.waitrose);
        rItems.add(item5);
        Items item6=new Items("Mcvitie's 饼干","¥ 14.90","产地","英国",R.mipmap.mcvitie);
        rItems.add(item6);
        Items item7=new Items("Ferrero Rocher","¥ 132.59","重量","300g",R.mipmap.ferrero);
        rItems.add(item7);
        Items item8=new Items("Maltesers","¥ 141.43","重量","118g",R.mipmap.maltesers);
        rItems.add(item8);
        Items item9=new Items("Lindt","¥ 139.43","重量","249g",R.mipmap.lindt);
        rItems.add(item9);
        Items item10=new Items("Borggreve","¥ 28.90","重量","640g",R.mipmap.borggreve);
        rItems.add(item10);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        Intent ch=getIntent();
        if(ch!=null) {
            Bundle extra=ch.getExtras();
            if(extra!=null) {
                if(extra.getString("change")!=null) {
                    if (extra.getString("change").equals("1")) {
                        Bundle b = new Bundle();
                        b.putString("change", "2");
                        setIntent(ch.replaceExtras(b));
                        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatButton);
                        floatingActionButton.setImageResource(R.drawable.mainpage);
                        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.itemsRecyclyer);
                        recyclerView1.setVisibility(View.GONE);
                        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.layoutList);
                        linearLayout2.setVisibility(View.VISIBLE);
                        floa = 1;
                    }
                }
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
