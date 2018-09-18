package com.theopus.core.render;

import com.theopus.core.models.RawModel;
import com.theopus.core.shaders.ShaderProgram;
import com.theopus.core.shaders.StaticShader;

public class RawModelStaticShaderRenderer extends RawModelRenderer {

    private ShaderProgram shader;

    public RawModelStaticShaderRenderer(ShaderProgram shader) {
        this.shader = shader;
    }

    @Override
    public void preRender(RawModel rawModel) {
        super.preRender(rawModel);
        shader.start();
    }

    @Override
    public void postRender() {
        super.postRender();
        shader.stop();
    }
}
