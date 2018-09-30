package com.theopus.core.modules;

import com.theopus.core.App;
import com.theopus.core.model.ModelModule;
import com.theopus.core.terrain.TerrainModule;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = {MainModule.class, PropertiesModule.class, TerrainModule.class, ModelModule.class})
@Singleton
public interface AppComponent {

    App buildApp();
}
