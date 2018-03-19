package com.theopus.core.render;

import com.theopus.core.models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.Closeable;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class VaoLoader implements Closeable {

    public static final class Attribute {

        public static final int VERTICIES = 0;
        public static final int TEXTURE_COORDS = 1;
    }

    private List<Integer> vao = new ArrayList<>();
    private List<Integer> vbo = new ArrayList<>();

    public RawModel loadToVAO(float[] positions, int[] indicies) {
        int vaoID = createVAO();
        bindVao(vaoID);
        bindIndicesBuffer(indicies);

        writeInVao(Attribute.VERTICIES, 3, positions);
        unbindVao();
        return new RawModel(vaoID, indicies.length);
    }

    private void writeInVao(int attributeNumber, int coordinatesSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbo.add(vboID);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinatesSize, GL11.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbo.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW );
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer result = BufferUtils.createIntBuffer(data.length);
        result.put(data);
        result.flip();
        return result;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer result = BufferUtils.createFloatBuffer(data.length);
        result.put(data);
        result.flip();
        return result;
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vao.add(vaoID);
        return vaoID;
    }

    private void bindVao(int id) {
        GL30.glBindVertexArray(id);
    }

    private void unbindVao() {
        GL30.glBindVertexArray(0);
    }

    @Override
    public void close() throws IOException {
        for (int vao : vao) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbo) {
            GL15.glDeleteBuffers(vbo);
        }
    }
}
