package com.theopus.core.model;

import com.theopus.core.base.objects.MaterialModel;
import com.theopus.core.base.objects.Model;
import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.window.InputHadler;
import com.theopus.core.base.window.InputHub;
import com.theopus.core.base.window.KeyListener;
import com.theopus.core.terrain.Terrain;
import org.joml.Vector3f;
import com.theopus.core.base.objects.Entity;
import org.lwjgl.glfw.GLFW;

public class ModelEntity extends Entity implements InputHadler, Movable {


    public String name;
    private Model<?> tModel;
    private Terrain terrain;

    public ModelEntity(Model<?> tModel) {
        this.tModel = tModel;
    }

    public ModelEntity(Model<?> tModel, Vector3f position) {
        super(position);
        this.tModel = tModel;
    }

    public ModelEntity(Model<?> tModel, Vector3f position, float scale) {
        super(position, scale);
        this.tModel = tModel;
    }

    public ModelEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale, Model<?> tModel) {
        super(position, rotX, rotY, rotZ, scale);
        this.tModel = tModel;
    }

    public Model<?> gettModel() {
        return tModel;
    }

    private float moveSpeed = 1f;
    private float rotSpeed = 3f;
    private float currMoveSpeed = 0f;
    private float currRotSpeed = 0f;
    private float upspeed = 1f;
    private float curupspeed = 0;
    private boolean isInAir = false;
    private float LOWER_BOUND = 0;
    private float gravity = -0.05f;


    @Override
    public void handle(InputHub inputHub) {
        if (inputHub.isKeyPressed(GLFW.GLFW_KEY_W)){
            currMoveSpeed = moveSpeed;
        } else
        if (inputHub.isKeyPressed(GLFW.GLFW_KEY_S)){
            currMoveSpeed = -moveSpeed;
        } else {
            currMoveSpeed = 0;
        }
        if (inputHub.isKeyPressed(GLFW.GLFW_KEY_D)){
            currRotSpeed = -rotSpeed;
        } else
        if (inputHub.isKeyPressed(GLFW.GLFW_KEY_A)){
            currRotSpeed = rotSpeed;
        } else {
            currRotSpeed = 0f;
        }
        if (inputHub.isKeyPressed(GLFW.GLFW_KEY_SPACE)){
            if (!isInAir){
                curupspeed = upspeed;
                isInAir = true;
            }
        }
    }

    public ModelEntity onTerrain(Terrain terrain){
        this.terrain = terrain;
        return this;
    }

    @Override
    public void move() {
        this.increaseRotY(currRotSpeed);
        float distance = currMoveSpeed;

        float dx = (float) (distance * Math.sin(Math.toRadians(this.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(this.getRotY())));
        this.increasePosY(curupspeed);
        this.increasePosX(dx);
        this.increasePosZ(dz);

        float height = terrain.getHeightAt(this.getPosition().x, this.getPosition().z);
//        float height = LOWER_BOUND;
        if (this.getPosition().y < height){
            isInAir = false;
            this.getPosition().y = height;
        } else {
            curupspeed+=gravity;
        }


    }
}
