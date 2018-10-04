package com.theopus.core.base.load;

import com.theopus.core.base.objects.*;
import com.theopus.core.base.render.Attribute;
import com.theopus.core.base.memory.MemoryContext;
import com.theopus.core.utils.ObjParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class MaterialModelLoader extends Loader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialModelLoader.class);

    public MaterialModelLoader(MemoryContext context) {
        super(context);
    }

    public MaterialModel loadSimpleMesh(float[] positions, int[] indices) {
        int vaoID = createVAO();
        bindVao(vaoID);
        int indicesVboId = bindIndicesBuffer(indices);

        int verticesVboId = writeInVao(Attribute.VERTICES, 3, positions);

        unbindVao();
        unbindIndicesBuffer();
        MaterialModel model = new MaterialModel(
                new TexturedVao(vaoID, indices.length, indicesVboId, verticesVboId, 0, 0),
                null);
        context.put(model);
        LOGGER.info("Loaded vao {}", vaoID);
        return model;
    }

    public MaterialModel loadTexturedMesh(float[] positions, int[] indices, float[] textureCoords, String textureFile) {
        return loadMaterialModel(positions, indices, textureCoords, new float[]{0.0f}, textureFile);
    }

    public MaterialModel loadMaterialModel(float[] positions, int[] indices, float[] textureCoords, float[] normals, String textureFile) {
        int vaoID = createVAO();
        bindVao(vaoID);
        int indicesVboId = bindIndicesBuffer(indices);

        int verticesVboId = writeInVao(Attribute.VERTICES, 3, positions);
        int texturesVboId = writeInVao(Attribute.TEXTURE_COORDS, 2, textureCoords);
        int normalsVboId = writeInVao(Attribute.NORMALS, 3, normals);

        unbindVao();
        unbindIndicesBuffer();

        Material material = new Material(loadTexture(textureFile));

        MaterialModel model = new MaterialModel(
                new TexturedVao(vaoID,
                        indices.length,
                        indicesVboId,
                        verticesVboId,
                        texturesVboId,
                        normalsVboId),
                material);

        context.put(model);
        LOGGER.info("Loaded textured mesh, vao {} tex {}", model.getVao().getVaoId(), material.getTexture().getTextureId());
        return model;
    }

    public TexturedModel loadTexturedModel(float[] positions, int[] indices, float[] textureCoords, float[] normals, String textureFile) {
        int vaoID = createVAO();
        bindVao(vaoID);
        int indicesVboId = bindIndicesBuffer(indices);

        int verticesVboId = writeInVao(Attribute.VERTICES, 3, positions);
        int texturesVboId = writeInVao(Attribute.TEXTURE_COORDS, 2, textureCoords);
        int normalsVboId = writeInVao(Attribute.NORMALS, 3, normals);

        unbindVao();
        unbindIndicesBuffer();

        Texture texture = loadTexture(textureFile);

        TexturedModel model = new TexturedModel(
                new TexturedVao(vaoID,
                        indices.length,
                        indicesVboId,
                        verticesVboId,
                        texturesVboId,
                        normalsVboId),
                texture);

        context.put(model);
        LOGGER.info("Loaded textured mesh, vao {} tex {}", model.getVao().getVaoId(), texture.getTextureId());
        return model;
    }

    public MemoryContext getMemoryContext() {
        return this.context;
    }

    public MaterialModel loadTexturedModel(String obj, String texture) throws IOException {
        ObjParser.Result parse = ObjParser.parse(obj);

        return loadMaterialModel(
                parse.getPosArr(),
                parse.getIndicesArr(),
                parse.getTextCoordArr(),
                parse.getNormArr(),
                texture);
    }
}
