package com.theopus.core.model;

import com.theopus.core.base.objects.MaterialModel;
import com.theopus.core.base.objects.Model;
import com.theopus.core.base.objects.TexturedModel;
import org.joml.Vector3f;
import com.theopus.core.base.objects.Entity;

public class ModelEntity extends Entity {


    public String name;
    private Model<?> tModel;

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
}
