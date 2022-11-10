package com.api.JsonWeather;

import java.util.List;

public class Weath {
    public List<WeatherinRequest> weather;
    public Temp main;
    public Wind wind;


    public String getWeather(){
        String res = null;
        if(weather.get(0).description != null)
            res = "<html>description = " + weather.get(0).description + "\n";
        if(weather.get(0).main != null)
            res = res + "<br />main = " + weather.get(0).main + "\n";
        if(main.feels_like != null)
            res = res + "<br />feels_like = " + main.feels_like + "\n";
        if(main.temp != null)
            res = res + "<br />temperature = " + main.temp + "\n";
        if(wind.speed != null)
            res = res + "<br />wind speed = " + wind.speed + "\n";

        return res;
    }

}
