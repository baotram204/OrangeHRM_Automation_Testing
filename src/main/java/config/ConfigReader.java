package config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties  = new Properties();

    static {
        try {

            FileInputStream file = new FileInputStream("src/test/resources/config.properties");
            properties.load(file);


        } catch (Exception e) {
            throw new RuntimeException("Can't load file config.properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
