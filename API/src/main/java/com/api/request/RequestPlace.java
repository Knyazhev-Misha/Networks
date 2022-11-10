package com.api.request;

import com.api.JsonPlace.Place;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class RequestPlace {

    private String api_key = "703926d2-dd29-4425-9591-d49100485729";
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

}
