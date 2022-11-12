package com.api.request;

import com.api.Json.JsonPlace.Place;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RequestPlace {

    private String api_key;

    public RequestPlace(){
        getApiKey();
    }

     public Place request(String location)  {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://graphhopper.com/api/1/geocode?&locale=en&q=" + location + "&key=" + api_key)
                    .build();

            Response response = null;

            response = client.newCall(request).execute();

            Gson g = new Gson();
            Place place = g.fromJson(response.body().string(), Place.class);

            return place;
        }
        catch (IOException e) {
            return null;
        }
    }

    private void getApiKey(){
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src/main/resources/api_key.properties");
            property.load(fis);

            api_key = property.getProperty("api_key_place");
            fis.close();
        } catch (IOException e) {
            System.err.println("Key place don't exist");
        }
    }
}
