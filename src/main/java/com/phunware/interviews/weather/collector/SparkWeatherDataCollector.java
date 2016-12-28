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
    JavaPairRDD<Integer, String> zips = file.mapToPair(
        new PairFunction<String, Integer, String>() {
          public Tuple2<Integer, String> call(String line) throws Exception {
            String[] parts = line.split(",");
            System.out.println("Line: " + line);
            Integer zipcode = null;
            try {
              zipcode = Integer.parseInt(parts[0]);
            } catch (Exception ex) {
              throw new Exception("Invalid zipcode: " + zipcode);
            }
            Zip zip = new Zip(zipcode, parts[1], parts[2]);
            return new Tuple2(zipcode, zip);
          }
        });
    zips.foreach(new VoidFunction<Tuple2<Integer, String>>() {
      public void call(Tuple2<Integer, String> integerStringTuple2) throws Exception {
        Integer zipcode = integerStringTuple2._1;
        Weather weather = YahooWeatherService.getWeather(integerStringTuple2._1, config);
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
