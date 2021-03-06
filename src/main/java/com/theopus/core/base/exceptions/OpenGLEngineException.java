package com.theopus.core.base.exceptions;

public class OpenGLEngineException extends EngineException {
    public OpenGLEngineException(String message, String openGlCause) {
        super(String.format("%s. OpenGL: %s",message, openGlCause));
    }
}
