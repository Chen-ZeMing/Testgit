package com.example.administrator.robot.net;

import com.example.administrator.robot.bean.Ask;
import com.example.administrator.robot.bean.Take;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by cz
 * 描述: Retrofit接口
 */

public interface Api {
    //Retrofit将Http请求抽象成Java接口，并在接口里面采用注解来配置网络请求参数
    //// @post注解的作用:采用Get方法发送网络请求
    //发送json数据形式的post请求，把网络请求接口的后半部分openapi/api/v写在里面
    //Ask是请求数据实体类，Take接受数据实体类
    @POST("openapi/api/v2")
    Call<Take> request(@Body Ask ask);
}
