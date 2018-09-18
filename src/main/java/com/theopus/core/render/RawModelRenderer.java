package com.theopus.core.render;

import com.theopus.core.models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class RawModelRenderer implements Renderer<RawModel> {

    @Override
    public void prepare() {
        GL11.glClearColor(0, 0, 0, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render(RawModel rawModel) {
        GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertices(), GL11.GL_UNSIGNED_INT, 0);
    }

    @Override
    public void preRender(RawModel rawModel) {
        GL30.glBindVertexArray(rawModel.getVaoId());
        GL20.glEnableVertexAttribArray(Attribute.VERTICES.getPosition());
    }

    @Override
    public void postRender() {
        GL20.glDisableVertexAttribArray(Attribute.VERTICES.getPosition());
        GL30.glBindVertexArray(0);
    }

}
