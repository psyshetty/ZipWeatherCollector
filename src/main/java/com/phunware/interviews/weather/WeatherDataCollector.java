package com.phunware.interviews.weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.phunware.interviews.weather.pojo.Weather;
import com.phunware.interviews.weather.pojo.Zip;
import com.phunware.interviews.weather.reader.ZipcodeFileReader;
import com.phunware.interviews.weather.webservice.ElasticSearchWebService;
import com.phunware.interviews.weather.webservice.YahooWeatherService;

import java.io.File;
import java.util.HashSet;
import java.util.Properties;

/**
 * Created by ps on 12/23/16.
 */
public class WeatherDataCollector {

    public static void collect(File zipFile, Properties config) throws Exception {
        HashSet<Zip> zips = ZipcodeFileReader.getZips(zipFile);
        System.out.println("Zips size: " + zips.size());
        HashSet<Weather> weatherSet = YahooWeatherService.getWeather(zips, config);
        for (Weather weather : weatherSet) {
            String weatherJSON = toJSON(weather);
            System.out.println("weather json: " + weatherJSON);
            ElasticSearchWebService.put(weatherJSON, weather.getZipcode(), config);
        }
    }

    public static String toJSON(Weather weather) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(weather);
    }
}
