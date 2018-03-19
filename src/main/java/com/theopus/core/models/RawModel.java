package com.theopus.core.models;

/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class RawModel {

    private int vaoId;
    private int verticies;

    public RawModel(int vaoId, int verticies) {
        this.vaoId = vaoId;
        this.verticies = verticies;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVerticies() {
        return verticies;
    }
}
