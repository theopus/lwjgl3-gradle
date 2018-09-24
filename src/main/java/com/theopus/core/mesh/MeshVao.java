package com.theopus.core.mesh;

import com.theopus.core.base.objects.Texture;
import com.theopus.core.base.objects.Vao;
import org.lwjgl.opengl.GL15;

/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class MeshVao extends Vao {

    private int textureVboId;
    private int normalsVboId;
    private Texture texture;

    protected MeshVao(int vaoId, int vertexCount, int indicesVboId, int verticesVboId, int textureVboId, int normalsVboId, Texture texture) {
        super(vaoId, vertexCount, indicesVboId, verticesVboId);
        this.textureVboId = textureVboId;
        this.normalsVboId = normalsVboId;
        this.texture = texture;
    }

    public int getTextureVboId() {
        return textureVboId;
    }

    public int getNormalsVboId() {
        return normalsVboId;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }



    @Override
    public void cleanup() {
        GL15.glDeleteBuffers(textureVboId);
        GL15.glDeleteBuffers(normalsVboId);
        super.cleanup();
    }

}
