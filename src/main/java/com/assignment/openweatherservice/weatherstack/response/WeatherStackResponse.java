package com.assignment.openweatherservice.weatherstack.response;

import java.io.Serializable;

public class WeatherStackResponse {

    private Request request;
    private Location location;
    private Current current;

    public WeatherStackResponse() {
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }
}
