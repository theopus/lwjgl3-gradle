package com.theopus.core.render;

import com.theopus.core.models.RawModel;
import com.theopus.core.models.Texture;
import com.theopus.core.utils.LoaderUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class Loader implements Closeable {

    public static Logger LOGGER = LoggerFactory.getLogger(Loader.class);

    private List<Integer> vao = new ArrayList<>();
    private List<Integer> vbo = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel loadRawModel(float[] positions, int[] indices) {
        int vaoID = createVAO();
        bindVao(vaoID);
        bindIndicesBuffer(indices);

        writeInVao(Attribute.VERTICES, 3, positions);

        unbindVao();
        unbindIndicesBuffer();
        return new RawModel(vaoID, indices.length);
    }

    private Texture loadTexture(String path) {
        int width = 0;
        int height = 0;

        int[] pixels = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int[] data = LoaderUtils.argbToRgba(pixels, width,height);

        int textureId = GL11.glGenTextures();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buffer = stack.mallocInt(data.length).put(data);
            buffer.flip();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return new Texture(textureId, width, height);
    }

    private void writeInVao(Attribute attributeNumber, int coordinatesSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbo.add(vboID);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(data.length).put(data);
            buffer.flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        }
        
        /*
         stride - offset between values in array,
         normalized - normalize floating point numbers
         pointer
         */
        GL20.glVertexAttribPointer(attributeNumber.getPosition(), coordinatesSize, GL11.GL_FLOAT, false, 0, 0);


        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

    }

    //This is same VBO as top one, but GL_ELEMENT_ARRAY_BUFFER indicates the buffer contains indices of each element in the others VBO
    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbo.add(vboID);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buffer = stack.mallocInt(indices.length).put(indices);
            buffer.flip();
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        }
    }

    private void unbindIndicesBuffer() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
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
        for (int texture: textures){
            GL11.glDeleteTextures(texture);
        }
    }
}
