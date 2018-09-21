package com.theopus.core;

import com.theopus.core.objects.Mesh;
import com.theopus.core.render.MemoryContext;
import com.theopus.core.render.Renderer;
import com.theopus.core.objects.MeshLoader;
import com.theopus.core.render.WindowManager;

import java.io.IOException;

public class App {

    private final WindowManager windowManager;
    private final Renderer renderer;
    private MemoryContext context;

    public App(WindowManager windowManager, Renderer renderer, MemoryContext context) {
        this.windowManager = windowManager;
        this.renderer = renderer;
        this.context = context;
    }

    public void run() throws Exception {

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
        };

        int[] indicies = {
                0,1,3,
                3,1,2
        };
        MeshLoader meshLoader = new MeshLoader(context);

        Mesh mesh = meshLoader.loadSimpleMesh(vertices, indicies);


        while (!windowManager.windowShouldClose()) {
            renderer.renderCycle(mesh);
            windowManager.update();

            Thread.sleep(100);
        }

        windowManager.close();
        context.close();
    }
}
