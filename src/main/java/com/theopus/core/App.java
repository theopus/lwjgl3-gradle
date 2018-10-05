package com.theopus.core;

import com.theopus.core.base.load.MaterialModelLoader;
import com.theopus.core.base.memory.MemoryContext;
import com.theopus.core.base.objects.MaterialModel;
import com.theopus.core.base.objects.TexturePackModel;
import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.render.BatchRenderer;
import com.theopus.core.base.window.WindowManager;
import com.theopus.core.model.ModelEntity;
import com.theopus.core.terrain.Terrain;
import com.theopus.core.terrain.TerrainLoader;
import com.theopus.core.utils.Objects;
import org.joml.Vector3f;

import java.util.stream.Collectors;

public class App {

    private final WindowManager windowManager;
    private MemoryContext context;
    private Loop loop;
    private final MaterialModelLoader materialModelLoader;
    private final TerrainLoader terrainLoader;
    private BatchRenderer<MaterialModel, ModelEntity> modelRenderer;
    private final BatchRenderer<TexturePackModel, Terrain> terrainRenderer;

    public App(WindowManager windowManager, MemoryContext context, Loop loop, MaterialModelLoader materialModelLoader, TerrainLoader terrainLoader, BatchRenderer<MaterialModel, ModelEntity> modelRenderer, BatchRenderer<TexturePackModel, Terrain> terrainRenderer) {
        this.windowManager = windowManager;
        this.context = context;
        this.loop = loop;
        this.materialModelLoader = materialModelLoader;
        this.terrainLoader = terrainLoader;
        this.modelRenderer = modelRenderer;
        this.terrainRenderer = terrainRenderer;
    }

    public void run() throws Exception {
        windowManager.showWindow();

        MaterialModel grassModel = materialModelLoader.loadTexturedModel("grassModel.obj", "grassTexture.png");
        grassModel.getMaterial().getTexture().setHasTransparency(true);
        grassModel.getMaterial().setUseFakeLight(true);
        MaterialModel fernModel = materialModelLoader.loadTexturedModel("fern.obj", "fern.png");
        fernModel.getMaterial().getTexture().setHasTransparency(true);
        fernModel.getMaterial().setUseFakeLight(true);
        MaterialModel treeModel = materialModelLoader.loadTexturedModel("tree.obj", "tree.png");
        treeModel.getMaterial().setReflectivity(0);
        treeModel.getMaterial().setShineDamper(10);
        MaterialModel dragonModel = materialModelLoader.loadTexturedModel("dragon.obj", "whiteIm.png");
        dragonModel.getMaterial().setReflectivity(1);
        dragonModel.getMaterial().setShineDamper(10);

        ModelEntity dragonEntity = new ModelEntity(dragonModel);
        dragonEntity.setScale(0.75f);
        dragonEntity.setPosition(new Vector3f(20, 5, 20));

        TexturePackModel terrainModel = terrainLoader.loadTerrain(
                "blendMap.png",
                "mud.png",
                "grassy2.png",
                "grassFlowers.png",
                "path.png");


        Terrain ter = new Terrain(terrainModel);
        ter.setPosition(new Vector3f(0, -5, 0));

        modelRenderer.put(treeModel,
                Objects.generatePoints(
                        new Vector3f(-600, -5, -600),
                        new Vector3f(600, -5, 600),
                        600).stream().map(v3 -> new ModelEntity(treeModel, v3, 5)).collect(Collectors.toList()));

        modelRenderer.put(fernModel,
                Objects.generatePoints(
                        new Vector3f(-600, -5, -600),
                        new Vector3f(600, -5, 600),
                        600).stream().map(v3 -> new ModelEntity(fernModel, v3, 1)).collect(Collectors.toList()));
        modelRenderer.put(grassModel,
                Objects.generatePoints(
                        new Vector3f(-600, -5, -600),
                        new Vector3f(600, -5, 600),
                        600).stream().map(v3 -> new ModelEntity(grassModel, v3, 1)).collect(Collectors.toList()));

        modelRenderer.put(dragonModel, dragonEntity);
        terrainRenderer.put(terrainModel, ter);

        loop
                .update(() -> dragonEntity.increaseRotY(1))
                .loop();

        windowManager.close();
        context.close();
    }
}
