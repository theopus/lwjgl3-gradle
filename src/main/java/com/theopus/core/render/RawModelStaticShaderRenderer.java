package com.theopus.core.render;

import com.theopus.core.models.RawModel;
import com.theopus.core.shaders.ShaderProgram;

public class RawModelStaticShaderRenderer extends RawModelRenderer {

    private ShaderProgram shader;

    public RawModelStaticShaderRenderer(ShaderProgram shader) {
        this.shader = shader;
    }

    @Override
    public void preRender(RawModel rawModel) {
        shader.start();
        super.preRender(rawModel);
    }

    @Override
    public void postRender() {
        super.postRender();
        shader.stop();
    }
}
