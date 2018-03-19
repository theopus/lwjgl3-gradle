package com.theopus.core;

import com.theopus.core.loader.SharedLibraryLoader;
import com.theopus.core.models.RawModel;
import com.theopus.core.modules.DaggerAppComponent;
import com.theopus.core.modules.PropertiesModule;
import com.theopus.core.render.Renderer;
import com.theopus.core.render.VaoLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class EntryPoint {

    private static final Logger LOG = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        SharedLibraryLoader.load();
        App app = DaggerAppComponent.builder().propertiesModule(new PropertiesModule("config.properties"))
                .build().buildApp();
        app.run();
    }
}
