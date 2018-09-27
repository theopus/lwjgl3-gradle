package com.theopus.core.base.objects;

public class TexturedVao extends Vao{

    private int textureVboId;
    private int normalsVboId;

    public TexturedVao(int vaoId, int vertexCount, int indicesVboId, int verticesVboId, int textureVboId, int normalsVboId) {
        super(vaoId, vertexCount, indicesVboId, verticesVboId);
        this.textureVboId = textureVboId;
        this.normalsVboId = normalsVboId;
    }

    public int getTextureVboId() {
        return textureVboId;
    }

    public int getNormalsVboId() {
        return normalsVboId;
    }
}
