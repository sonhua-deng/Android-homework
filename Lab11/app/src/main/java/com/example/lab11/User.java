package com.example.lab11;

import android.util.Log;

/**
 * Created by Lenovo on 2017/12/19.
 */

public class User {
    String login;
    int id;
    String blog;

    public void setLogin(String name) {
        this.login = name;
    }

    public String getLogin() {
        return login;
    }

    public String getBlog() {
        return blog;
    }

    public int getId() {
        return id;
    }

    public void setBlog(String blog) {
        Log.e("TAG","SETBLOG");
        this.blog = blog;
    }

    public void setId(int id) {
        this.id = id;
    }

}
