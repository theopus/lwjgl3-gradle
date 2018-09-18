package com.theopus.core.render;

public interface Renderer<T> {
    void prepare();

    void render(T t);

    void preRender(T t);

    void postRender();

    default void renderCycle(T t){
        preRender(t);
        render(t);
        postRender();
    }
}
