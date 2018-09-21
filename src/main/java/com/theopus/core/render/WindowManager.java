package com.theopus.core.render;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowManager implements Closeable {

    public static Logger LOGGER = LoggerFactory.getLogger(WindowManager.class);

    private GLFWKeyCallbackI listener;
    public final int width;
    public final int height;
    public long window;

    public WindowManager(int width, int height, GLFWKeyCallbackI listener) {
        this.width = width;
        this.height = height;
        this.listener = listener;
    }
    public WindowManager(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void createWindow(){
        createWindow(true);
    }

    public void createWindow(boolean showWindow){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        window = glfwCreateWindow(width, height,"Kurs",NULL,NULL);

        if (listener != null) {
            glfwSetKeyCallback(window, listener);
        }

        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glViewport(0,0, width, height);

        if (showWindow) {
            glfwShowWindow(window);
        }

        GL11.glClearColor(1, 0, 0, 1);
        LOGGER.info("Finished init of GLFW.");
    }

    public void update(){
        glfwPollEvents();
        glfwSwapInterval(1);
        glfwSwapBuffers(window);
    }

    public void setClearColor(float r, float g, float b, float a){
        GL11.glClearColor(r, g, b, a);
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
