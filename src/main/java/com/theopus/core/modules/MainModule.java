package com.theopus.core.modules;

import com.theopus.core.App;
import com.theopus.core.Loop;
import com.theopus.core.base.render.Renderer;
import com.theopus.core.memory.MemoryContext;
import com.theopus.core.model.ModelRenderer;
import com.theopus.core.modules.configs.LoopConfig;
import com.theopus.core.modules.configs.PerspectiveConfig;
import com.theopus.core.modules.configs.WindowConfig;
import com.theopus.core.base.objects.Camera;
import com.theopus.core.base.objects.Light;
import com.theopus.core.base.ShaderFactory;
import com.theopus.core.base.StaticShader;
import com.theopus.core.utils.Maths;
import com.theopus.core.window.KeyListener;
import com.theopus.core.window.WindowManager;
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
    public Loop loop(LoopConfig loopConfig){
        return new Loop(loopConfig);
    }

    @Singleton
    @Provides
    public KeyListener listener() {
        return new KeyListener();
    }

    @Singleton
    @Provides
    public Camera camera(KeyListener listener){
        Camera camera = new Camera(listener);
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
    public Renderer renderer(StaticShader shaderProgram, Matrix4f mt, Camera camera) {
        Light light = new Light(new Vector3f(0,0,-20), new Vector3f(1,1,1));
        return new ModelRenderer(shaderProgram, mt, camera, light);
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
    public StaticShader staticShader(WindowManager windowManager, MemoryContext ctx) {
        windowManager.createWindow();
        StaticShader staticShader = ShaderFactory.createStaticShader("static.vert", "static.frag");
        ctx.put(staticShader);
        return staticShader;
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
                   Renderer renderer,
                   MemoryContext ctx,
                   Camera camera,
                   Loop loop) {
        return new App(wm, renderer, ctx, camera, loop);
    }
}
