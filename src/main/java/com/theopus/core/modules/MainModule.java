package com.theopus.core.modules;

import com.theopus.core.App;
import com.theopus.core.Loop;
import com.theopus.core.Render;
import com.theopus.core.base.load.MaterialModelLoader;
import com.theopus.core.base.objects.MaterialModel;
import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.render.BatchRenderer;
import com.theopus.core.base.memory.MemoryContext;
import com.theopus.core.model.ModelEntity;
import com.theopus.core.modules.configs.LoopConfig;
import com.theopus.core.modules.configs.PerspectiveConfig;
import com.theopus.core.modules.configs.WindowConfig;
import com.theopus.core.base.objects.Camera;
import com.theopus.core.base.objects.Light;
import com.theopus.core.terrain.Terrain;
import com.theopus.core.terrain.TerrainLoader;
import com.theopus.core.utils.Maths;
import com.theopus.core.base.window.KeyListener;
import com.theopus.core.base.window.WindowManager;
import dagger.Module;
import dagger.Provides;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class MainModule {


    @Singleton
    @Provides
    @Inject
    public Loop loop(LoopConfig loopConfig,
                     WindowConfig windowConfig,
                     Camera camera,
                     KeyListener listener,
                     Render render,
                     WindowManager windowManager) {

        return new Loop(loopConfig, windowConfig.getvSync() > 0)
                .input(() -> camera.handle(listener))
                .render(() -> {
                    render.render();
                    windowManager.update();
                })
                .interruptOn(windowManager::windowShouldClose);
    }

    @Singleton
    @Provides
    public KeyListener listener() {
        return new KeyListener();
    }

    @Singleton
    @Provides
    public Camera camera() {
        Camera camera = new Camera();
        return camera;
    }

    @Singleton
    @Provides
    @Inject
    public WindowManager windowManager(WindowConfig config,
                                       KeyListener listener) {
        return new WindowManager(config, listener);
    }

    @Singleton
    @Provides
    @Inject
    public Light light() {
        return new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
    }

    @Singleton
    @Provides
    @Inject
    public Matrix4f projectionMatrix(WindowManager windowManager, PerspectiveConfig pc) {
        Matrix4f projMatrix = Maths.createProjectionMatrixAuto(
                (float) Math.toRadians(pc.getFov()),
                pc.getNear(),
                pc.getFar(),
                (float)windowManager.getWidth() / (float) windowManager.getHeight());
        return projMatrix;
    }


    @Singleton
    @Provides
    @Inject
    public MemoryContext ctx() {
        return new MemoryContext();
    }


    @Provides
    @Singleton
    @Inject
    public App app(WindowManager wm,
                   MemoryContext ctx,
                   Loop loop,
                   MaterialModelLoader materialModelLoader,
                   TerrainLoader terrainLoader,
                   BatchRenderer<MaterialModel, ModelEntity> modelRenderer,
                   BatchRenderer<TexturedModel, Terrain> terrainRenderer
    ) {
        return new App(wm, ctx, loop, materialModelLoader, terrainLoader, modelRenderer, terrainRenderer);
    }

    @Provides
    @Singleton
    @Inject
    public Render render(BatchRenderer<MaterialModel, ModelEntity> modelRenderer,
                         BatchRenderer<TexturedModel, Terrain> terrainRenderer) {
        Render render = new Render();
        render.add(modelRenderer);
        render.add(terrainRenderer);
        return render;
    }
}
