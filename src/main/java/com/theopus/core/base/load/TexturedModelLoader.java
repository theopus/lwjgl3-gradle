package com.theopus.core.base.load;

import com.theopus.core.base.objects.Texture;
import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.objects.TexturedVao;
import com.theopus.core.base.render.Attribute;
import com.theopus.core.memory.MemoryContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class TexturedModelLoader extends Loader {

    private static final Logger LOGGER = LoggerFactory.getLogger(TexturedModelLoader.class);

    public TexturedModelLoader(MemoryContext context) {
        super(context);
    }

    public TexturedModel loadSimpleMesh(float[] positions, int[] indices) {
        int vaoID = createVAO();
        bindVao(vaoID);
        int indicesVboId = bindIndicesBuffer(indices);

        int verticesVboId = writeInVao(Attribute.VERTICES, 3, positions);

        unbindVao();
        unbindIndicesBuffer();
        TexturedModel mesh = new TexturedModel(
                new TexturedVao(vaoID, indices.length, indicesVboId, verticesVboId, 0, 0),
                null);
        context.put(mesh);
        LOGGER.info("Loaded vao {}", vaoID);
        return mesh;
    }

    public TexturedModel loadTexturedMesh(float[] positions, int[] indices, float[] textureCoords, String textureFile) {
        return loadModelMesh(positions, indices, textureCoords, new float[]{0.0f}, textureFile);
    }

    public TexturedModel loadModelMesh(float[] positions, int[] indices, float[] textureCoords, float[] normals, String textureFile) {
        int vaoID = createVAO();
        bindVao(vaoID);
        int indicesVboId = bindIndicesBuffer(indices);

        int verticesVboId = writeInVao(Attribute.VERTICES, 3, positions);
        int texturesVboId = writeInVao(Attribute.TEXTURE_COORDS, 2, textureCoords);
        int normalsVboId = writeInVao(Attribute.NORMALS, 3, normals);

        unbindVao();
        unbindIndicesBuffer();

        Texture texture = loadTexture(textureFile);

        TexturedModel texturedMesh = new TexturedModel(
                new TexturedVao(vaoID,
                indices.length,
                indicesVboId,
                verticesVboId,
                texturesVboId,
                normalsVboId),
                texture);

        context.put(texturedMesh);
        LOGGER.info("Loaded textured mesh, vao {} tex {}", texturedMesh.getVao().getVaoId(), texturedMesh.getTexture().getTextureId());
        return texturedMesh;
    }
}
