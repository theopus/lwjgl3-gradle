package com.theopus.core;

import com.theopus.core.base.load.TexturedModelLoader;
import com.theopus.core.base.memory.MemoryContext;
import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.render.BatchRenderer;
import com.theopus.core.base.window.WindowManager;
import com.theopus.core.model.ModelEntity;
import com.theopus.core.terrain.Terrain;
import com.theopus.core.terrain.TerrainLoader;
import com.theopus.core.utils.ObjParser;
import com.theopus.core.utils.Objects;
import org.joml.Vector3f;

import java.util.stream.Collectors;

public class App {

    private final WindowManager windowManager;
    private MemoryContext context;
    private Loop loop;
    private final TexturedModelLoader texturedModelLoader;
    private final TerrainLoader terrainLoader;
    private BatchRenderer<TexturedModel, ModelEntity> modelRenderer;
    private final BatchRenderer<TexturedModel, Terrain> terrainRenderer;

    public App(WindowManager windowManager, MemoryContext context, Loop loop, TexturedModelLoader texturedModelLoader, TerrainLoader terrainLoader, BatchRenderer<TexturedModel, ModelEntity> modelRenderer, BatchRenderer<TexturedModel, Terrain> terrainRenderer) {
        this.windowManager = windowManager;
        this.context = context;
        this.loop = loop;
        this.texturedModelLoader = texturedModelLoader;
        this.terrainLoader = terrainLoader;
        this.modelRenderer = modelRenderer;
        this.terrainRenderer = terrainRenderer;
    }

    public void run() throws Exception {
        windowManager.showWindow();

        ObjParser.Result treeParse = ObjParser.parse("tree.obj");

        TexturedModel treeModel = texturedModelLoader.loadModelMesh(
                treeParse.getPosArr(),
                treeParse.getIndicesArr(),
                treeParse.getTextCoordArr(),
                treeParse.getNormArr(),
                "tree.png");


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

        TexturedModel terrainModel = terrainLoader.loadTerrain(
                new Vector3f(-1000, 0, -1000),
                new Vector3f(1000, 0, 1000),
                100,
                "grass.png",
                100);


        Terrain ter = new Terrain(terrainModel);
        ter.setPosition(new Vector3f(0, -5, 0));

        modelRenderer.put(treeModel,
                Objects.generatePoints(
                        new Vector3f(-600, -5, -600),
                        new Vector3f(600, -5, 600),
                        600).stream().map(v3 -> new ModelEntity(treeModel, v3, 5)).collect(Collectors.toList()));

        modelRenderer.put(dragonVao, dragonEntity);
        terrainRenderer.put(terrainModel, ter);

        loop
                .update(() -> dragonEntity.increaseRotY(1))
                .loop();

        windowManager.close();
        context.close();
    }
}
