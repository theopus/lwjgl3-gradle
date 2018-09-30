package com.theopus.core.base.render;

import com.theopus.core.base.objects.Entity;
import com.theopus.core.base.objects.Model;
import com.theopus.core.base.objects.Texture;
import org.lwjgl.opengl.*;

public interface RenderCommand<M extends Model, E extends Entity> {

    RenderCommand<M, E> render(E e);

    RenderCommand<M, E> preRender(M t);

    RenderCommand<M, E> postRender(M t);

    default void bindVao(Model meshVao){
        GL30.glBindVertexArray(meshVao.getVao().getVaoId());
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


//    default RenderCommand<E> fullrender(E t){
//        return preRender(t)
//                .render(t)
//                .postRender(t);
//    }

}