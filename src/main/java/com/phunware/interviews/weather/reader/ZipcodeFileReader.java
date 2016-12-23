package com.phunware.interviews.weather.reader;

import com.phunware.interviews.weather.pojo.Zip;

import java.io.File;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by ps on 12/22/16.
 */
public class ZipcodeFileReader {

    public static HashSet<Zip> getZips(File zipFile) throws Exception {
        HashSet<Zip> zips = new HashSet<Zip>();
        Scanner scanner = new Scanner(zipFile);
        while (scanner.hasNext()) {
            String line = scanner.nextLine(); // line = zipcode,city,county
            String[] parts = line.split(",");
            if (parts.length != 3) {
                throw new Exception("Invalid line: " + line);
            } else {
                Integer zipcode = null;
                try {
                    zipcode = Integer.parseInt(parts[0]);
                } catch (Exception ex) {
                    throw new Exception("Invalid zipcode: " + zipcode);
                }
                Zip zip = new Zip(zipcode, parts[1], parts[2]);
                zips.add(zip);
            }
        }
        return zips;
    }


}
