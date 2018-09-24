package com.theopus.core.mesh;

import org.joml.Vector3f;
import com.theopus.core.base.objects.Entity;

public class Mesh extends Entity {

    private MeshVao meshVao;

    public Mesh(MeshVao meshVao) {
        this.meshVao = meshVao;
    }

    public Mesh(Vector3f position, float rotX, float rotY, float rotZ, float scale, MeshVao meshVao) {
        super(position, rotX, rotY, rotZ, scale);
        this.meshVao = meshVao;
    }

    public MeshVao getMeshVao() {
        return meshVao;
    }
}
