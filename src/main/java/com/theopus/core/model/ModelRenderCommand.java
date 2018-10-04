package com.theopus.core.model;

import com.theopus.core.base.objects.*;
import com.theopus.core.base.render.RenderCommand;
import com.theopus.core.base.shader.StaticShader;
import com.theopus.core.base.render.Attribute;
import org.joml.Matrix4f;

public class ModelRenderCommand implements RenderCommand<MaterialModel, ModelEntity> {
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
    public ModelRenderCommand preRender(MaterialModel model) {
        // gl stuff
        enableDepthTest();
        if (!model.getMaterial().getTexture().isHasTransparency()) {
            enableCulling();
        } else {
            disableCulling();
        }

        // prep
        bindVao(model);
        bindVbo(Attribute.VERTICES);
        bindVbo(Attribute.TEXTURE_COORDS);
        bindVbo(Attribute.NORMALS);
        bindTexture(model.getMaterial().getTexture());

        // shader
        shader.bind();
        shader.loadViewMatrix(camera.viewMatrix());
        shader.loadLightPosition(light.getPosition());
        shader.loadLightColor(light.getColor());
        shader.loadMaterial(model.getMaterial());

        return this;
    }

    @Override
    public ModelRenderCommand postRender(MaterialModel model) {
        if (model.getMaterial().getTexture().isHasTransparency()){
            enableCulling();
        }
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
