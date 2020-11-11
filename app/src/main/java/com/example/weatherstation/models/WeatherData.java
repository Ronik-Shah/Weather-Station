package com.example.weatherstation.models;

public class WeatherData {
    int imageResource;
    String data;
    String result;

    public WeatherData(String data,String result,int imageResource){
        this.data = data;
        this.imageResource = imageResource;
        this.result = result;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getData() {
        return data;
    }

    public String getResult() {
        return result;
    }
}
