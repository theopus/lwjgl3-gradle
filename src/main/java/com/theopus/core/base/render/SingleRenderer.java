package com.theopus.core.base.render;

import com.theopus.core.base.objects.Entity;
import com.theopus.core.base.objects.Model;

public class SingleRenderer<M extends Model, E extends Entity> implements Renderer<M, E> {

    private RenderCommand<M, E> command;
    private final M m;
    private final E e;

    SingleRenderer(RenderCommand<M,E> command, M m, E e) {
        this.command = command;
        this.m = m;
        this.e = e;
    }

    @Override
    public Renderer<M, E> render() {
        command.preRender(m);
        command.render(e);
        command.postRender(m);
        return this;
    }
}
