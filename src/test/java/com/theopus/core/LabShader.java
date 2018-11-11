package com.theopus.core;

import com.theopus.core.base.render.Attribute;
import com.theopus.core.base.shader.ShaderProgram;

import java.io.IOException;

public class LabShader extends ShaderProgram {
    public LabShader(String vertex, String fragment) throws IOException {
        super(ShaderProgram.loadShader(vertex, Type.VERTEX), ShaderProgram.loadShader(fragment, Type.FRAGMENT));
    }

    @Override
    protected void getAllUniformLocations() {

    }

    @Override
    protected void bindAllAttributes() {
        bindAttribute(Attribute.VERTICES);
        bindAttribute(Attribute.TEXTURE_COORDS);
    }
}
