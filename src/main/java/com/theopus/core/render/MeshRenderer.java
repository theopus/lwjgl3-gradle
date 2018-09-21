package com.theopus.core.render;

import com.theopus.core.objects.Mesh;
import com.theopus.core.shaders.ShaderProgram;
import com.theopus.core.shaders.StaticShader;
import org.lwjgl.opengl.GL11;

public class MeshRenderer extends Renderer<Mesh, StaticShader> {

    private ShaderProgram shader;

    public MeshRenderer(StaticShader shader) {
        super(shader);
    }

    @Override
    void render(Mesh mesh) {
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertices(), GL11.GL_UNSIGNED_INT, 0);
    }
}
