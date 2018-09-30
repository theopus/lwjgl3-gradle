package com.theopus.core.base.shader;

import com.theopus.core.base.exceptions.OpenGLEngineException;
import com.theopus.core.base.memory.Resource;
import com.theopus.core.base.render.Attribute;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

public abstract class ShaderProgram implements Resource {

    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    private final FloatBuffer matrixBuffer;

    public static Logger LOGGER = LoggerFactory.getLogger(ShaderProgram.class);

    public ShaderProgram(int vertexShaderID, int fragmentShaderID) {
        this.vertexShaderID = vertexShaderID;
        this.fragmentShaderID = fragmentShaderID;
        this.programID = GL20.glCreateProgram();
        this.matrixBuffer = MemoryUtil.memAllocFloat(16);

        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAllAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    protected abstract void bindAllAttributes();

    public void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    public void loadVector3f(int location, Vector3f value) {
        GL20.glUniform3f(location, value.x, value.y, value.z);
    }

    public void loadBool(int location, boolean value) {
        GL20.glUniform1f(location, value ? 1 : 0);
    }

    public void loadMatrix4f(int location, Matrix4f value) {
        matrixBuffer.clear();
        FloatBuffer floatBuffer = value.get(matrixBuffer);
        GL20.glUniformMatrix4fv(location, false, floatBuffer);
        matrixBuffer.clear();
    }

    protected void bindAttribute(Attribute attribute) {
        GL20.glBindAttribLocation(programID, attribute.getPosition(), attribute.getPlaceholder());
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    @Override
    public void cleanup() {
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
        MemoryUtil.memFree(matrixBuffer);
    }

    public static int loadShader(String file, Type type) throws IOException {
        StringBuilder shaderSource = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ShaderProgram.class.getClassLoader().getResourceAsStream(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append('\n');
            }
        }

        LOGGER.info("SHADER = \n{}", shaderSource.toString());
        int shaderID = GL20.glCreateShader(type.binding());
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new OpenGLEngineException(String.format("Exception during initializing of [%s] shader, shader file: [%s]", type, file),
                    GL20.glGetShaderInfoLog(shaderID, 10_000));

        }
        return shaderID;
    }

    public enum Type {
        VERTEX(GL20.GL_VERTEX_SHADER),
        FRAGMENT(GL20.GL_FRAGMENT_SHADER),;
        private int glBinding;

        Type(int glBinding) {
            this.glBinding = glBinding;
        }

        public int binding() {
            return this.glBinding;
        }
    }

    public final class Uniforms {
        public static final String TRANSFORMATION_MATRIX = "transformationMatrix";
        public static final String PROJECTION_MATRIX = "projectionMatrix";
        public static final String VIEW_MATRIX = "viewMatrix";
        public static final String LIGHT_POSITION = "lightPosition";
        public static final String LIGHT_COLOR = "lightColor";
        public static final String SHINE_DAMPER = "shineDamper";
        public static final String REFLECTIVITY = "reflectivity";
    }
}
