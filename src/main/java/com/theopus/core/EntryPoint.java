package com.theopus.core;

import com.theopus.core.utils.SharedLibraryLoader;
import com.theopus.core.modules.DaggerAppComponent;
import com.theopus.core.modules.PropertiesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String[] args) throws Exception {
        SharedLibraryLoader.load();
        App app = DaggerAppComponent.builder()
                .propertiesModule(new PropertiesModule("config.properties"))
                .build()
                .buildApp();
        app.run();
    }
}
