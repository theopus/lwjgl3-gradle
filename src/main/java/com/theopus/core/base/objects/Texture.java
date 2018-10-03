package com.theopus.core.base.objects;

import com.theopus.core.base.memory.Resource;
import org.lwjgl.opengl.GL11;

public class Texture implements Resource {

    private int textureId;
    private int width;
    private int height;

    private float shineDumper = 1;
    private float reflectivity = 0;

    private boolean hasTransparency;
    private boolean useFakeLight;

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

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    @Override
    public void cleanup() {
        GL11.glDeleteTextures(textureId);
    }

    public float getShineDumper() {
        return shineDumper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean isUseFakeLight() {
        return useFakeLight;
    }

    public void setUseFakeLight(boolean useFakeLight) {
        this.useFakeLight = useFakeLight;
    }
}
