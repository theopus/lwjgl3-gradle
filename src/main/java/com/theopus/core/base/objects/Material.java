package com.theopus.core.base.objects;

import com.theopus.core.base.memory.Resource;

public class Material implements Resource {

    private Texture texture;
    private float reflectivity;
    private float shineDamper = 1;
    private boolean useFakeLight;

    public Material(Texture texture) {
        this.texture = texture;
    }

    public Material(Texture texture, float reflectivity, float shineDamper, boolean useFakeLight) {
        this.texture = texture;
        this.reflectivity = reflectivity;
        this.shineDamper = shineDamper;
        this.useFakeLight = useFakeLight;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isHasTexture(){
        return texture != null;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public boolean isUseFakeLight() {
        return useFakeLight;
    }

    @Override
    public void cleanup() {
        texture.cleanup();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public void setUseFakeLight(boolean useFakeLight) {
        this.useFakeLight = useFakeLight;
    }
}
