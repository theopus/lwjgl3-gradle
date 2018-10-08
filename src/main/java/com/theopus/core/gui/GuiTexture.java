package com.theopus.core.gui;

import org.joml.Vector2f;

public class GuiTexture {

    private int id;
    private Vector2f position;
    private Vector2f scale;

    public GuiTexture(int id, Vector2f position, Vector2f scale) {
        this.id = id;
        this.position = position;
        this.scale = scale;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }
}
