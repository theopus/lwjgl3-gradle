package com.theopus.core.model;

import com.theopus.core.base.objects.*;
import com.theopus.core.base.render.Renderer;
import com.theopus.core.base.StaticShader;
import com.theopus.core.base.render.Attribute;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class ModelRenderer implements Renderer<TexturedModel, ModelEntity> {
    private final Camera camera;
    private final Light light;
    private final StaticShader shader;

    public ModelRenderer(StaticShader shader, Matrix4f pjMtx, Camera camera, Light light) {
        this.camera = camera;
        this.shader = shader;
        this.light = light;

        shader.bind();
        shader.loadProjectionMatrix(pjMtx);
        shader.unbind();
    }

    @Override
    public ModelRenderer render(ModelEntity modelEntity) {
        shader.loadTransformationMatrix(modelEntity.transformationMatrix());

        GL11.glDrawElements(GL11.GL_TRIANGLES, modelEntity.gettModel().getVao().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        return this;
    }

    @Override
    public ModelRenderer preRender(TexturedModel model) {
        // gl stuff
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // prep
        bindVao(model);
        bindVbo(Attribute.VERTICES);
        bindVbo(Attribute.TEXTURE_COORDS);
        bindVbo(Attribute.NORMALS);
        bindTexture(model.getTexture());

        // shader
        shader.bind();
        shader.loadViewMatrix(camera.viewMatrix());
        shader.loadLightPosition(light.getPosition());
        shader.loadLightColor(light.getColor());
        shader.loadReflectivity(model.getTexture().getReflictivity());
        shader.loadShineDamper(model.getTexture().getShineDumper());
        return this;
    }

    @Override
    public ModelRenderer postRender(TexturedModel model) {
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
