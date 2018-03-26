package com.example.Lab_1;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    int checked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView image =(ImageView) findViewById(R.id.sysu_image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("上传头像");
                final String[] items=new String[]{"拍摄","从相册中选择"};
                dialog.setItems(items,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick( DialogInterface dialogInterface,int which) {
                        Toast.makeText(getApplicationContext(),"您选择了"+"["+items[which]+"]",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick( DialogInterface dialogInterface,int which) {
                        Toast.makeText(getApplicationContext(),"您选择了[取消]",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
        RadioGroup rg=(RadioGroup) findViewById(R.id.radioGroup);
        final RadioButton rB1=(RadioButton) findViewById(R.id.rB1);
        final RadioButton rB2=(RadioButton) findViewById(R.id.rB2);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String msg = "";
                checked=checkedId;
                if(rB1.getId()==checkedId){
                    msg = "您选择了"+rB1.getText().toString();
                }
                if(rB2.getId()==checkedId){
                    msg = "您选择了"+rB2.getText().toString();
                }
                Snackbar.make(group,msg,Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"确定按钮被点击了",Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });
        final TextInputLayout textIn1 = (TextInputLayout) findViewById(R.id.textIn1);
        final TextInputLayout textIn2 = (TextInputLayout) findViewById(R.id.textIn2);
        textIn1.setHint("请输入学号");
        textIn2.setHint("请输入密码");
        Button button1 = (Button) findViewById(R.id.Button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=textIn1.getEditText().getText().toString();
                String password=textIn2.getEditText().getText().toString();
                String a="";
                String b="123456";
                String c="6666";
                if(num.equals(a)) {
                    textIn1.setErrorEnabled(true);
                    textIn2.setErrorEnabled(false);
                    textIn1.setError("学号不能为空");
                }
                else if(password.equals(a)) {
                    textIn2.setErrorEnabled(true);
                    textIn1.setErrorEnabled(false);
                    textIn2.setError("密码不能为空");
                }
                else if(!num.equals(b)||!password.equals(c)) {
                    textIn1.setErrorEnabled(false);
                    textIn2.setErrorEnabled(false);
                    Snackbar.make(v,"学号或密码错误",Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                }
                else if(num.equals(b)&&password.equals(c)) {
                    textIn1.setErrorEnabled(false);
                    textIn2.setErrorEnabled(false);
                    Snackbar.make(v,"登陆成功",Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                }
            }
        });
        Button button2 = (Button) findViewById(R.id.Button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rB1.getId()==checked) {
                    Snackbar.make(v,"学生注册功能尚未启用",Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                }
                if(rB2.getId()==checked) {
                    Snackbar.make(v,"教职工注册功能尚未启用",Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                }

                }
        });
    }
}
