package com.theopus.core.terrain;

import com.theopus.core.base.objects.Entity;
import com.theopus.core.base.objects.TexturedModel;
import org.joml.Vector3f;

public class Terrain extends Entity {

    private TexturedModel texturedModel;

    public Terrain(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public Terrain(Vector3f position, float rotX, float rotY, float rotZ, float scale, TexturedModel texturedModel) {
        super(position, rotX, rotY, rotZ, scale);
        this.texturedModel = texturedModel;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }
}