package com.theopus.core.modules;

import com.theopus.core.modules.configs.LoopConfig;
import com.theopus.core.modules.configs.PerspectiveConfig;
import com.theopus.core.modules.configs.WindowConfig;
import dagger.Module;
import dagger.Provides;
import org.joml.Vector4f;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Module
public class PropertiesModule {

    private final Map<String, String> props;

    public PropertiesModule(String path) {
        this.props = loadProperties(path);
    }


    @Provides
    public WindowConfig wconfig() {
        return new WindowConfig(
                Integer.valueOf(getOrThrow("window.width")),
                Integer.valueOf(getOrThrow("window.height")),
                new Vector4f(
                        Float.valueOf(getOrThrow("window.cc.r")),
                        Float.valueOf(getOrThrow("window.cc.g")),
                        Float.valueOf(getOrThrow("window.cc.b")),
                        Float.valueOf(getOrThrow("window.cc.a"))
                ),
                Integer.valueOf(getOrThrow("window.vsync")));
    }

    @Provides
    public LoopConfig lconfig() {
        return new LoopConfig(
                Integer.valueOf(getOrThrow("loop.fps")),
                Integer.valueOf(getOrThrow("loop.ups")),
                Boolean.valueOf(getOrThrow("loop.logs.enabled"))
        );
    }

    @Provides
    public PerspectiveConfig pconfig() {
        return new PerspectiveConfig(
                Float.valueOf(getOrThrow("perspective.fov")),
                Float.valueOf(getOrThrow("perspective.near")),
                Float.valueOf(getOrThrow("perspective.far"))
        );
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
