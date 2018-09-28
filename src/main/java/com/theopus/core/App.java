package com.theopus.core;

import com.theopus.core.base.load.TexturedModelLoader;
import com.theopus.core.base.objects.Camera;
import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.render.ButchRenderer;
import com.theopus.core.base.render.Renderer;
import com.theopus.core.memory.MemoryContext;
import com.theopus.core.model.ModelEntity;
import com.theopus.core.utils.ObjParser;
import com.theopus.core.window.KeyListener;
import com.theopus.core.window.WindowManager;
import org.joml.Vector3f;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class App {

    private final WindowManager windowManager;
    private final Renderer renderer;
    private MemoryContext context;
    private Camera camera;
    private Loop loop;
    private KeyListener keyListener;

    public App(WindowManager windowManager, Renderer renderer, MemoryContext context, Camera camera, Loop loop, KeyListener keyListener) {
        this.windowManager = windowManager;
        this.renderer = renderer;
        this.context = context;
        this.camera = camera;
        this.loop = loop;
        this.keyListener = keyListener;
    }

    public void run() throws Exception {
        windowManager.showWindow();
        TexturedModelLoader texturedModelLoader = new TexturedModelLoader(context);


        ObjParser.Result parse = ObjParser.parse("dragon.obj");
        TexturedModel dragonVao = texturedModelLoader.loadModelMesh(
                parse.getPosArr(),
                parse.getIndicesArr(),
                parse.getTextCoordArr(),
                parse.getNormArr(),
                "whiteIm.png");

        dragonVao.getTexture().setReflictivity(1);
        dragonVao.getTexture().setShineDumper(10);

//
//        parse = ObjParser.parse("stall.obj");
//        MeshVao stallVao = texturedModelLoader.loadModelMesh(
//                parse.getPosArr(),
//                parse.getIndicesArr(),
//                parse.getTextCoordArr(),
//                parse.getNormArr(),
//                "whiteIm.png");

//        ThreadLocalRandom rand = ThreadLocalRandom.current();
//        float min = -200f;
//        float max = 200;
////
//        List<ModelEntity> collect = IntStream.range(0, 20).mapToObj(value -> {
//            ModelEntity m = new ModelEntity(dragonVao);
//            m.setScale(rand.nextFloat());
//            m.setPosition(new Vector3f(
//                    (float) ((Math.random()) * (max - min) + min),
//                    (float) ((Math.random()) * (max - min) + min),
//                    (float) ((Math.random()) * (max - min) + min)));
//            m.setRotX((float) ((Math.random()) * (max - min) + min));
//            m.setRotY((float) ((Math.random()) * (max - min) + min));
//            m.setRotX((float) ((Math.random()) * (max - min) + min));
//            return m;
//        }).collect(Collectors.toList());

        ModelEntity dragonEntity = new ModelEntity(dragonVao);
        dragonEntity.setScale(0.75f);
        dragonEntity.setPosition(new Vector3f(0, 0, -25f));

        ButchRenderer<TexturedModel, ModelEntity> butchRenderer = new ButchRenderer<TexturedModel, ModelEntity>(renderer);
        butchRenderer.put(dragonVao, dragonEntity);

        loop
                .input(() -> camera.handle(keyListener))
                .update(() -> dragonEntity.increaseRotY(1))
                .render(() -> {
                    butchRenderer.render();
                    windowManager.update();
                })
                .interruptOn(windowManager::windowShouldClose)
                .loop();

        windowManager.close();
        context.close();
    }
}
