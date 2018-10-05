package com.theopus.core.terrain;

import com.theopus.core.base.memory.MemoryContext;
import com.theopus.core.base.objects.*;
import com.theopus.core.base.render.BatchRenderer;
import com.theopus.core.base.render.Renderer;
import com.theopus.core.base.shader.ShaderProgram;
import com.theopus.core.base.window.WindowManager;
import dagger.Module;
import dagger.Provides;
import org.joml.Matrix4f;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Module
public class TerrainModule {

    @Singleton
    @Provides
    @Inject
    public TerrainShader terrainShader(WindowManager windowManager, MemoryContext ctx) {
        try {
            TerrainShader terrainShader = new TerrainShader(
                    ShaderProgram.loadShader("terrain.vert", ShaderProgram.Type.VERTEX),
                    ShaderProgram.loadShader("terrain.frag", ShaderProgram.Type.FRAGMENT)
            );
            ctx.put(terrainShader);
            return terrainShader;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Singleton
    @Provides
    @Inject
    public TerrainRenderCommand terrainRenderCommand(TerrainShader terrainShader, Camera camera, Light light, Matrix4f pjMtx){
        return new TerrainRenderCommand(terrainShader, camera, light, pjMtx);
    }

    @Singleton
    @Provides
    @Inject
    public TerrainLoader terrainLoader(MemoryContext ctx){
        return new TerrainLoader(ctx);
    }


    @Singleton
    @Provides
    @Inject
    public BatchRenderer<TexturePackModel, Terrain> renderer(TerrainRenderCommand command){
        return Renderer.batchOf(command);
    }


}
