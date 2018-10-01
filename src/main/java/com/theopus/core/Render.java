package com.theopus.core;

import com.theopus.core.base.render.Renderer;
import org.lwjgl.opengl.GL11;

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

    public void add(Renderer<?,?> renderer){
        renderers.add(renderer);
    }
    public void add(int index, Renderer<?,?> renderer){
        renderers.add(index,renderer);
    }
    public void addAsFirst(Renderer<?,?> renderer){
        renderers.add(0, renderer);
    }

    public void render(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        renderers.forEach(Renderer::render);
    }


}
