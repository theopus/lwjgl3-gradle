package com.theopus.core.render;

import org.lwjgl.opengl.GL;

import java.io.Closeable;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowManager implements Closeable {
    public final int width;
    public final int height;
    public long window;

    public WindowManager(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void createWindow(){
        if(!glfwInit()){
            System.err.println("Err with initialization of GLFW");
            return;
        }
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        window = glfwCreateWindow(width, height,"Kurs",NULL,NULL);

        glfwShowWindow(window);

//        glfwSetKeyCallback(window, new Input());
        glfwInit();

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glViewport(0,0, width, height);
    }

    public void update(){
        glfwPollEvents();
        glfwSwapInterval(1);
        glfwSwapBuffers(window);
    }

    public void close(){
        glfwTerminate();
    }

    public boolean windowShouldClose(){
        return glfwWindowShouldClose(window);
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
