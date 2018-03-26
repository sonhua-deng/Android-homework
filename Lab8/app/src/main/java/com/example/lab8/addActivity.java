package com.example.lab8;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Button fi=(Button) findViewById(R.id.finish_add);
        fi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name_add_text=(EditText)findViewById(R.id.name_add_text);
                EditText gift_add_text=(EditText)findViewById(R.id.gift_add_text);
                EditText birth_add_text=(EditText)findViewById(R.id.birth_add_text);
                String name=name_add_text.getText().toString();
                String gift=gift_add_text.getText().toString();
                String birth=birth_add_text.getText().toString();
                Mydatabase dbHelper=new Mydatabase(addActivity.this,"Items.db",null,1);
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                int flag=0;
                if(name.equals("")) {
                    Toast.makeText(addActivity.this,"name can not be empty",Toast.LENGTH_SHORT).show();
                }
                else  {
                    Cursor cursor=db.query("item",null,null,null,null,null,null);
                    if(cursor.moveToFirst()) {
                        do {
                            String name_term =cursor.getString(cursor.getColumnIndex("name"));
                            if(name.equals(name_term)) {
                                flag=1;
                            }
                        }while(cursor.moveToNext());
                    }
                    if(flag==0) {
                        ContentValues values=new ContentValues();
                        values.put("name",name);
                        values.put("gift",gift);
                        values.put("birth",birth);
                        db.insert("item",null,values);

                        Intent intent=new Intent();
                        intent.putExtra("name",name);
                        setResult(1,intent);
                        db.close();
                        finish();
                    }
                    else {
                        Toast.makeText(addActivity.this,"name excited",Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }

            }
        });
    }
}
