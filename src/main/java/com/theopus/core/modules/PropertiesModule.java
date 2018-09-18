package com.theopus.core.modules;

import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Module
public class PropertiesModule {

    public static final String WINDOW_WIDTH = "window.width";
    public static final String WINDOW_HEIGHT = "window.height";
    private final Map<String, String> props;

    public PropertiesModule(String path) {
        this.props = loadProperties(path);
    }

    @Provides
    @Named(WINDOW_HEIGHT)
    public Integer height() {
        return Integer.valueOf(getOrThrow(WINDOW_HEIGHT));
    }

    @Provides
    @Named(WINDOW_WIDTH)
    public Integer width() {
        return Integer.valueOf(getOrThrow(WINDOW_WIDTH));
    }

    private String getOrThrow(String key) {
        String value = props.get(key);
        if (Objects.isNull(value)) {
            throw new RuntimeException("Property " + key + "not exists.");
        }
        return value;

    }

    private Map<String, String> loadProperties(String path) {
        Map<String, String> patternMap = new HashMap<>();
        try (InputStream fileInputStream = this.getClass().getClassLoader().getResourceAsStream(path)) {
            Properties prop = new Properties();
            prop.load(fileInputStream);
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);

                patternMap.put(key, value);
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patternMap;
    }
}
