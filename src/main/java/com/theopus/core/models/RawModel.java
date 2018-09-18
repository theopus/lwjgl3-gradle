package com.theopus.core.models;

/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class RawModel {

    private int vaoId;
    private int vertices;

    public RawModel(int vaoId, int vertices) {
        this.vaoId = vaoId;
        this.vertices = vertices;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertices() {
        return vertices;
    }
}
