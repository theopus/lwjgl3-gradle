package com.theopus.core.base.render;

import com.theopus.core.base.objects.Entity;
import com.theopus.core.base.objects.Model;
import com.theopus.core.base.objects.Texture;
import org.lwjgl.opengl.*;

public interface RenderCommand<M extends Model, E extends Entity> {

    RenderCommand<M, E> render(E e);

    RenderCommand<M, E> preRender(M t);

    RenderCommand<M, E> postRender(M t);


    default<Mod extends com.theopus.core.base.objects.Model> void trianglesDraw(Mod m){
        GL11.glDrawElements(GL11.GL_TRIANGLES, m.getVao().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
    }

    default void enableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    default void disableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE);

    }

    default void enableDepthTest(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    default void disbleDepthTest(){
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

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
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
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
