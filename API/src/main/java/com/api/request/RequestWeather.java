package com.api.request;

import com.api.JsonWeather.Weath;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class RequestWeather {
    private String api_key = "edfbf89984953c273b08d9028777f51b";

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
}
