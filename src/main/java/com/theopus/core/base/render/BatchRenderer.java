package com.theopus.core.base.render;

import com.theopus.core.base.objects.Entity;
import com.theopus.core.base.objects.Model;

import java.util.*;

public class BatchRenderer<M extends Model, E extends Entity> implements Renderer<M, E> {

    private RenderCommand<M, E> renderCommand;
    private Map<M, List<E>> renderMap = new HashMap<>();

    BatchRenderer(RenderCommand<M, E> renderer) {
        this.renderCommand = renderer;
    }

    BatchRenderer(RenderCommand<M, E> command, Map<M, List<E>> renderMap) {
        this.renderCommand = command;
        this.renderMap = renderMap;
    }

    public BatchRenderer<M, E> put(M m, E e) {
        if (renderMap.containsKey(m)) {
            renderMap.get(m).add(e);
        } else {
            renderMap.put(m, Arrays.asList(e));
        }
        return this;
    }

    public BatchRenderer<M, E> put(M m, List<E> e) {
        if (renderMap.containsKey(m)) {
            renderMap.get(m).addAll(e);
        } else {
            renderMap.put(m, e);
        }
        return this;
    }

    public BatchRenderer<M, E> render(M m) {
        renderList(m, renderMap.getOrDefault(m, Collections.emptyList()));
        return this;
    }

    public BatchRenderer<M, E> render() {
        renderMap.forEach(this::renderList);
        return this;
    }

    private void renderList(M M, List<E> es) {
        if (!es.isEmpty()) {
            renderCommand.preRender(M);
            es.forEach(e -> renderCommand.render(e));
            renderCommand.postRender(M);
        }
    }

}
