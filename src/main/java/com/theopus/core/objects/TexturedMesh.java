package com.theopus.core.objects;

import com.theopus.core.render.Attribute;
import org.lwjgl.opengl.GL20;

public class TexturedMesh extends Mesh {

    private final int textureCoordsVboId;
    private final Texture texture;

    TexturedMesh(int vaoId, int verticesVboId, int vertices, int indicesVboId, int normalsVboId, int textureCoordsVboId, Texture texture) {
        super(vaoId, verticesVboId, vertices, indicesVboId, normalsVboId);
        this.textureCoordsVboId = textureCoordsVboId;
        this.texture = texture;
    }

    @Override
    public void bind() {
        super.bind();
        GL20.glEnableVertexAttribArray(Attribute.TEXTURE_COORDS.getPosition());
    }

    @Override
    public void unbind()
    {   texture.unbind();
        GL20.glEnableVertexAttribArray(0);
        super.unbind();
    }

    @Override
    public void cleanup() {
        texture.cleanup();
        super.cleanup();
    }
}
