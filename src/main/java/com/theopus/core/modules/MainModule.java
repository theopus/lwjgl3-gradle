package com.theopus.core.modules;

import com.theopus.core.App;
import com.theopus.core.Loop;
import com.theopus.core.base.render.BatchRenderer;
import com.theopus.core.base.render.RenderCommand;
import com.theopus.core.base.memory.MemoryContext;
import com.theopus.core.model.ModelRenderCommand;
import com.theopus.core.modules.configs.LoopConfig;
import com.theopus.core.modules.configs.PerspectiveConfig;
import com.theopus.core.modules.configs.WindowConfig;
import com.theopus.core.base.objects.Camera;
import com.theopus.core.base.objects.Light;
import com.theopus.core.base.shader.ShaderFactory;
import com.theopus.core.base.shader.StaticShader;
import com.theopus.core.terrain.TerrainLoader;
import com.theopus.core.terrain.TerrainRenderCommand;
import com.theopus.core.terrain.TerrainShader;
import com.theopus.core.utils.Maths;
import com.theopus.core.base.window.KeyListener;
import com.theopus.core.base.window.WindowManager;
import dagger.Module;
import dagger.Provides;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Module
public class MainModule {


    @Singleton
    @Provides
    @Inject
    public Loop loop(LoopConfig loopConfig, WindowConfig windowConfig, Camera camera, KeyListener listener){
        return new Loop(loopConfig, windowConfig.getvSync() > 0).input(() -> {
            camera.handle(listener);
        });
    }

    @Singleton
    @Provides
    public KeyListener listener() {
        return new KeyListener();
    }

    @Singleton
    @Provides
    public Camera camera(){
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
    public Light light(){
        return new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));
    }

    @Singleton
    @Provides
    @Inject
    public Matrix4f projectionMatrix(WindowManager windowManager, PerspectiveConfig pc) {
        Matrix4f projMatrix = Maths.createProjectionMatrix(pc.getFov(), pc.getNear(), pc.getFar(), windowManager.getWidth(), windowManager.getHeight());
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
                   ModelRenderCommand renderer,
                   MemoryContext ctx,
                   Loop loop,
                   TerrainLoader terrainLoader) {
        return new App(wm, renderer, ctx, loop, terrainLoader);
    }
}
