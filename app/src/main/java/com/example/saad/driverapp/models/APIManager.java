package com.example.saad.driverapp.models;

import com.example.saad.driverapp.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIManager {


    private static APIInterface apiInterface;
    private static APIManager apiManager;


    public static APIManager getApiManager(){
        if(apiManager==null){
            apiManager=new APIManager();
        }
        return apiManager;
    }

    private APIManager(){
        //Create Retrofit Object
        Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface=retrofit.create(APIInterface.class);

    }



    public void sendLocationDataJSON(String jsonString, Callback<LocationDataModel> locationDataModelCallback){
        Call<LocationDataModel> locationDataModelCall = apiInterface.sendLocationDataJSON(jsonString);
        locationDataModelCall.enqueue(locationDataModelCallback);
    }


}
