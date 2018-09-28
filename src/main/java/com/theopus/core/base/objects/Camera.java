package com.theopus.core.base.objects;

import com.theopus.core.base.objects.Entity;
import com.theopus.core.window.KeyListener;
import com.theopus.core.base.Updatable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera extends Entity implements Updatable {

    private KeyListener keyListener;

    public Camera(KeyListener keyListener) {
        this.keyListener = keyListener;
    }

    public Camera(Vector3f position, float rotX, float rotY, float rotZ, float scale, KeyListener keyListener) {
        super(position, rotX, rotY, rotZ, scale);
        this.keyListener = keyListener;
    }

    @Override
    public void update() {
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_W)) {
            this.increasePosZ(-0.1f);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_S)) {
            this.increasePosZ(0.1f);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_A)) {
            this.increasePosX(-0.1f);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
            this.increasePosX(0.1f);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_Q)) {
            this.increaseRotY(-3);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_E)) {
            this.increaseRotY(3);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            this.increasePosY(0.1f);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_C)) {
            this.increasePosY(-0.1f);
        }
    }

    public Matrix4f viewMatrix() {
        Matrix4f viewMatrix = transformationMatrix();
        Vector3f translation = viewMatrix.getTranslation(new Vector3f());
        viewMatrix.setTranslation(translation.negate());
        return viewMatrix;
    }
}