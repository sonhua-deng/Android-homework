package com.example.lab7;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class File extends AppCompatActivity implements View.OnClickListener{
    EditText name;
    EditText content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        name=(EditText) findViewById(R.id.name);
        content=(EditText)findViewById(R.id.content);
        Button save=(Button)findViewById(R.id.saveF);
        Button load=(Button)findViewById(R.id.loadF);
        Button clear=(Button) findViewById(R.id.clearF);
        Button delete=(Button) findViewById(R.id.deleteF);
        save.setOnClickListener(this);
        load.setOnClickListener(this);
        clear.setOnClickListener(this);
        delete.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        String fname=name.getText().toString();
        String fcontent=content.getText().toString();
        switch (v.getId()) {
            case R.id.saveF:
                if(fname.equals("")) {
                    Toast.makeText(this," File Name cannot empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    FileOutputStream fileOutputStream=null;
                    try{
                        fileOutputStream=openFileOutput(fname, Context.MODE_PRIVATE);
                        if(fcontent.equals("")) Log.e("TAG"," content Null");
                        fileOutputStream.write(fcontent.getBytes());
                    }catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this,"Save faily",Toast.LENGTH_SHORT).show();
                        Log.e("TAG","savefail");
                    }finally {
                        try{
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            Toast.makeText(this,"Save Succesfully",Toast.LENGTH_SHORT).show();
                        }catch (Exception e) {
                            e.printStackTrace();
                            Log.e("TAG","flushfail");
                            Toast.makeText(this,"Save Faily",Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                break;
            case R.id.loadF:

                if(fname.equals("")) {
                    Toast.makeText(this," File Name cannot empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    FileInputStream fileInutputStream=null;
                    try{
                        fileInutputStream=openFileInput(fname);
                        byte[] c=new byte[fileInutputStream.available()];
                        Log.e("TAG","c.length"+fileInutputStream.available());
                        if(fileInutputStream.available()!=0) {
                            while ( fileInutputStream.read(c)!=-1){};
                            String term=new String(c);
                            content.setText(term);
                        }
                        else {
                            content.setText("");
                        }

                        Toast.makeText(this,"Load successfully",Toast.LENGTH_SHORT).show();

                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this,"Fail to Load file",Toast.LENGTH_SHORT).show();
                        Log.e("TAG","saveFileNotFound");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this,"Fail to Load file",Toast.LENGTH_SHORT).show();
                        Log.e("TAG","saveIOfail");
                    }
                }
                break;
            case R.id.clearF:
                content.setText("");
                break;
            case R.id.deleteF:
                if(fname.equals("")) {
                    Toast.makeText(this," File Name cannot empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    try{
                        this.deleteFile(fname);
                        content.setText("");
                        name.setText("");
                        Toast.makeText(this,"Delete successfully",Toast.LENGTH_SHORT).show();

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this,"Fail to delete file",Toast.LENGTH_SHORT).show();
                        Log.e("TAG","saveIOfail");
                    }
                }
                break;
            default:
                break;
        }
    }
}
