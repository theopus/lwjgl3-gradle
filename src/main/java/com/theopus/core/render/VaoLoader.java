package com.theopus.core.render;

import com.theopus.core.models.RawModel;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.Closeable;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class VaoLoader implements Closeable {

    public static final class Attribute {

        public static final int VERTICIES = 0;
        public static final int INDICIES = 1;
        public static final int TEXTURE_COORDS = 2;
    }

    private List<Integer> vaoIds = new ArrayList<>();
    private List<Integer> vboIds = new ArrayList<>();

    public RawModel loadToModel(float[] pos, int vecSize) {
        int vaoId = createVao();
        bindVao(vaoId);

        storeArrayBuffer(Attribute.VERTICIES, vecSize, pos);

        unbindVao();
        return new RawModel(vaoId, pos.length / vecSize);
    }

    private void storeArrayBuffer(int pos, int vecSize, float[] data) {
        int bufferId = createBuffer();
        bindArrayBuffer(bufferId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, toFloatBuffer(data), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(pos, vecSize, GL20.GL_FLOAT_VEC3, false, 0, 0);
        unbindArrayBuffer();
    }

    private FloatBuffer toFloatBuffer(float[] array) {
        FloatBuffer allocate = FloatBuffer.allocate(array.length);
        allocate.put(array);
        allocate.flip();
        return allocate;
    }

    private void bindArrayBuffer(int id) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
    }

    private void unbindArrayBuffer() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void bindVao(int id) {
        GL30.glBindVertexArray(id);
    }

    private void unbindVao() {
        GL30.glBindVertexArray(0);
    }

    private int createBuffer() {
        int bufferId = GL15.glGenBuffers();
        vboIds.add(bufferId);
        return bufferId;
    }

    private int createVao() {
        int id = GL30.glGenVertexArrays();
        vaoIds.add(id);
        return id;
    }

    public void close() {
        unbindVao();
        for (Integer vaoId : vaoIds) {
            GL30.glDeleteVertexArrays(vaoId);
        }
        unbindArrayBuffer();
        for (Integer vboId : vboIds) {
            GL15.glDeleteBuffers(vboId);
        }
    }

}
