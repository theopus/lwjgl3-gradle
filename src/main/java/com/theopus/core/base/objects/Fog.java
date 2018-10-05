package com.theopus.core.base.objects;

import org.joml.Vector3f;

public class Fog {

    private boolean enabled;
    private Vector3f color;
    private float density;
    private float gradient;

    public Fog(boolean enabled, Vector3f color, float density, float gradient) {
        this.enabled = enabled;
        this.color = color;
        this.density = density;
        this.gradient = gradient;
    }

    public Fog(Vector3f color) {
        this.enabled = true;
        this.color = color;
        this.density = 0.0020f;
        this.gradient = 1.5f;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Vector3f getColor() {
        return color;
    }

    public float getDensity() {
        return density;
    }

    public float getGradient() {
        return gradient;
    }
}
