package com.theopus.core.base.objects;

import com.theopus.core.memory.Resource;

import java.util.Objects;

public class Model<Vao extends com.theopus.core.base.objects.Vao> implements Resource {

    private Vao vao;

    public Model(Vao vao) {
        this.vao = vao;
    }

    public Vao getVao() {
        return vao;
    }

    public void setVao(Vao vao) {
        this.vao = vao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return Objects.equals(vao, model.vao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vao);
    }

    @Override
    public void cleanup() {
        vao.cleanup();
    }

    @Override
    public String toString() {
        return "Model{" +
                "vao=" + vao +
                '}';
    }
}
