package com.theopus.core.shaders;

import java.io.IOException;

public class ShaderFactory {

    public static StaticShader createStaticShader(String vertex, String fragment) {
        try {
            return new StaticShader(
                    ShaderProgram.loadShader(vertex, ShaderProgram.Type.VERTEX),
                    ShaderProgram.loadShader(fragment, ShaderProgram.Type.FRAGMENT)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
