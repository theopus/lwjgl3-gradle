package com.theopus.core.gui;

import com.theopus.core.base.render.Attribute;
import com.theopus.core.base.shader.ShaderProgram;
import org.joml.Matrix4f;

import java.io.IOException;

public class GuiShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/guis/gui.vert";
    private static final String FRAGMENT_FILE = "src/guis/gui.frag";

    private int location_transformationMatrix;

    public GuiShader(String vert, String frag) throws IOException {
        super(loadShader(vert, Type.VERTEX), loadShader(frag, Type.FRAGMENT));
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix4f(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAllAttributes() {
        super.bindAttribute(Attribute.VERTICES);
    }




}
