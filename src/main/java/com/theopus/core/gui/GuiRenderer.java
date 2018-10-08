package com.theopus.core.gui;

import com.theopus.core.base.load.Loader;
import com.theopus.core.base.objects.Vao;
import com.theopus.core.utils.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class GuiRenderer {

    private Vao quad;
    private GuiShader shader;


    public GuiRenderer(Loader loader, GuiShader shader) {
        this.shader = shader;
        float positions[] = {
                -1, 1,
                -1, -1,
                1, 1,
                1, -1};
        quad = loader.loadVao(positions);
    }

    public void render(List<GuiTexture> textureList){
        GL30.glBindVertexArray(quad.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        shader.bind();
        //
        for (GuiTexture guiTexture : textureList) {
            shader.loadTransformation(Maths.createTransformationMatrix2D(guiTexture.getPosition(), guiTexture.getScale()));
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, guiTexture.getId());
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }
        //
        shader.unbind();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL30.glBindVertexArray(0);
        GL20.glDisableVertexAttribArray(0);

    }
}
