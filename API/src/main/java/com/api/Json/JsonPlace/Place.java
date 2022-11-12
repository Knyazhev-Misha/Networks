package com.api.Json.JsonPlace;

import java.util.List;

public class Place {
    private List<Hits> hits;

    public int getLength(){
        return hits.size();
    }

    public String getName(int num){
        return hits.get(num).getName();
    }

    public String getLat(int num){
        Hits currentHits = hits.get(num);
        return currentHits.getPoint().getLat();
    }

    public String getLng(int num){
        Hits currentHits = hits.get(num);
        return currentHits.getPoint().getLng();
    }

    public String getPlace(int num){
        Hits currentHits = hits.get(num);
        String res = null;

        if(currentHits.getPoint().getLat() != null)
            res = "<html>lat = " + currentHits.getPoint().getLat() + "\n";
        if(currentHits.getPoint().getLng() != null)
            res = res + "<br />lng = " + currentHits.getPoint().getLng() + "\n";
        if(currentHits.getName() != null)
            res = res + "<br />name = " + currentHits.getName() + "\n";
        if(currentHits.getCountry()!= null)
            res = res + "<br />country = " + currentHits.getCountry() + "\n";
        if(currentHits.getState() != null)
            res = res + "<br />state = " + currentHits.getState() + "\n";
        if(currentHits.getPostcode() != null)
            res = res + "<br />postcode = " + currentHits.getPostcode() + "\n";
        if(currentHits.getOsm_value() != null)
            res = res + "<br />osm_value = " + currentHits.getOsm_value() + "\n";


        return  res;
    }
}
