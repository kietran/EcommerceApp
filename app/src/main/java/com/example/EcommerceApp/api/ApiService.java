package com.example.EcommerceApp.api;


import com.example.EcommerceApp.model.DistrictRequest;
import com.example.EcommerceApp.model.DistrictResponse;
import com.example.EcommerceApp.model.ProvinceRequest;
import com.example.EcommerceApp.model.ProvinceResponse;
import com.example.EcommerceApp.model.WardResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://online-gateway.ghn.vn/shiip/public-api/master-data/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("province")
    @Headers("token:052074b7-11ef-11ef-8aec-2293d60b7bbb")
    Call<ProvinceResponse> getProvinceResponse();

    @POST("district")
    @Headers("token:052074b7-11ef-11ef-8aec-2293d60b7bbb")
    Call<DistrictResponse> getDistrictResponse(@Body ProvinceRequest provinceRequest);

    @POST("ward")
    @Headers("token:052074b7-11ef-11ef-8aec-2293d60b7bbb")
    Call<WardResponse> getWardResponse(@Body DistrictRequest districtRequest);
}

