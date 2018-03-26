package com.example.lab8;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.support.v7.widget.RecyclerView;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Items> rItems=new ArrayList<Items>();
    private ItemsAdapter adapter;
    private Mydatabase dbHelper;
    private  RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new Mydatabase(this,"Items.db",null,1);
        getContactPermission();
        inititem();


        recyclerView=(RecyclerView)findViewById(R.id.itemsRecyclyer);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter= new ItemsAdapter(rItems);
        ScaleInAnimationAdapter animationAdapter=new ScaleInAnimationAdapter(adapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        recyclerView.setItemAnimator(animator);
        recyclerView.addItemDecoration(new RecycleViewDivider(MainActivity.this, LinearLayoutManager.HORIZONTAL));

        recyclerView.getItemAnimator().setRemoveDuration(1000);
        adapter.setClickListener(new ItemsAdapter.OnItemClickListener() {
            public void onClick(View view, Items flag, final int position) {
               final View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog,null);

                TextView name=(TextView) view1.findViewById(R.id.name_dialog_text);
                EditText birth=(EditText) view1.findViewById(R.id.birth_dialog_text );
                EditText gift=(EditText) view1.findViewById(R.id.gift_dialog_text);
                TextView phone=(TextView) view1.findViewById(R.id.phone_dialog_text);
                name.setText(flag.getName());
                gift.setText(flag.getGift());
                birth.setText(flag.getBirth());
                String phone_term ="无";
                final String contactName = flag.getName();
                ContentResolver cr = MainActivity.this.getContentResolver();
                Cursor pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?",
                        new String[] { contactName }, null);
                if (pCur.moveToFirst()) {
                    phone_term = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    pCur.close();
                }
                phone.setText(phone_term);

                final AlertDialog.Builder alertDialog1=new AlertDialog.Builder(MainActivity.this);
                alertDialog1.setView(view1);
                alertDialog1.setNegativeButton("放弃修改",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick( DialogInterface dialogInterface,int which) {
                    }
                });
                alertDialog1.setPositiveButton("保存修改",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick( DialogInterface dialogInterface,int which) {
                        EditText birth_edit = (EditText) view1.findViewById(R.id.birth_dialog_text);
                        EditText gift_edit = (EditText) view1.findViewById(R.id.gift_dialog_text);

                        String birth = birth_edit.getText().toString();
                        String gift = gift_edit.getText().toString();
                        SQLiteDatabase mydb=dbHelper.getWritableDatabase();
                        ContentValues values=new ContentValues();
                        values.put("gift",gift);
                        values.put("birth",birth);
                        mydb.update("item",values,"name= ?",new String[]{contactName});
                        rItems.get(position).setGift(gift);
                        rItems.get(position).setBirth(birth);
                        adapter.notifyDataSetChanged();
                        mydb.close();
                    }
                });
                alertDialog1.show();
            }
        });
        adapter.setLongClickListener(new ItemsAdapter.OnItemLongClickListener(){
            public void onLongClick(View view, final Items flag,final int position) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                final int pos=position;
                dialog.setMessage("是否删除?");
                dialog.setNegativeButton("否",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick( DialogInterface dialogInterface,int which) {
                    }
                });
                dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick( DialogInterface dialogInterface,int which) {
                        String name=flag.getName();
                        SQLiteDatabase mydb=dbHelper.getWritableDatabase();
                        mydb.delete("item","name = ?",new String[]{name});
                        mydb.close();
                        rItems.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                });
                dialog.show();
            }
        });

        Button add=(Button)findViewById(R.id.add_item);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,addActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle=data.getExtras();
        String name=bundle.getString("name");
        if(!name.equals("")) {
            SQLiteDatabase mydb=dbHelper.getWritableDatabase();
            Cursor cursor=mydb.query("item",null,"name = ?",new String[]{name},null,null,null);
            if(cursor.moveToFirst()) {
                do {
                    Items items=new Items();
                    String birth=cursor.getString(cursor.getColumnIndex("birth"));
                    String gift=cursor.getString(cursor.getColumnIndex("gift"));
                    items.setBirth(birth);
                    items.setGift(gift);
                    items.setName(name);
                    rItems.add(items);
                    adapter.notifyDataSetChanged();
                }while(cursor.moveToNext());
            }
            mydb.close();
        }
    }
    private void inititem() {
        SQLiteDatabase mydb=dbHelper.getWritableDatabase();
        Cursor cursor=mydb.query("item",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                Items items=new Items();
                String name =cursor.getString(cursor.getColumnIndex("name"));
                String birth=cursor.getString(cursor.getColumnIndex("birth"));
                String gift=cursor.getString(cursor.getColumnIndex("gift"));
                items.setBirth(birth);
                items.setGift(gift);
                items.setName(name);
                rItems.add(items);
            }while(cursor.moveToNext());
        }
        mydb.close();
    }
    private void getContactPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }
    }
    @Override
    public  void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT);
                    Log.e("TAG","You denied the permission");
                }
        }
    }

}
