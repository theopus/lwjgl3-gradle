package com.theopus.core.terrain;

import com.theopus.core.base.objects.*;
import com.theopus.core.base.render.Attribute;
import com.theopus.core.base.render.RenderCommand;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TerrainRenderCommand implements RenderCommand<TexturePackModel, Terrain> {

    private TerrainShader shader;
    private Camera camera;
    private Light light;

    public TerrainRenderCommand(TerrainShader shader, Camera camera, Light light, Matrix4f pjMtx) {
        this.shader = shader;
        this.camera = camera;
        this.light = light;

        shader.bind();
        shader.loadProjectionMatrix(pjMtx);
        shader.loadFog(new Fog(new Vector3f(0.5f,0.5f,0.5f)));
        shader.connectTextures();
        shader.unbind();
    }

    @Override
    public RenderCommand<TexturePackModel, Terrain> render(Terrain terrain) {
        shader.loadTransformationMatrix(terrain.transformationMatrix());

        trianglesDraw(terrain.getTexturedModel());
        return this;
    }

    @Override
    public RenderCommand<TexturePackModel, Terrain> preRender(TexturePackModel t) {
        // gl stuff
        // gl stuff
        enableDepthTest();
        enableCulling();

        // prep
        bindVao(t);
        bindVbo(Attribute.VERTICES);
        bindVbo(Attribute.TEXTURE_COORDS);
        bindVbo(Attribute.NORMALS);
        bindTexurePack(t.getTexturePack());


        // shader
        shader.bind();
        shader.loadViewMatrix(camera.viewMatrix());
        shader.loadLightPosition(light.getPosition());
        shader.loadLightColor(light.getColor());
        return this;
    }

    @Override
    public RenderCommand<TexturePackModel, Terrain> postRender(TexturePackModel t) {
        // unbind
        unbindVao();
        unbindVbo(Attribute.VERTICES);
        unbindVbo(Attribute.TEXTURE_COORDS);
        unbindVbo(Attribute.NORMALS);
        unbindTexturePack();
        // shader
        shader.unbind();
        return this;
    }
}
