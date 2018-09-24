package com.theopus.core.base.render;

import com.theopus.core.base.objects.Texture;
import com.theopus.core.mesh.MeshVao;
import com.theopus.core.render.Attribute;
import org.lwjgl.opengl.*;

public interface Renderer<T> {

    Renderer<T> render(T t);

    Renderer<T> preRender(T t);

    Renderer<T> postRender(T t);

    default void bindVao(MeshVao meshVao){
        GL30.glBindVertexArray(meshVao.getVaoId());
    }

    default void unbindVao(){
        GL30.glBindVertexArray(0);
    }

    default void bindVbo(Attribute vertices){
        GL20.glEnableVertexAttribArray(vertices.getPosition());
    }

    default void unbindVbo(Attribute vertices){
        GL20.glDisableVertexAttribArray(vertices.getPosition());
    }

    default void bindTexture(Texture texture){
//        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
    }

    default void unbindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }


//    default Renderer<T> fullrender(T t){
//        return preRender(t)
//                .render(t)
//                .postRender(t);
//    }

}
