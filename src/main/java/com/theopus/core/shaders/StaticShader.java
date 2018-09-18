package com.theopus.core.shaders;

import com.theopus.core.render.Attribute;

public class StaticShader extends ShaderProgram {

    public StaticShader(int programID, int vertexShaderID) {
        super(programID, vertexShaderID);
    }

    @Override
    protected void getAllUniformLocations() {
        super.bindAttribute(Attribute.VERTICES.getPosition(),Attribute.VERTICES.getPlaceholder());
    }

    @Override
    protected void bindAllAttributes() {
        super.bindAttribute(Attribute.VERTICES.getPosition(),Attribute.VERTICES.getPlaceholder());
    }
}
