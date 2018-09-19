package com.theopus.core.render;

import com.theopus.core.models.RawModel;

public interface Renderer<T extends RawModel> {
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
