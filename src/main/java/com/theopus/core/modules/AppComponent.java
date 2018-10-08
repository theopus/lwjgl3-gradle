package com.theopus.core.modules;

import com.theopus.core.App;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = {MainModule.class, PropertiesModule.class})
@Singleton
public interface AppComponent {

    App buildApp();
}
