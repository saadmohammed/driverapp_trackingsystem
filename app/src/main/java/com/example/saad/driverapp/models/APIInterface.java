package com.example.saad.driverapp.models;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("/create")
    Call<LocationDataModel> sendLocationData(@Body LocationDataModel locationDataModel);



    @Headers("Content-Type: application/json")
    @POST("create")
    Call<LocationDataModel> sendLocationDataJSON(@Body String body);
}
