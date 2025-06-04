package BrowserDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties prop;

    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/java/resources/config.properties");
            prop = new Properties();
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load config file.");
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}


