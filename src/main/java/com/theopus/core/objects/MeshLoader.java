package com.theopus.core.objects;

import com.theopus.core.render.Attribute;
import com.theopus.core.render.MemoryContext;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class MeshLoader {

    public static Logger LOGGER = LoggerFactory.getLogger(MeshLoader.class);

    private final MemoryContext ctx;

    public MeshLoader(MemoryContext ctx) {
        this.ctx = ctx;
    }

    public Mesh loadSimpleMesh(float[] positions, int[] indices) {
        int vaoID = createVAO();
        bindVao(vaoID);
        int indicesVboId = bindIndicesBuffer(indices);

        int verticesVboId = writeInVao(Attribute.VERTICES, 3, positions);

        unbindVao();
        unbindIndicesBuffer();
        Mesh mesh = new Mesh(vaoID, verticesVboId, indices.length, indicesVboId, 0);
        ctx.put(mesh);
        return mesh;
    }

    public TexturedMesh loadTexturedMesh(float[] positions, int[] indices, int[] textureCoords, String textureFile){
        int vaoID = createVAO();
        bindVao(vaoID);
        int indicesVboId = bindIndicesBuffer(indices);

        int verticesVboId = writeInVao(Attribute.VERTICES, 3, positions);
        int texturesVboId = writeInVao(Attribute.TEXTURE_COORDS, 3, positions);

        unbindVao();
        unbindIndicesBuffer();

        Texture texture = loadTexture(textureFile);

        TexturedMesh texturedMesh = new TexturedMesh(vaoID,
                verticesVboId, indices.length,
                indicesVboId, 0,
                texturesVboId, texture);
        ctx.put(texturedMesh);
        return texturedMesh;
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

        int[] data = LoaderUtils.argbToRgba(pixels, width, height);

        int textureId = GL11.glGenTextures();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buffer = stack.mallocInt(data.length).put(data);
            buffer.flip();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            buffer.clear();
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return new Texture(textureId, width, height);
    }

    private int writeInVao(Attribute attributeNumber, int coordinatesSize, float[] data) {
        int vboID = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(data.length).put(data);
            buffer.flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
            buffer.clear();
        }

        /*
         stride - offset between values in array,
         normalized - normalize floating point numbers
         pointer
         */
        GL20.glVertexAttribPointer(attributeNumber.getPosition(), coordinatesSize, GL11.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return vboID;
    }

    //This is same VBO as top one, but GL_ELEMENT_ARRAY_BUFFER indicates the buffer contains indices of each element in the others VBO
    //VAO contains specific slot for this kind of element buffer object, so it is actually EBO
    private int bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buffer = stack.mallocInt(indices.length).put(indices);
            buffer.flip();
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
            buffer.clear();
        }

        return vboID;
    }

    private void unbindIndicesBuffer() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        return vaoID;
    }

    private void bindVao(int id) {
        GL30.glBindVertexArray(id);
    }

    private void unbindVao() {
        GL30.glBindVertexArray(0);
    }

}
