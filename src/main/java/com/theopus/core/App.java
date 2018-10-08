package com.theopus.core;

import com.theopus.core.base.load.Loader;
import com.theopus.core.base.load.MaterialModelLoader;
import com.theopus.core.base.memory.MemoryContext;
import com.theopus.core.base.objects.Camera;
import com.theopus.core.base.objects.Light;
import com.theopus.core.base.objects.MaterialModel;
import com.theopus.core.base.objects.TexturePackModel;
import com.theopus.core.base.render.BatchRenderer;
import com.theopus.core.base.render.Renderer;
import com.theopus.core.base.shader.ShaderFactory;
import com.theopus.core.base.shader.ShaderProgram;
import com.theopus.core.base.shader.StaticShader;
import com.theopus.core.base.window.InputHub;
import com.theopus.core.base.window.WindowManager;
import com.theopus.core.gui.GuiRenderer;
import com.theopus.core.gui.GuiShader;
import com.theopus.core.gui.GuiTexture;
import com.theopus.core.model.ModelEntity;
import com.theopus.core.model.ModelRenderCommand;
import com.theopus.core.terrain.Terrain;
import com.theopus.core.terrain.TerrainLoader;
import com.theopus.core.terrain.TerrainRenderCommand;
import com.theopus.core.terrain.TerrainShader;
import com.theopus.core.utils.Objects;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    private WindowManager windowManager;
    private Matrix4f projectionMtx;
    private Loop loop;
    private InputHub hub;

    public App(WindowManager windowManager, Matrix4f projectionMtx, Loop loop, InputHub hub) {
        this.windowManager = windowManager;
        this.projectionMtx = projectionMtx;
        this.loop = loop;
        this.hub = hub;
    }

    public void run() throws Exception {
        windowManager.showWindow();
        // loaders, world objects
        Camera camera = new Camera();
        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
        MemoryContext context = new MemoryContext();
        Loader loader = new Loader(context);
        MaterialModelLoader materialModelLoader = new MaterialModelLoader(context);
        TerrainLoader terrainLoader = new TerrainLoader(context);

        //shaders
        StaticShader staticShader = ShaderFactory.createStaticShader("static.vert", "static.frag");
        context.put(staticShader);
        TerrainShader terrainShader = new TerrainShader(
                ShaderProgram.loadShader("terrain.vert", ShaderProgram.Type.VERTEX),
                ShaderProgram.loadShader("terrain.frag", ShaderProgram.Type.FRAGMENT)
        );
        context.put(terrainShader);

        //models load
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
        MaterialModel catModel = materialModelLoader.loadTexturedModel("cat.obj", "whiteIm.png");

        ModelEntity dragonEntity = new ModelEntity(dragonModel);
        dragonEntity.setScale(0.75f);
        dragonEntity.setPosition(new Vector3f(20, 5, 20));

        Terrain terrain = terrainLoader.loadTerrain(
                "blendMap.png",
                "mud.png",
                "grassy2.png",
                "grassFlowers.png",
                "path.png");


        terrain.setPosition(new Vector3f(-500, 0, -500));

        //renderers
        ModelRenderCommand modelRenderCommand = new ModelRenderCommand(staticShader, projectionMtx, camera, light);
        TerrainRenderCommand terrainRenderCommand = new TerrainRenderCommand(terrainShader, camera, light, projectionMtx);

        BatchRenderer<MaterialModel, ModelEntity> modelRender = Renderer.batchOf(modelRenderCommand);
        BatchRenderer<TexturePackModel, Terrain> terrainRender = Renderer.batchOf(terrainRenderCommand);

        //put to rederers
        modelRender.put(treeModel,
                Objects.generatePoints(
                        new Vector3f(-600, -5, -600),
                        new Vector3f(600, -5, 600),
                        600).stream().map(v3 -> new ModelEntity(treeModel, v3, 5))
                        .peek(modelEntity -> {
                            Vector3f position = modelEntity.getPosition();
                            modelEntity.setPosition(new Vector3f(position.x, terrain.getHeightAt(position.x, position.z), position.z));
                        })
                        .collect(Collectors.toList()));

        modelRender.put(fernModel,
                Objects.generatePoints(
                        new Vector3f(-600, -5, -600),
                        new Vector3f(600, -5, 600),
                        600).stream().map(v3 -> new ModelEntity(fernModel, v3, 1))
                        .peek(modelEntity -> {
                            Vector3f position = modelEntity.getPosition();
                            modelEntity.setPosition(new Vector3f(position.x, terrain.getHeightAt(position.x, position.z), position.z));
                        })
                        .collect(Collectors.toList()));

        //model preparing stuff
        ModelEntity cat = new ModelEntity(catModel);
        cat.setScale(8f);
        cat.setPosition(new Vector3f(0, -5f, 0));

        camera.setEntity(cat);
        camera.setPosition(new Vector3f(100, 0, -50));
        camera.setRotX(12);
        camera.setRotY(180);
        camera.setRotZ(0);

        modelRender.put(dragonModel, dragonEntity);
        modelRender.put(catModel, cat);
        terrainRender.put(terrain.getTexturedModel(), terrain);


        //gui
        List<GuiTexture> textureList = new ArrayList<>();
        GuiTexture soc = new GuiTexture(loader.loadTexture("java.png").getTextureId(), new Vector2f(-0.9f, -0.85f), new Vector2f(0.1f, 0.1f));
        textureList.add(soc);

        GuiShader shader = new GuiShader("gui.vert", "gui.frag");
        context.put(shader);
        GuiRenderer textureRender = new GuiRenderer(loader, shader);

        //loop
        loop
                .input(() -> {
                    cat.handle(hub);
                    camera.handle(hub);
                    hub.resetCursorDelta();
                })
                .update(() -> {
                    dragonEntity.increaseRotY(1);
                    cat.onTerrain(terrain);
                    cat.move();
                    camera.move();
                })
                .render(() -> {
                    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                    modelRender.render();
                    terrainRender.render();
                    textureRender.render(textureList);
                    windowManager.update();
                })
                .loop();
        windowManager.close();
        context.close();
    }
}
