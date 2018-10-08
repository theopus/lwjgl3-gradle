package com.theopus.core.base.objects;

import com.theopus.core.base.memory.Resource;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.util.Objects;

public class Vao implements Resource {

    private int vaoId;
    private int vertexCount;
    private int indicesVboId;
    private int verticesVboId;

    public Vao(int vaoId, int vertexCount, int indicesVboId, int verticesVboId) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
        this.indicesVboId = indicesVboId;
        this.verticesVboId = verticesVboId;
    }

    public Vao(int vaoId, int vertexCount) {
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getIndicesVboId() {
        return indicesVboId;
    }

    public int getVerticesVboId() {
        return verticesVboId;
    }

    @Override
    public void cleanup() {
        GL15.glDeleteBuffers(indicesVboId);
        GL15.glDeleteBuffers(verticesVboId);
        GL30.glDeleteVertexArrays(vaoId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vao vao = (Vao) o;
        return getVaoId() == vao.getVaoId() &&
                getVerticesVboId() == vao.getVerticesVboId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVaoId(), getVerticesVboId());
    }
}
