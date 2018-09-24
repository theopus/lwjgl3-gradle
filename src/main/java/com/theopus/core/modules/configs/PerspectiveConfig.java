package com.theopus.core.modules.configs;

public class PerspectiveConfig {

    private float fov;
    private float near;
    private float far;

    public PerspectiveConfig(float fov, float near, float far) {
        this.fov = fov;
        this.near = near;
        this.far = far;
    }

    public float getFov() {
        return fov;
    }

    public float getNear() {
        return near;
    }

    public float getFar() {
        return far;
    }
}
