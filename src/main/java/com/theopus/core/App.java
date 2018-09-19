package com.theopus.core;

import com.theopus.core.models.RawModel;
import com.theopus.core.render.Renderer;
import com.theopus.core.render.Loader;
import com.theopus.core.render.RawModelRenderer;
import com.theopus.core.render.WindowManager;

import java.io.IOException;

public class App {

    private WindowManager windowManager;

    public App(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public void run() throws InterruptedException, IOException {

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
        Loader loader = new Loader();
        Renderer renderer = new RawModelRenderer();
        RawModel rawModel = loader.loadRawModel(vertices, indicies);




        while (!windowManager.windowShouldClose()) {
            renderer.prepare();

            renderer.renderCycle(rawModel);
            windowManager.update();
            Thread.sleep(100);
        }

        windowManager.close();
        loader.close();
    }
}
