package com.theopus.core.base.objects;

import org.joml.Vector3f;

public abstract class Point {

    private Vector3f position;

    public Point(Vector3f position) {
        this.position = position;
    }

    public Point() {
        this.position = new Vector3f();
    }

    public float increasePosX(float deltaPosX) {
        return position.x += deltaPosX;
    }

    public float increasePosY(float deltaPosY) {
        return position.y += deltaPosY;
    }

    public float increasePosZ(float deltaPosX) {
        return position.z += deltaPosX;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
