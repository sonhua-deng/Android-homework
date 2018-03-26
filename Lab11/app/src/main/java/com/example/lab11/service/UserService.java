package com.example.lab11.service;

import com.example.lab11.User;

import retrofit2.http.GET;
import retrofit2.http.Path;

import rx.Observable;
/**
 * Created by Lenovo on 2017/12/19.
 */

public interface UserService {
    @GET("/users/{user}")
    Observable<User> getTopUser(@Path("user")String user);
}