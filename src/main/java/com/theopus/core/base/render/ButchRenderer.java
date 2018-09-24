package com.theopus.core.base.render;

import com.theopus.core.base.objects.Vao;

import java.util.*;

public class ButchRenderer<T> {

    private Renderer<T> renderer;
    private Map<Vao, List<T>> renderMap = new HashMap<>();

    public ButchRenderer(Renderer<T> renderer) {
        this.renderer = renderer;
    }

    public ButchRenderer<T> put(Vao vao, T t){
        if(renderMap.containsKey(vao)){
            renderMap.get(vao).add(t);
        } else {
            renderMap.put(vao, Arrays.asList(t));
        }
        return this;
    }

    public ButchRenderer<T> put(Vao vao, List<T> t){
        if(renderMap.containsKey(vao)){
            renderMap.get(vao).addAll(t);
        } else {
            renderMap.put(vao, t);
        }
        return this;
    }

    public ButchRenderer<T> render(Vao vao) {
        renderList(renderMap.getOrDefault(vao, Collections.emptyList()));
        return this;
    }

    public ButchRenderer<T> render() {
        renderMap.forEach((vao, ts) -> renderList(ts));
        return this;
    }

    private void renderList(List<T> ts) {
        if (!ts.isEmpty()) {
            renderer.preRender(ts.get(0));
            ts.forEach(t -> renderer.render(t));
            renderer.postRender(ts.get(0));
        }
    }

}
