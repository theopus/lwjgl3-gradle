package com.theopus.core.modules;

import com.theopus.core.App;
import com.theopus.core.Loop;
import com.theopus.core.base.window.InputHub;
import com.theopus.core.base.window.WindowManager;
import com.theopus.core.modules.configs.LoopConfig;
import com.theopus.core.modules.configs.PerspectiveConfig;
import com.theopus.core.modules.configs.WindowConfig;
import com.theopus.core.utils.Maths;
import dagger.Module;
import dagger.Provides;
import org.joml.Matrix4f;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class MainModule {


    @Singleton
    @Provides
    @Inject
    public Loop loop(LoopConfig loopConfig,
                     WindowConfig windowConfig,
                     WindowManager windowManager) {

        return new Loop(loopConfig, windowConfig.getvSync() > 0)
                .interruptOn(windowManager::windowShouldClose);
    }

    @Singleton
    @Provides
    public InputHub hub() {
        return new InputHub();
    }

    @Singleton
    @Provides
    @Inject
    public WindowManager windowManager(WindowConfig config,
                                       InputHub listener) {
        WindowManager windowManager = new WindowManager(config, listener);
        windowManager.createWindow();
        System.out.println("created");
        return windowManager;
    }

    @Singleton
    @Provides
    @Inject
    public Matrix4f projectionMatrix(WindowManager windowManager, PerspectiveConfig pc) {
        Matrix4f projMatrix = Maths.createProjectionMatrixAuto(
                (float) Math.toRadians(pc.getFov()),
                pc.getNear(),
                pc.getFar(),
                (float) windowManager.getWidth() / (float) windowManager.getHeight());
        return projMatrix;
    }

    @Provides
    @Singleton
    @Inject
    public App app(
            InputHub hub,
            WindowManager wm,
            Loop loop,
            Matrix4f pMatrix
    ) {
        return new App(wm, pMatrix, loop, hub);
    }
}
