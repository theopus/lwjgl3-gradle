package com.theopus.core;

import com.theopus.core.base.load.TexturedModelLoader;
import com.theopus.core.base.objects.Camera;
import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.render.BatchRenderer;
import com.theopus.core.base.render.RenderCommand;
import com.theopus.core.base.render.Renderer;
import com.theopus.core.base.memory.MemoryContext;
import com.theopus.core.base.render.SingleRenderer;
import com.theopus.core.model.ModelEntity;
import com.theopus.core.model.ModelRenderCommand;
import com.theopus.core.terrain.Terrain;
import com.theopus.core.terrain.TerrainLoader;
import com.theopus.core.terrain.TerrainRenderCommand;
import com.theopus.core.utils.ObjParser;
import com.theopus.core.base.window.KeyListener;
import com.theopus.core.base.window.WindowManager;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class App {

    private final WindowManager windowManager;
    private final ModelRenderCommand renderer;
    private MemoryContext context;
    private Loop loop;
    private final TerrainRenderCommand terrainRenderCommand;
    private final TerrainLoader terrainLoader;

    public App(WindowManager windowManager, ModelRenderCommand renderer, MemoryContext context, Loop loop, TerrainRenderCommand terrainRenderCommand, TerrainLoader terrainLoader) {
        this.windowManager = windowManager;
        this.renderer = renderer;
        this.context = context;
        this.loop = loop;
        this.terrainRenderCommand = terrainRenderCommand;
        this.terrainLoader = terrainLoader;
    }

    public void run() throws Exception {
        windowManager.showWindow();

        ObjParser.Result parse = ObjParser.parse("dragon.obj");
        TexturedModel dragonVao = texturedModelLoader.loadModelMesh(
                parse.getPosArr(),
                parse.getIndicesArr(),
                parse.getTextCoordArr(),
                parse.getNormArr(),
                "whiteIm.png");

        dragonVao.getTexture().setReflictivity(1);
        dragonVao.getTexture().setShineDumper(10);

        ModelEntity dragonEntity = new ModelEntity(dragonVao);
        dragonEntity.setScale(0.75f);
        dragonEntity.setPosition(new Vector3f(20, 5, 20));

        BatchRenderer<TexturedModel, ModelEntity> batchRenderer = Renderer.batchOf(renderer);
        batchRenderer.put(dragonVao, dragonEntity);

        TexturedModel terrainModel = terrainLoader.loadTerrain(
                new Vector3f(-1000, 0, -1000),
                new Vector3f(1000, 0, 1000),
                100,
                "grass.png",
                100);


        Terrain ter = new Terrain(terrainModel);
        ter.setPosition(new Vector3f(0,-5,0));
        SingleRenderer<TexturedModel, Terrain> terrainSingleRenderer = Renderer.singleOf(terrainRenderCommand, terrainModel, ter);


        loop
                .update(() -> dragonEntity.increaseRotY(1))
                .render(() -> {
                    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                    batchRenderer.render();
                    terrainSingleRenderer.render();
                    windowManager.update();
                })
                .interruptOn(windowManager::windowShouldClose)
                .loop();

        windowManager.close();
        context.close();
    }
}
