package com.phunware.interviews.weather.pojo;

/**
 * Created by ps on 12/22/16.
 */
public class Weather {
    private Float lowTemperature;
    private Float highTemperature;
    private Integer zipcode;

    public Weather() {}

    public Weather(Float lowTemperature, Float highTemperature, Integer zipcode) {
        this.lowTemperature = lowTemperature;
        this.highTemperature = highTemperature;
        this.zipcode = zipcode;
    }

    public Float getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(Float lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public Float getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(Float highTemperature) {
        this.highTemperature = highTemperature;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "lowTemperature='" + lowTemperature + '\'' +
                ", highTemperature='" + highTemperature + '\'' +
                ", zipcode=" + zipcode +
                '}';
    }
}
