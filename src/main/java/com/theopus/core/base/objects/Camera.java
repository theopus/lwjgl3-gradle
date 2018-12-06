package com.theopus.core.base.objects;

import com.theopus.core.base.window.InputHadler;
import com.theopus.core.base.window.InputHub;
import com.theopus.core.model.Movable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class  Camera extends Entity implements InputHadler, Movable {

    private Entity entity;

    public Camera() {
    }

    public Camera(Entity entity) {
        this.entity = entity;
    }

    public Matrix4f viewMatrix() {
        Vector3f position = this.getPosition();
        Vector3f newpos = new Vector3f(-position.x, -position.y, -position.z);
        return transformationMatrix()
                .m30(0f)
                .m31(0f)
                .m32(0f)
                .translate(newpos);
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    private float distance = 50f;
    private float angleAround = 0;
    private float yOffset = 10f;

    @Override
    public void move() {
        calculateCameraPos(calculateHorizontal(), calculateVertical());
        this.setRotY(180 - (entity.getRotY() + angleAround));
    }

    private float calculateHorizontal() {
        return (float) (distance * Math.cos(Math.toRadians(this.getRotX())));
    }

    private float calculateVertical() {
        return (float) (distance * Math.sin(Math.toRadians(this.getRotX())));
    }

    private void calculateCameraPos(float horiz, float vert) {
        Vector3f position = entity.getPosition();
        float theta = entity.getRotY() + angleAround;
        float offsetX = (float) (horiz * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horiz * Math.cos(Math.toRadians(theta)));


        this.getPosition().x = position.x - offsetX;
        this.getPosition().y = position.y + vert + yOffset;
        this.getPosition().z = position.z - offsetZ;

    }

    @Override
    public void handle(InputHub hub) {
        if (hub.isMouseKeyPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            increaseRotX((float) (hub.yAxisDelta() * 0.4));
        }
        if (hub.isMouseKeyPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            angleAround -= hub.xAxisDelta() * 0.4f;
        }
        if (hub.isKeyPressed(GLFW.GLFW_KEY_P)) {
            distance += 1f;
        }
        if (hub.isKeyPressed(GLFW.GLFW_KEY_O)) {
            distance += -1f;
        }
    }
}
