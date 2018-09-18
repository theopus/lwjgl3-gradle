package com.theopus.core.render;

import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyListener implements GLFWKeyCallbackI {

    public static Logger LOGGER = LoggerFactory.getLogger(KeyListener.class);

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        LOGGER.info("Pressed window={}, key={}, scancode={}, action={}, mods={}", window, key, scancode, action, mods);
    }
}
