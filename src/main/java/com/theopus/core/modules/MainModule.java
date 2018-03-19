package com.theopus.core.modules;

import com.theopus.core.App;
import com.theopus.core.render.WindowManager;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.theopus.core.modules.PropertiesModule.WINDOW_HEIGHT;
import static com.theopus.core.modules.PropertiesModule.WINDOW_WIDTH;

@Module
public class MainModule {

    @Singleton
    @Provides
    @Inject
    public WindowManager windowManager(@Named(WINDOW_WIDTH) Integer width, @Named(WINDOW_HEIGHT) Integer height){
        return new WindowManager(width, height);
    }

    @Provides
    @Singleton
    @Inject
    public App app(WindowManager wm) {
        return new App(wm);
    }
}
