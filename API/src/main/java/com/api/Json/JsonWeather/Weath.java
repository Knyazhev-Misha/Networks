package com.api.Json.JsonWeather;

import java.util.List;

public class Weath {
    private List<WeatherinRequest> weather;
    private Temp main;
    private Wind wind;


    public String getWeather(){
        String res = null;
        if(weather.get(0).getDescription() != null)
            res = "<html>description = " + weather.get(0).getDescription()+ "\n";
        if(weather.get(0).getMain() != null)
            res = res + "<br />main = " + weather.get(0).getMain() + "\n";
        if(main.getFeels_like() != null)
            res = res + "<br />feels_like = " + reverseTemperature(main.getFeels_like()) + "\n";
        if(main.getTemp() != null)
            res = res + "<br />temperature = " + reverseTemperature(main.getTemp()) + "\n";
        if(wind.getSpeed() != null)
            res = res + "<br />wind speed = " + wind.getSpeed() + "m\\h\n";

        return res;
    }

    private String reverseTemperature(String temp){
        double tempD = Double.parseDouble(temp) - 273.0;
        return Double.toString(tempD);
    }

}
