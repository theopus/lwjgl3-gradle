package com.theopus.core.render;

import com.theopus.core.shaders.ShaderProgram;

public abstract class Renderer<T extends Bindable, S extends ShaderProgram> {

    private S shader;

    public Renderer(S shader) {
        this.shader = shader;
    }

    public abstract void render(T t);

    public void preRender(T t){
        shader.bind();
        t.bind();
    }

    public void postRender(T t) {
        shader.unbind();
        t.unbind();
    }

    public void renderCycle(T t){
        preRender(t);
        render(t);
        postRender(t);
    }
}
