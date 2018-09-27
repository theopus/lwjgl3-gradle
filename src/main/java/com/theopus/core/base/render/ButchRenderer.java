package com.theopus.core.base.render;

import com.theopus.core.base.objects.Entity;
import com.theopus.core.base.objects.Model;

import java.util.*;

public class ButchRenderer<M extends Model,T extends Entity> {

    private Renderer<M,T> renderer;
    private Map<M, List<T>> renderMap = new HashMap<>();

    public ButchRenderer(Renderer<M,T> renderer) {
        this.renderer = renderer;
    }

    public ButchRenderer<M,T> put(M m, T t){
        if(renderMap.containsKey(m)){
            renderMap.get(m).add(t);
        } else {
            renderMap.put(m, Arrays.asList(t));
        }
        return this;
    }

    public ButchRenderer<M,T> put(M m, List<T> t){
        if(renderMap.containsKey(m)){
            renderMap.get(m).addAll(t);
        } else {
            renderMap.put(m, t);
        }
        return this;
    }

    public ButchRenderer<M,T> render(M m) {
        renderList(m,renderMap.getOrDefault(m, Collections.emptyList()));
        return this;
    }

    public ButchRenderer<M,T> render() {
        renderMap.forEach(this::renderList);
        return this;
    }

    private void renderList(M M, List<T> ts) {
        if (!ts.isEmpty()) {
            renderer.preRender(M);
            ts.forEach(t -> renderer.render(t));
            renderer.postRender(M);
        }
    }

}
