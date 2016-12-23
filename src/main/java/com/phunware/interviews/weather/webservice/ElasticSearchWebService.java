package com.phunware.interviews.weather.webservice;

import com.google.gson.JsonObject;

import java.util.Properties;

/**
 * Created by ps on 12/23/16.
 */
public class ElasticSearchWebService {



    public static void put(String json, Integer id, Properties config) throws Exception {
        String putIndexURL = config.getProperty("ELASTICSEARCHURL") +
                                "/" + config.getProperty("PHUNWAREESINDEX") +
                                "/" + config.getProperty("ESWEATHERMAPPING") + "/" +
                                id;
        System.out.println("putindexurl: " + putIndexURL);
        System.out.println("weather json: " + json);
        WebServiceClient.put(putIndexURL, json);
    }
}
