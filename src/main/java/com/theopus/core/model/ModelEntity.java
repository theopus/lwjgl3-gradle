package com.theopus.core.model;

import com.theopus.core.base.objects.TexturedModel;
import org.joml.Vector3f;
import com.theopus.core.base.objects.Entity;

public class ModelEntity extends Entity {


    public String name;
    private TexturedModel tModel;

    public ModelEntity(TexturedModel tModel) {
        this.tModel = tModel;
    }

    public ModelEntity(Vector3f position, float rotX, float rotY, float rotZ, float scale, TexturedModel tModel) {
        super(position, rotX, rotY, rotZ, scale);
        this.tModel = tModel;
    }

    public TexturedModel gettModel() {
        return tModel;
    }
}
