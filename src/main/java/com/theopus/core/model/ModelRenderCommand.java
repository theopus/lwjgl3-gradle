package com.theopus.core.model;

import com.theopus.core.base.objects.*;
import com.theopus.core.base.render.RenderCommand;
import com.theopus.core.base.shader.StaticShader;
import com.theopus.core.base.render.Attribute;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class ModelRenderCommand implements RenderCommand<TexturedModel, ModelEntity> {
    private final Camera camera;
    private final Light light;
    private final StaticShader shader;

    public ModelRenderCommand(StaticShader shader, Matrix4f pjMtx, Camera camera, Light light) {
        this.camera = camera;
        this.shader = shader;
        this.light = light;

        shader.bind();
        shader.loadProjectionMatrix(pjMtx);
        shader.unbind();
    }

    @Override
    public ModelRenderCommand render(ModelEntity modelEntity) {
        shader.loadTransformationMatrix(modelEntity.transformationMatrix());

        trianglesDraw(modelEntity.gettModel());
        return this;
    }

    @Override
    public ModelRenderCommand preRender(TexturedModel model) {
        // gl stuff
        enableDepthTest();
        enableCulling();

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
    public ModelRenderCommand postRender(TexturedModel model) {
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
