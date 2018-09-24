package com.theopus.core.modules.configs;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class WindowConfig {

    private int width;
    private int height;
    private Vector4f color;
    private boolean primitivesCompatible;

    public WindowConfig(int width, int height, Vector4f color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }
    public WindowConfig(int width, int height, Vector4f color, boolean primitivesCompatible) {
        this(width, height, color);
        this.primitivesCompatible = primitivesCompatible;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public boolean isPrimitivesCompatible() {
        return primitivesCompatible;
    }
}
