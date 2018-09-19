package com.theopus.core.shaders;

import com.theopus.core.exceptions.OpenGLEngineException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class ShaderProgram implements Closeable {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public static Logger LOGGER = LoggerFactory.getLogger(ShaderProgram.class);

    public ShaderProgram(int programID, int vertexShaderID) {
        this.programID = programID;
        this.vertexShaderID = vertexShaderID;
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName){
        return GL20.glGetUniformLocation(programID,uniformName);
    }

    protected abstract void bindAllAttributes();

    protected void bindAttribute(int attribute, String variableName){
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    public void start(){
        GL20.glUseProgram(programID);
    }

    public void stop(){
        GL20.glUseProgram(0);
    }

    @Override
    public void close() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    public static int loadShader(String file, Type type) throws IOException {
        StringBuilder shaderSource = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ShaderProgram.class.getClassLoader().getResourceAsStream(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append('\n');
            }
        }
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
        FRAGMENT(GL20.GL_VERTEX_SHADER),
        ;
        private int glBinding;

        Type(int glBinding) {
            this.glBinding = glBinding;
        }

        public int binding() {
            return this.glBinding;
        }
    }
}
