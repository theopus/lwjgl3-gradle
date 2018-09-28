package com.theopus.core.base.load;

import com.theopus.core.base.objects.Texture;
import com.theopus.core.base.render.Attribute;
import com.theopus.core.memory.MemoryContext;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Loader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);

    protected MemoryContext context;

    public Loader(MemoryContext context) {
        this.context = context;
    }

    protected int writeInVao(Attribute attributeNumber, int coordinatesSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        LOGGER.info("Created vbo:{}", vboID);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        buffer.clear();

        MemoryUtil.memFree(buffer);
        /*
         stride - offset between values in array,
         normalized - normalize floating point numbers
         pointer
         */
        GL20.glVertexAttribPointer(attributeNumber.getPosition(), coordinatesSize, GL11.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        LOGGER.info("Write vertex buffer in vao. vbo:{}, attr:{}, size:{}, dataLength:{}", vboID, attributeNumber, coordinatesSize, data.length);
        return vboID;
    }

    //This is same VBO as top one, but GL_ELEMENT_ARRAY_BUFFER indicates the buffer contains indices of each element in the others VBO
    //VAO contains specific slot for this kind of element buffer object, so it is actually EBO
    protected int bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

        IntBuffer intBuffer = MemoryUtil.memAllocInt(indices.length);
        intBuffer.put(indices);
        intBuffer.flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL15.GL_STATIC_DRAW);
        intBuffer.clear();
        MemoryUtil.memFree(intBuffer);
        LOGGER.info("Write indices buffer in vao. vbo:{}, dataLength:{}", vboID, indices.length);
        return vboID;
    }

    protected void unbindIndicesBuffer() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    protected int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        LOGGER.info("Created vao id:{}", vaoID);
        return vaoID;
    }

    protected void bindVao(int id) {
        GL30.glBindVertexArray(id);
        LOGGER.info("Bind vao id:{}", id);
    }

    protected void unbindVao() {
        GL30.glBindVertexArray(0);
        LOGGER.info("Unbind vao");
    }

    protected Texture loadTexture(String path) {

        try (InputStream resourceAsStream = Loader.class.getClassLoader().getResourceAsStream(path);){
            PNGDecoder decoder = new PNGDecoder(resourceAsStream);
            int width = decoder.getWidth();
            int height = decoder.getHeight();

            int textureId = GL11.glGenTextures();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            ByteBuffer byteBuffer = MemoryUtil.memAlloc(4 * width * height);
            decoder.decode(byteBuffer, height * 4, PNGDecoder.Format.RGBA);
            byteBuffer.flip();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            byteBuffer.clear();
            MemoryUtil.memFree(byteBuffer);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            LOGGER.info("Loaded texture. id:{}, width:{}, height:{}, size:{}", textureId, width, height, byteBuffer.limit());
            return new Texture(textureId, width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}