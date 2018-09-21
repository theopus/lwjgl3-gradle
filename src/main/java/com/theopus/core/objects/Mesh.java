package com.theopus.core.objects;

import com.theopus.core.render.Attribute;
import com.theopus.core.render.Bindable;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class Mesh implements Bindable, Resource {

    private int vaoId;

    private int vertices;

    private final int verticesVboId;
    private final int indicesVboId;
    private final int normalsVboId;

    Mesh(int vaoId, int verticesVboId, int vertices, int indicesVboId, int normalsVboId) {
        this.vaoId = vaoId;
        this.normalsVboId = normalsVboId;
        this.vertices = vertices;
        this.verticesVboId = verticesVboId;
        this.indicesVboId = indicesVboId;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertices() {
        return vertices;
    }

    @Override
    public void bind() {
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(Attribute.VERTICES.getPosition());
        GL20.glEnableVertexAttribArray(Attribute.NORMALS.getPosition());
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(Attribute.VERTICES.getPosition());
        GL20.glDisableVertexAttribArray(Attribute.NORMALS.getPosition());
        GL30.glBindVertexArray(0);
    }

    @Override
    public void cleanup() {
        GL30.glDeleteVertexArrays(vaoId);
        GL15.glDeleteBuffers(verticesVboId);
        GL15.glDeleteBuffers(indicesVboId);
        GL15.glDeleteBuffers(normalsVboId);

    }
}
