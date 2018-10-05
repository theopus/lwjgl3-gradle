package com.theopus.core.terrain;

import com.theopus.core.base.shader.ShaderProgram;
import com.theopus.core.base.shader.StaticShader;

public class TerrainShader extends StaticShader {

    private int blendMapLocation;
    private int backgroundLocation;
    private int rTexLocation;
    private int gTexLocation;
    private int bTexLocation;

    public TerrainShader(int programID, int vertexShaderID) {
        super(programID, vertexShaderID);
    }

    @Override
    protected void getAllUniformLocations() {
        super.getAllUniformLocations();
        blendMapLocation = super.getUniformLocation(Uniforms.BlendTextures.BLEND_MAP);
        backgroundLocation = super.getUniformLocation(Uniforms.BlendTextures.BACKGROUND);
        rTexLocation = super.getUniformLocation(Uniforms.BlendTextures.R);
        gTexLocation = super.getUniformLocation(Uniforms.BlendTextures.G);
        bTexLocation = super.getUniformLocation(Uniforms.BlendTextures.B);
    }

    public void connectTextures(){
        super.loadInt(blendMapLocation, 0);
        super.loadInt(backgroundLocation, 1);
        super.loadInt(rTexLocation, 2);
        super.loadInt(gTexLocation, 3);
        super.loadInt(bTexLocation, 4);
    }
}
