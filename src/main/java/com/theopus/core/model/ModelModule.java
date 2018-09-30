package com.theopus.core.model;


import com.theopus.core.base.load.TexturedModelLoader;
import com.theopus.core.base.memory.MemoryContext;
import com.theopus.core.base.objects.Camera;
import com.theopus.core.base.objects.Light;
import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.render.BatchRenderer;
import com.theopus.core.base.render.Renderer;
import com.theopus.core.base.shader.ShaderFactory;
import com.theopus.core.base.shader.StaticShader;
import com.theopus.core.base.window.WindowManager;
import dagger.Module;
import dagger.Provides;
import org.joml.Matrix4f;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class ModelModule {

    @Singleton
    @Provides
    @Inject
    public ModelRenderCommand modelRenderCommand(StaticShader shaderProgram, Matrix4f mt, Camera camera, Light light) {
        return new ModelRenderCommand(shaderProgram, mt, camera, light);
    }

    @Singleton
    @Provides
    @Inject
    public BatchRenderer<TexturedModel, ModelEntity> renderer(ModelRenderCommand command) {
        return Renderer.batchOf(command);
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
    public TexturedModelLoader texturedModelLoader(MemoryContext context) {
        return new TexturedModelLoader(context);
    }

}
