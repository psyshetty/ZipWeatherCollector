package com.phunware.interviews.weather.webservice;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.phunware.interviews.weather.pojo.Weather;
import com.phunware.interviews.weather.pojo.Zip;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Properties;

/**
 * Created by ps on 12/22/16.
 */
public class YahooWeatherService {

    public static HashSet<Weather> getWeather(HashSet<Zip> zipcodes, Properties config) throws Exception {
        HashSet<Weather> weatherSet = new HashSet<Weather>();
        for (Zip zip: zipcodes) {
            Weather weather = getWeather(zip.getZipcode(), config);
            if (weather != null) weatherSet.add(weather);
        }
        return weatherSet;
    }

    public static Weather getWeather(Integer zipcode, Properties config) throws Exception {
        String requestURL = createRequestURL(zipcode, config);
        JsonElement results = null;
        // the weather api returns results json element as null to rate limit requests.
        // i.e. results.isJsonNull() = true for a rate limited response
        // hence sleeping for 2s for every rate limited response before retrying for a max. of 5 attempts
        int numAttempts = 1;
        while ((results == null || results.isJsonNull()) &&
                numAttempts <= Integer.parseInt(config.getProperty("NUM_YQL_RETRIES"))) {
            JsonObject yahooWeatherResponse = WebServiceClient.get(requestURL);
            results = yahooWeatherResponse.getAsJsonObject("query").get("results");
            System.out.println("ATTEMPT " + numAttempts + " for zipcode: " + zipcode + "-> results: " + results);
            numAttempts++;
            Thread.sleep(Integer.parseInt(config.getProperty("SLEEP_INTERVAL_BEFORE_RETRY"))); // sleep for 2s before retrying
        }
        Weather weather = null;
        if (results != null && !results.isJsonNull()) {
            JsonElement currentDayForecast = results.getAsJsonObject().getAsJsonObject("channel").getAsJsonObject("item").getAsJsonArray("forecast").get(0);
            if (currentDayForecast.isJsonObject()) {
                Float highTemp = ((JsonObject) currentDayForecast).get("high").getAsFloat();
                Float lowTemp = ((JsonObject) currentDayForecast).get("low").getAsFloat();
                weather = new Weather(lowTemp, highTemp, zipcode);
                System.out.println(weather);
            }
        }
        return weather;
    }

    private static String createRequestURL(Integer zipcode, Properties config) throws Exception {
        String query = "select * from weather.forecast where woeid in " +
                            "(select woeid from geo.places where text='" + zipcode + "' limit 1)";
        String requestURL = config.getProperty("YAHOO_WEATHER_API_BASE_URL") +
                                    URLEncoder.encode(query, "UTF-8") + "&format=json";
        return requestURL;
    }

}
