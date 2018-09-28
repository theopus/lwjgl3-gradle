package com.theopus.core;

import com.theopus.core.base.load.TexturedModelLoader;
import com.theopus.core.base.objects.Camera;
import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.render.ButchRenderer;
import com.theopus.core.base.render.Renderer;
import com.theopus.core.memory.MemoryContext;
import com.theopus.core.model.ModelEntity;
import com.theopus.core.utils.ObjParser;
import com.theopus.core.window.WindowManager;
import org.joml.Vector3f;

public class App {

    private final WindowManager windowManager;
    private final Renderer renderer;
    private MemoryContext context;
    private Camera camera;

    public App(WindowManager windowManager, Renderer renderer, MemoryContext context, Camera camera) {
        this.windowManager = windowManager;
        this.renderer = renderer;
        this.context = context;
        this.camera = camera;
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
//        MeshVao cubeVao = Objects.geCube(texturedModelLoader);
//        cubeVao.setTexture(dragonVao.getTexture());
//        float min = -700f;
//        float max = 700;
//
//        List<ModelEntity> collect = IntStream.range(0, 5_000).mapToObj(value -> {
//            ModelEntity m = new ModelEntity(stallVao);
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


        int ups = 0;
        int fps = 0;


        long renderNanoPerFrame = (long) ((1d / 80) * 1000_000_000);
        long updNanoPerFrame = (long) ((1d / 60) * 1_000_000_000);

        System.out.println(renderNanoPerFrame);
        System.out.println(updNanoPerFrame);


        long before = System.nanoTime();
        long now;
        long elapsed;
        long frameEndTime;
        long steps = 0;

        long countStart = System.nanoTime();
        while (!windowManager.windowShouldClose()) {
            if((System.nanoTime() - countStart) > 1000_000_000){
                countStart = System.nanoTime();
                System.out.println("FPS = " + fps);
                System.out.println("UPS = " + ups);
                ups = 0;
                fps = 0;
            }
            now = System.nanoTime();
            elapsed = now - before;
            before = now;
            steps += elapsed;

            while (steps >= updNanoPerFrame){
                dragonEntity.increaseRotY(3);
                camera.update();
                steps -= updNanoPerFrame;
                ups++;
            }

            butchRenderer.render();
            windowManager.update();
            fps++;

            frameEndTime = now + renderNanoPerFrame;
            while (System.nanoTime() < frameEndTime){
                Thread.sleep(1);
            }

        }

        windowManager.close();
        context.close();
    }
}
