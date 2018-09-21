package com.theopus.core.objects;

import com.theopus.core.render.Bindable;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class Texture implements Bindable, Resource {

    private int textureId;
    private int width;
    private int height;

    public Texture(int textureId, int width, int height) {
        this.textureId = textureId;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureId(){
        return textureId;
    }

    @Override
    public void cleanup() {
        GL11.glDeleteTextures(textureId);
    }

    @Override
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    @Override
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
