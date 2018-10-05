package com.theopus.core.base.window;

import com.theopus.core.modules.configs.WindowConfig;
import org.joml.Vector4f;
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
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowManager implements Closeable {

    public static Logger LOGGER = LoggerFactory.getLogger(WindowManager.class);

    private GLFWKeyCallbackI listener;
    private Vector4f color;
    private boolean primitiveCompatible;
    public int width;
    public int height;
    public long window;
    private int vSync;

    public WindowManager(WindowConfig windowConfig, GLFWKeyCallbackI listener) {
        this.width = windowConfig.getWidth();
        this.height = windowConfig.getHeight();
        this.listener = listener;
        this.color = windowConfig.getColor();
        this.vSync = windowConfig.getvSync();
        this.primitiveCompatible = windowConfig.isPrimitivesCompatible();
    }

    public WindowManager(int width, int height) {
        this.width = width;
        this.height = height;
        this.color = new Vector4f(0, 0, 0, 1);
    }


    public void createWindow() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

        if (!primitiveCompatible) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        }

        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        window = glfwCreateWindow(width, height, "Kurs", NULL, NULL);

        if (listener != null) {
            glfwSetKeyCallback(window, listener);
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            LOGGER.info("Vidmode witdh: [{}], height: [{}]", vidmode.width(), vidmode.height());
            LOGGER.info("Window width: [{}], height: [{}]", pWidth.get(0), pHeight.get(0));

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
//        GL11.glViewport(0,0, width, height);
        glfwSwapInterval(vSync);
        GL11.glClearColor(color.x, color.y, color.z, color.w);
        LOGGER.info("Finished init of GLFW.");
    }

    public void update() {
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    public void showWindow() {
        glfwShowWindow(window);
    }

    public void setClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public void close() {
        glfwTerminate();
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(window);
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
