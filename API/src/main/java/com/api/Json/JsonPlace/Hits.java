package com.api.Json.JsonPlace;

public class Hits {
    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOsm_value() {
        return osm_value;
    }

    public void setOsm_value(String osm_value) {
        this.osm_value = osm_value;
    }

    private Point point;
    private String name;
    private String country;
    private String postcode;
    private String state;
    private String osm_value;

}
