package com.theopus.core;

import com.theopus.core.models.RawModel;
import com.theopus.core.render.Renderer;
import com.theopus.core.render.VaoLoader;
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
        windowManager.createWindow();
        VaoLoader loader = new VaoLoader();
        Renderer renderer = new Renderer();
        RawModel rawModel = loader.loadToVAO(vertices, indicies);




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
