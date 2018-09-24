package com.theopus.core.mesh;

import com.theopus.core.base.render.Renderer;
import com.theopus.core.base.StaticShader;
import com.theopus.core.base.objects.Camera;
import com.theopus.core.base.objects.Light;
import com.theopus.core.render.Attribute;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class MeshRenderer implements Renderer<Mesh> {
    private final Camera camera;
    private final Light light;
    private final StaticShader shader;

    public MeshRenderer(StaticShader shader, Matrix4f pjMtx, Camera camera, Light light) {
        this.camera = camera;
        this.shader = shader;
        this.light = light;

        shader.bind();
        shader.loadProjectionMatrix(pjMtx);
        shader.unbind();
    }

    @Override
    public MeshRenderer render(Mesh mesh) {
        shader.loadTransformationMatrix(mesh.transformationMatrix());

        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getMeshVao().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        return this;
    }

    @Override
    public MeshRenderer preRender(Mesh mesh) {
        // gl stuff
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // prep
        bindVao(mesh.getMeshVao());
        bindVbo(Attribute.VERTICES);
        bindVbo(Attribute.TEXTURE_COORDS);
        bindVbo(Attribute.NORMALS);
        bindTexture(mesh.getMeshVao().getTexture());

        // shader
        shader.bind();
        shader.loadViewMatrix(camera.viewMatrix());
        shader.loadLightPosition(light.getPosition());
        shader.loadLightColor(light.getColor());
        shader.loadReflectivity(mesh.getMeshVao().getTexture().getReflictivity());
        shader.loadShineDamper(mesh.getMeshVao().getTexture().getShineDumper());
        return this;
    }

    @Override
    public MeshRenderer postRender(Mesh mesh) {
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
