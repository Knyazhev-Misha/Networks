package com.api.request;

import com.api.Json.JsonInteresting.Interesting;
import com.api.Json.JsonPlace.Place;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RequestInteresting {
    private String api_key;

    public RequestInteresting(){
        getApiKey();
    }


    public Interesting request(String lat, String lon)  {
        try {
            String lon_max = Double.toString(Double.parseDouble(lon) + 0.1);
            String lat_max = Double.toString(Double.parseDouble(lat) + 0.1);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://api.opentripmap.com/0.1/ru/places/bbox?lon_min=" + lon
                            + "&lat_min=" + lat + "&lon_max=" + lon_max + "&lat_max=" +
                            lat_max + "&format=geojson&apikey=" + api_key)
                    .build();

            Response response = null;

            response = client.newCall(request).execute();

            Gson g = new Gson();
            Interesting interesting = g.fromJson(response.body().string(), Interesting.class);

            return interesting;
        }
        catch (IOException e) {
            return null;
        }
    }

    private void getApiKey() {
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src/main/resources/api_key.properties");
            property.load(fis);

            api_key = property.getProperty("api_key_interesting");
            fis.close();
        } catch (IOException e) {
            System.err.println("Key interesting don't exist");
        }
    }

}
