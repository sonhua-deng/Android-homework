package com.example.lab7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText newp;
    EditText conp;
    boolean is_log;
    String password;
    SharedPreferences share;
    public static final String PREFERENCE_NAME = "SaveSetting";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button clear=(Button) findViewById(R.id.clearP);
        Button okP=(Button)findViewById(R.id.okP);
        clear.setOnClickListener(this);
        okP.setOnClickListener(this);
        newp=(EditText)findViewById(R.id.newP);
        conp=(EditText)findViewById(R.id.conP);

        share=this.getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE);
        password=share.getString("password","*/empty");
        Log.e("TAG",password);
        if(!password.equals("*/empty")) {
            is_log=true;
            newp.setVisibility(View.GONE);
            conp.setHint("Password");
        }
        else is_log=false;
    }

    @Override
    public void onClick(View v) {
        if(is_log==false) {
            switch (v.getId()) {
                case R.id.clearP:
                    newp.setText("");
                    conp.setText("");
                    break;
                case R.id.okP:
                    if(newp.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(),"Password cannot be empty",Toast.LENGTH_SHORT).show();
                    }
                    else if(!newp.getText().toString().equals(conp.getText().toString())) {
                        Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
                    }
                    else if(newp.getText().toString().equals(conp.getText().toString())) {
                        SharedPreferences.Editor editor=share.edit();
                        editor.putString("password",newp.getText().toString());
                        editor.commit();
                        Intent intent=new Intent(this,File.class);
                        startActivity(intent);
                        this.finish();
                    }
                    break;
                default:
                    break;
            }
        }
        else {
            switch (v.getId()) {
                case R.id.clearP:
                    newp.setText("");
                    conp.setText("");
                    break;
                case R.id.okP:
                    if(conp.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(),"Password cannot be empty",Toast.LENGTH_SHORT).show();
                    }
                    else if(!conp.getText().toString().equals(password)) {
                        Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
                    }
                    else if(conp.getText().toString().equals(password)) {
                        Intent intent=new Intent(this,File.class);
                        startActivity(intent);
                        this.finish();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
