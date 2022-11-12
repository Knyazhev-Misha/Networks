package com.api.request;

import com.api.Json.JsonDescription.Description;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RequestDescription {
    private String api_key;

    public RequestDescription(){
        getApiKey();
    }


    public Description request(String id)  {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://api.opentripmap.com/0.1/ru/places/xid/"+ id +
                            "?format=geojson&apikey=" + api_key)
                    .build();

            Response response = null;

            response = client.newCall(request).execute();

            Gson g = new Gson();
            Description description = g.fromJson(response.body().string(), Description.class);

            return description;
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
