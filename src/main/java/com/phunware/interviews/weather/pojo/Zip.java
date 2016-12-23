package com.phunware.interviews.weather.pojo;

/**
 * Created by ps on 12/22/16.
 */
public class Zip {
    private Integer zipcode;
    private String city;
    private String county;

    public Zip(Integer zipcode, String city, String county) {
        this.zipcode = zipcode;
        this.city = city;
        this.county = county;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Override
    public String toString() {
        return "Zip{" +
                "zipcode=" + zipcode +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                '}';
    }
}
