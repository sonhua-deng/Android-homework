package com.example.lab11.service;
import com.example.lab11.Repos;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

import rx.Observable;
/**
 * Created by Lenovo on 2017/12/19.
 */

public interface ReposService {
    @GET("/users/{user}/repos")
    Observable<List<Repos>> getTopRepos(@Path("user")String user);
}