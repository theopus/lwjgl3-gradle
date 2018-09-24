package com.theopus.core.base.objects;

import com.theopus.core.utils.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class Entity extends Point {

    private float rotX;
    private float rotY;
    private float rotZ;
    private float scale;

    public Entity() {
        super();
        this.rotX = 0f;
        this.rotY = 0f;
        this.rotZ = 0f;
        this.scale = 1f;
    }

    public Entity(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(position);
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public Matrix4f transformationMatrix() {
        return Maths.createTransformationMatrix(getPosition(), getRotX(), getRotY(), getRotZ(), getScale());
    }


    public float increaseRotX(float deltaRotX) {
        return rotX += deltaRotX;
    }

    public float increaseRotY(float deltaRotY) {
        return rotY += deltaRotY;
    }

    public float increaseRotZ(float deltaRotZ) {
        return rotZ += deltaRotZ;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
