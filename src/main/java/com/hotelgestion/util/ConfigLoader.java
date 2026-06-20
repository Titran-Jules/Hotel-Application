package com.hotelgestion.util;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

public class ConfigLoader {
    public static Properties load() {
        Properties props = new Properties();
        try (InputStream in = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config.properties")
        ) {
            if (in == null) {
                throw new RuntimeException("config.properties unfounded in resources/");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Error of reading config.properties", e);
        }
        return props;
    }
}
