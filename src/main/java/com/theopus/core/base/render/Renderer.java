package com.theopus.core.base.render;

import com.theopus.core.base.objects.Entity;
import com.theopus.core.base.objects.Model;

import java.util.List;
import java.util.Map;

public interface Renderer<M extends Model, E extends Entity> {

    Renderer<M, E> render();

    public static<M extends Model, E extends Entity> SingleRenderer<M, E> singleOf(RenderCommand<M, E> command, M m, E e) {
        return new SingleRenderer<>(command, m, e);
    }

    public static <M extends Model, E extends Entity> BatchRenderer<M, E> batchOf(RenderCommand<M, E> command) {
        return new BatchRenderer<>(command);
    }

    public static <M extends Model, E extends Entity> BatchRenderer<M, E> batchOf(RenderCommand<M, E> command, Map<M, List<E>> renderMap) {
        return new BatchRenderer<>(command, renderMap);
    }

}
