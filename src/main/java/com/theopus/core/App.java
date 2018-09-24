package com.theopus.core;

import com.theopus.core.base.objects.Camera;
import com.theopus.core.base.render.ButchRenderer;
import com.theopus.core.base.render.Renderer;
import com.theopus.core.memory.MemoryContext;
import com.theopus.core.mesh.Mesh;
import com.theopus.core.mesh.MeshLoader;
import com.theopus.core.mesh.MeshVao;
import com.theopus.core.utils.ObjParser;
import com.theopus.core.utils.Objects;
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

    public App(WindowManager windowManager, Renderer renderer, MemoryContext context, Camera camera) {
        this.windowManager = windowManager;
        this.renderer = renderer;
        this.context = context;
        this.camera = camera;
    }

    public void run() throws Exception {
        windowManager.showWindow();
        MeshLoader meshLoader = new MeshLoader(context);


        ObjParser.Result parse = ObjParser.parse("dragon.obj");
        MeshVao dragonVao = meshLoader.loadModelMesh(
                parse.getPosArr(),
                parse.getIndicesArr(),
                parse.getTextCoordArr(),
                parse.getNormArr(),
                "whiteIm.png");

        dragonVao.getTexture().setReflictivity(1);
        dragonVao.getTexture().setShineDumper(10);

//
//        parse = ObjParser.parse("stall.obj");
//        MeshVao stallVao = meshLoader.loadModelMesh(
//                parse.getPosArr(),
//                parse.getIndicesArr(),
//                parse.getTextCoordArr(),
//                parse.getNormArr(),
//                "whiteIm.png");

//        ThreadLocalRandom rand = ThreadLocalRandom.current();
//        MeshVao cubeVao = Objects.geCube(meshLoader);
//        cubeVao.setTexture(dragonVao.getTexture());
//        float min = -700f;
//        float max = 700;
//
//        List<Mesh> collect = IntStream.range(0, 5_000).mapToObj(value -> {
//            Mesh m = new Mesh(stallVao);
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

        Mesh dragonEntity = new Mesh(dragonVao);
        dragonEntity.setScale(0.75f);
        dragonEntity.setPosition(new Vector3f(0, 0, -25f));


        ButchRenderer<Mesh> butchRenderer = new ButchRenderer<Mesh>(renderer);
        butchRenderer.put(dragonVao, dragonEntity);

        while (!windowManager.windowShouldClose()) {
            dragonEntity.increaseRotY(1f);

            camera.update();
            butchRenderer.render();

            windowManager.update();
            Thread.sleep(1);
        }

        windowManager.close();
        context.close();
    }
}
