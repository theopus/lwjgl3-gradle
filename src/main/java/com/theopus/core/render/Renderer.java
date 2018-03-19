package com.theopus.core.render;

import com.theopus.core.models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by Oleksandr_Tkachov on 18.03.2018.
 */
public class Renderer {

    public void prepare() {
        GL11.glClearColor(1, 0, 0, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void render(RawModel rawModel) {
        GL30.glBindVertexArray(rawModel.getVaoId());
        GL20.glEnableVertexAttribArray(0);

        GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVerticies(), GL11.GL_UNSIGNED_INT, 0);
        //TEARDOWN
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

}
