package com.theopus.core.base.shader;

import com.theopus.core.base.render.Attribute;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class StaticShader extends ShaderProgram {

    private int transMatrixLocation;
    private int projMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;
    private int lightColorLocation;
    private int shineDamperLocation;
    private int reflectivityLocation;
    private int useFakeLightLocation;
    private int hasTransparencyLocation;

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
        shineDamperLocation = super.getUniformLocation(Uniforms.SHINE_DAMPER);
        reflectivityLocation = super.getUniformLocation(Uniforms.REFLECTIVITY);
        useFakeLightLocation = super.getUniformLocation(Uniforms.USE_FAKE_LIGHT);
        hasTransparencyLocation = super.getUniformLocation(Uniforms.HAS_TRANSPARENCY);
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

    public void loadShineDamper(float value) {
        super.loadFloat(shineDamperLocation, value);
    }

    public void loadReflectivity(float value) {
        super.loadFloat(reflectivityLocation, value);
    }

    public void loadUseFakeLight(boolean value) {
        super.loadFloat(useFakeLightLocation, value ? 1 : 0);
    }

    public void loadHasTransparency(boolean value) {
        super.loadFloat(hasTransparencyLocation, value ? 1 : 0);
    }
}
