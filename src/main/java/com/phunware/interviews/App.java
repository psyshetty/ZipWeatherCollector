package com.phunware.interviews;

import com.phunware.interviews.weather.collector.WeatherDataCollector;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Main class for the Phunware Weather application
 */
public class App {

    public static Properties getConfig(File configFile) throws Exception {
        Properties config = new Properties();
        config.load(new FileInputStream(configFile.getAbsolutePath()));
        return config;
    }

    public static void main(String[] args) {
        //String filePath = "/Users/ps/wd/data/tz.csv";
        if (args.length == 2) {
            String configPath = args[0];
            String filePath = args[1];
            System.out.println("config file path : " + configPath);
            System.out.println("file path : " + filePath);
            try {
                File configFile = new File(configPath);
                Properties config = null;
                if (configFile.exists()) {
                    config = getConfig(configFile);
                }
                File zipFile = new File(filePath);
                if (zipFile.exists()) {
                    WeatherDataCollector.collect(zipFile,config);
                } else {
                    throw new Exception("File does not exist: " + filePath);
                }

            } catch (Exception ex) {
                System.out.println("Error processing input file: " + filePath);
                ex.printStackTrace();
            }
        } else {
            System.out.println("USAGE: java -cp target/zipweather-1.0-SNAPSHOT-jar-with-dependencies.jar com.phunware.interviews.App $CONFIGDIR/config.properties $DATADIR/zips.csv");
        }
    }
}
