package com.phunware.interviews;

import com.phunware.interviews.weather.collector.SparkWeatherDataCollector;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Main class for the Phunware Weather application
 */
public class SparkApp {

  public static Properties getConfig(JavaSparkContext sc, Path configFile) throws Exception {
    Properties config = new Properties();
    FileSystem fs = FileSystem.get(sc.hadoopConfiguration());
    InputStream inputStream = fs.open(configFile);
    config.load(inputStream);
    return config;
  }

  public static void main(String[] args) throws Exception {
    if (args.length == 2) {
      String configPath = args[0];
      String dataFilePath = args[1];
      System.out.println("config file path : " + configPath);
      System.out.println("data file path : " + dataFilePath);
      SparkConf conf = new SparkConf().setAppName("com.phunware.interviews.SparkApp").setMaster("local[4]");
      JavaSparkContext sc = new JavaSparkContext(conf);
      Properties config = null;
      try {
        Path configFile = new Path(configPath);
        config = getConfig(sc, configFile);
        SparkWeatherDataCollector collector = new SparkWeatherDataCollector();
        collector.collect(sc, config, dataFilePath);
        sc.close();
      } catch (Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        ex.printStackTrace();
      }
    } else {
      System.out.println("USAGE: spark-submit --master local[4] --class com.phunware.interviews.SparkApp target/zipweather-1.0-SNAPSHOT.jar src/main/resources/config.properties ~/wd/data/tz.csv");
    }
  }
}
