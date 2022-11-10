package com.api.JsonPlace;

import java.util.List;

public class Place {
    private List<Hits> hits;

    public int getLength(){
        return hits.size();
    }

    public String getName(int num){
        return hits.get(num).name;
    }

    public String getLat(int num){
        Hits currentHits = hits.get(num);
        return currentHits.point.lat;
    }

    public String getLng(int num){
        Hits currentHits = hits.get(num);
        return currentHits.point.lng;
    }

    public String getPlace(int num){
        Hits currentHits = hits.get(num);
        String res = null;

        if(currentHits.point.lat != null)
            res = "<html>lat = " + currentHits.point.lat + "\n";
        if(currentHits.point.lng != null)
            res = res + "<br />lng = " + currentHits.point.lng + "\n";
        if(currentHits.name != null)
            res = res + "<br />name = " + currentHits.name + "\n";
        if(currentHits.country != null)
            res = res + "<br />country = " + currentHits.country + "\n";
        if(currentHits.state != null)
            res = res + "<br />state = " + currentHits.state + "\n";
        if(currentHits.postcode != null)
            res = res + "<br />postcode = " + currentHits.postcode + "\n";
        if(currentHits.osm_value != null)
            res = res + "<br />osm_value = " + currentHits.osm_value + "\n";


        return  res;
    }
}
