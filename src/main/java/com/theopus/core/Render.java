package com.theopus.core;

import com.theopus.core.base.render.Renderer;

import java.util.ArrayList;
import java.util.List;

public class Render {

    private List<Renderer<?,?>> renderers;

    public Render() {
        renderers = new ArrayList<>();
    }

    public Render(List<Renderer<?, ?>> renderers) {
        this.renderers = renderers;
    }

    public void render(){
        renderers.forEach(Renderer::render);
    }


}
