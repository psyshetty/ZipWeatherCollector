package com.phunware.interviews.weather.collector;

import com.phunware.interviews.weather.pojo.Weather;
import com.phunware.interviews.weather.pojo.Zip;
import com.phunware.interviews.weather.webservice.ElasticSearchWebService;
import com.phunware.interviews.weather.webservice.YahooWeatherService;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Properties;

import static com.phunware.interviews.weather.collector.WeatherDataCollector.toJSON;

/**
 * Created by ps on 12/28/16.
 */
public class SparkWeatherDataCollector implements Serializable {

  public void collect(JavaSparkContext sc, final Properties config, String inputFileName) {
    JavaRDD<String> file = sc.textFile(inputFileName);
    System.out.println("Input file: " + inputFileName);
    file.foreach(new VoidFunction<String>(){
      public void call(String line) throws Exception {
            String[] parts = line.split(",");
            System.out.println("Line: " + line);
            Integer zipcode = null;
            try {
              zipcode = Integer.parseInt(parts[0]);
            } catch (Exception ex) {
              throw new Exception("Invalid zipcode: " + zipcode);
            }
        Weather weather = YahooWeatherService.getWeather(zipcode, config);
        if (weather != null) {
          String weatherJSON = toJSON(weather);
          System.out.println("zip : " + zipcode + ", weather json: " + weatherJSON);
          ElasticSearchWebService.put(weatherJSON, weather.getZipcode(), config);
        } else {
          System.out.println("Could not obtain weather for zipcode: " + zipcode);
        }
      }
    });
  }
}
