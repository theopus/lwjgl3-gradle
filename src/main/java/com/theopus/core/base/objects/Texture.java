package com.theopus.core.base.objects;

import com.theopus.core.memory.Resource;
import org.lwjgl.opengl.GL11;

public class Texture implements Resource {

    private int textureId;
    private int width;
    private int height;

    private float shineDumper = 1;
    private float reflictivity = 0;

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

    public void setShineDumper(float shineDumper) {
        this.shineDumper = shineDumper;
    }

    public void setReflictivity(float reflictivity) {
        this.reflictivity = reflictivity;
    }

    @Override
    public void cleanup() {
        GL11.glDeleteTextures(textureId);
    }

    public float getShineDumper() {
        return shineDumper;
    }

    public float getReflictivity() {
        return reflictivity;
    }
}
