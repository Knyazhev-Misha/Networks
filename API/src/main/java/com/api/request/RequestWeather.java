package com.api.request;

import com.api.Json.JsonWeather.Weath;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RequestWeather {
    private String api_key;

    public RequestWeather(){
        getApiKey();
    }

    public Weath request(String lat, String lon)  {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://api.openweathermap.org/data/2.5/weather?lat=" + lat +
                            "&lon=" + lon + "&appid=" + api_key)
                    .build();

            Response response = client.newCall(request).execute();

            Gson gr = new Gson();
            Weath weath = gr.fromJson(response.body().string(), Weath.class);

            return weath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getApiKey(){
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src/main/resources/api_key.properties");
            property.load(fis);

            api_key = property.getProperty("api_key_weather");
            fis.close();
        } catch (IOException e) {
            System.err.println("Key weather don't exist");
        }
    }
}
