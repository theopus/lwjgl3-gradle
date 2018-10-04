package com.theopus.core.base.shader;

import com.theopus.core.base.objects.Fog;
import com.theopus.core.base.objects.Material;
import com.theopus.core.base.render.Attribute;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class StaticShader extends ShaderProgram {

    private int transMatrixLocation;
    private int projMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;
    private int lightColorLocation;

    private int materialHasTextureLocation;
    private int materialHasTransparencyLocation;
    private int materialReflectivityLocation;
    private int materialShineDamperLocation;
    private int materialUseFakeLightLocation;

    private int fogEnabled;
    private int fogColor;
    private int fogDensity;
    private int fogGradient;

    public StaticShader(int programID, int vertexShaderID) {
        super(programID, vertexShaderID);
    }

    @Override
    protected void getAllUniformLocations() {
        transMatrixLocation = super.getUniformLocation(Uniforms.TRANSFORMATION_MATRIX);
        projMatrixLocation = super.getUniformLocation(Uniforms.PROJECTION_MATRIX);
        viewMatrixLocation = super.getUniformLocation(Uniforms.VIEW_MATRIX);
        lightPositionLocation = super.getUniformLocation(Uniforms.LIGHT_POSITION);
        lightColorLocation = super.getUniformLocation(Uniforms.LIGHT_COLOR);

        materialHasTextureLocation = super.getStructUniformLocation(Uniforms.Material.VARIABLE, Uniforms.Material.HAS_TEXTURE);
        materialHasTransparencyLocation = super.getStructUniformLocation(Uniforms.Material.VARIABLE, Uniforms.Material.HAS_TRANSPARENCY);
        materialReflectivityLocation = super.getStructUniformLocation(Uniforms.Material.VARIABLE, Uniforms.Material.REFLECTIVITY);
        materialShineDamperLocation = super.getStructUniformLocation(Uniforms.Material.VARIABLE, Uniforms.Material.SHINE_DAMPER);
        materialUseFakeLightLocation = super.getStructUniformLocation(Uniforms.Material.VARIABLE, Uniforms.Material.USE_FAKE_LIGHT);

        fogEnabled = super.getStructUniformLocation(Uniforms.Fog.VARIABLE, Uniforms.Fog.ENABLED);
        fogColor = super.getStructUniformLocation(Uniforms.Fog.VARIABLE, Uniforms.Fog.COLOR);
        fogDensity = super.getStructUniformLocation(Uniforms.Fog.VARIABLE, Uniforms.Fog.DESITY);
        fogGradient = super.getStructUniformLocation(Uniforms.Fog.VARIABLE, Uniforms.Fog.GRADIENT);
    }

    @Override
    protected void bindAllAttributes() {
        super.bindAttribute(Attribute.VERTICES);
        super.bindAttribute(Attribute.TEXTURE_COORDS);
        super.bindAttribute(Attribute.NORMALS);
    }

    public void loadTransformationMatrix(Matrix4f matrix4f) {
        super.loadMatrix4f(transMatrixLocation, matrix4f);
    }

    public void loadProjectionMatrix(Matrix4f projMatrix) {
        super.loadMatrix4f(projMatrixLocation, projMatrix);
    }

    public void loadViewMatrix(Matrix4f viewMatrix) {
        super.loadMatrix4f(viewMatrixLocation, viewMatrix);
    }

    public void loadLightPosition(Vector3f position) {
        super.loadVector3f(lightPositionLocation, position);
    }

    public void loadLightColor(Vector3f position) {
        super.loadVector3f(lightColorLocation, position);
    }

    public void loadMaterial(Material material) {

        if (material.isHasTexture()) {
            super.loadBool(materialHasTextureLocation, material.isHasTexture());
            super.loadBool(materialHasTransparencyLocation, material.getTexture().isHasTransparency());
        } else {
            super.loadBool(materialHasTextureLocation, material.isHasTexture());
        }
        super.loadFloat(materialReflectivityLocation, material.getReflectivity());
        super.loadFloat(materialShineDamperLocation, material.getShineDamper());
        super.loadBool(materialUseFakeLightLocation, material.isUseFakeLight());
    }

    public void loadFog(Fog fog){
        super.loadBool(fogEnabled, fog.isEnabled());
        super.loadVector3f(fogColor, fog.getColor());
        super.loadFloat(fogDensity, fog.getDensity());
        super.loadFloat(fogGradient, fog.getGradient());
    }
}
