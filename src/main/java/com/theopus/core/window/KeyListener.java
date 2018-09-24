package com.theopus.core.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class KeyListener implements GLFWKeyCallbackI {
    private static Logger LOGGER = LoggerFactory.getLogger(KeyListener.class);

    private Map<Integer, Boolean> keyMap;

    public KeyListener() {
        this.keyMap = new HashMap<>();
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        LOGGER.info("Pressed window={}, key={}, scancode={}, action={}, mods={}", window, key, scancode, action, mods);
        keyMap.put(key, action != GLFW.GLFW_RELEASE);
    }

    public boolean isKeyPressed(int key){
        return keyMap.getOrDefault(key, false);
    }


}
