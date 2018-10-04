package com.theopus.core.base.objects;

import com.theopus.core.base.memory.Resource;
import org.lwjgl.opengl.GL11;

public class Texture implements Resource {

    private int textureId;
    private int width;
    private int height;

    private boolean hasTransparency;

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


    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

}
