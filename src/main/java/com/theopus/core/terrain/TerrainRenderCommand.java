package com.theopus.core.terrain;

import com.theopus.core.base.objects.Camera;
import com.theopus.core.base.objects.Light;
import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.render.Attribute;
import com.theopus.core.base.render.RenderCommand;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class TerrainRenderCommand implements RenderCommand<TexturedModel, Terrain> {

    private TerrainShader shader;
    private Camera camera;
    private Light light;

    public TerrainRenderCommand(TerrainShader shader, Camera camera, Light light, Matrix4f pjMtx) {
        this.shader = shader;
        this.camera = camera;
        this.light = light;

        shader.bind();
        shader.loadProjectionMatrix(pjMtx);
        shader.unbind();
    }

    @Override
    public RenderCommand<TexturedModel, Terrain> render(Terrain terrain) {
        shader.loadTransformationMatrix(terrain.transformationMatrix());

        GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getTexturedModel().getVao().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        return this;
    }

    @Override
    public RenderCommand<TexturedModel, Terrain> preRender(TexturedModel t) {
        // gl stuff
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        // prep
        bindVao(t);
        bindVbo(Attribute.VERTICES);
        bindVbo(Attribute.TEXTURE_COORDS);
        bindVbo(Attribute.NORMALS);
        bindTexture(t.getTexture());

        // shader
        shader.bind();
        shader.loadViewMatrix(camera.viewMatrix());
        shader.loadLightPosition(light.getPosition());
        shader.loadLightColor(light.getColor());
        shader.loadReflectivity(t.getTexture().getReflictivity());
        shader.loadShineDamper(t.getTexture().getShineDumper());
        return this;
    }

    @Override
    public RenderCommand<TexturedModel, Terrain> postRender(TexturedModel t) {
        // unbind
        unbindVao();
        unbindVbo(Attribute.VERTICES);
        unbindVbo(Attribute.TEXTURE_COORDS);
        unbindVbo(Attribute.NORMALS);
        unbindTexture();
        // shader
        shader.unbind();
        return this;
    }
}
