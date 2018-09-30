package com.theopus.core.base.objects;

import com.theopus.core.base.window.InputHadler;
import com.theopus.core.base.window.KeyListener;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera extends Entity implements InputHadler {

    public Camera() {
    }

    public Camera(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(position, rotX, rotY, rotZ, scale);
    }

    public Matrix4f viewMatrix() {
        Matrix4f viewMatrix = transformationMatrix();
        Vector3f translation = viewMatrix.getTranslation(new Vector3f());
        viewMatrix.setTranslation(translation.negate());
        return viewMatrix;
    }

    @Override
    public void handle(KeyListener keyListener) {
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
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_R)) {
            this.increaseRotZ(-3);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_F)) {
            this.increaseRotZ(3);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            this.increasePosY(0.1f);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_C)) {
            this.increasePosY(-0.1f);
        }
        if (keyListener.isKeyPressed(GLFW.GLFW_KEY_Y)) {
            this.setPosition(new Vector3f(0,0,0));
            this.setRotX(0);
            this.setRotY(0);
            this.setRotZ(0);
        }
    }
}
