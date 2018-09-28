package com.theopus.core.modules.configs;

import org.joml.Vector4f;

public class WindowConfig {

    private int width;
    private int height;
    private Vector4f color;
    private boolean primitivesCompatible;
    private int fpsCap;

    public WindowConfig(int width, int height, Vector4f color, int fpsCap) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.fpsCap = fpsCap;
    }
    public WindowConfig(int width, int height, Vector4f color, boolean primitivesCompatible, int fpsCap) {
        this(width, height, color, fpsCap);
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

    public int getFpsCap() {
        return fpsCap;
    }

    public void setFpsCap(int fpsCap) {
        this.fpsCap = fpsCap;
    }
}
