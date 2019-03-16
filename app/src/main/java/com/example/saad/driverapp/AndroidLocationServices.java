package com.example.saad.driverapp;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.example.saad.driverapp.models.APIInterface;
import com.example.saad.driverapp.models.APIManager;
import com.example.saad.driverapp.models.LocationDataModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AndroidLocationServices extends Service {

    private static final String TAG = AndroidLocationServices.class.getSimpleName();

    private LocationManager locationManager;
    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {


            Log.e("Google", "Location ChangedNew");
            if (location == null) return;
            if (isConnectingToInternet(getApplicationContext())) {
                JSONObject jsonObject = new JSONObject();

                try {
                    //this is necessary
                    /**
                     * This was important using HTTPClient because we are passing JSON Data. And now we will be using this only because we will be passing JSON Data again. So yes, this is important
                     *
                     */
                    jsonObject.put("latitude", location.getLatitude());
                    jsonObject.put("longitude", location.getLongitude());
                    jsonObject.put("rootid", 2);

                    //Creating Location object to pass to retrofit
                    LocationDataModel locationDataModel = new LocationDataModel();
                    locationDataModel.setLatitude(location.getLatitude());
                    locationDataModel.setLongitude(location.getLongitude());
                    locationDataModel.setLatitude(2);


                    //                    sendLocationData(locationDataModel);
                    sendLocationDataJSON(jsonObject);


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }

        @Override
        public void onProviderDisabled(String provider) {


        }

        @Override
        public void onProviderEnabled(String provider) {


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {


        }
    };

    private void sendLocationDataJSON(JSONObject jsonObject) {
        Log.d(TAG, jsonObject.toString() + "");
        Log.d(TAG, "Sending JSON Location Data");
        APIManager apiManager = APIManager.getApiManager();
        apiManager.sendLocationDataJSON(jsonObject.toString(), new Callback<LocationDataModel>() {
            @Override
            public void onResponse(Call<LocationDataModel> call, Response<LocationDataModel> response) {
                Log.e(TAG, "Success JSON");
                Log.d(TAG, call.toString());
                Log.d(TAG, response.toString());
            }

            @Override
            public void onFailure(Call<LocationDataModel> call, Throwable t) {
                Log.d(TAG, call.toString());
                Log.e(TAG, "Failed JSON");
            }
        });
    }


    public AndroidLocationServices() {
        // TODO Auto-generated constructor stub
    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) for (int i = 0; i < info.length; i++)
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }

        }
        return false;
    }


    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {

        super.onCreate();

        Log.e("Google", "Service Created");

    }

    @SuppressLint("MissingPermission")
    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);

        Log.e("Google", "Service Started");

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);


    }

    @Override
    public void onDestroy() {

        super.onDestroy();


        if (locationManager != null) {
            locationManager.removeUpdates(listener);

        }
    }

}
