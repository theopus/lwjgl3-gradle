package com.theopus.core;

import com.theopus.core.models.RawModel;
import com.theopus.core.render.Renderer;
import com.theopus.core.render.VaoLoader;
import com.theopus.core.render.WindowManager;

public class App {

    private WindowManager windowManager;

    public App(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public void run() throws InterruptedException {

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,

                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };

        VaoLoader loader = new VaoLoader();
        Renderer renderer = new Renderer();
        RawModel rawModel = loader.loadToModel(vertices, 3);
        windowManager.createWindow();
        while (!windowManager.windowShouldClose()) {
            renderer.prepare();

            renderer.render(rawModel);
            windowManager.update();
            Thread.sleep(100);
        }

        windowManager.close();
        loader.close();
    }
}
