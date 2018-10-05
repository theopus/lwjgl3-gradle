package com.theopus.core.base.objects;

public class BlendTexturePack {
    private Texture blendMapTexture;
    private Texture backgroundTexture;
    private Texture rTexture;
    private Texture gTexture;
    private Texture bTexture;

    public BlendTexturePack(Texture blendMapTexture, Texture backgroundTexture, Texture rTexture, Texture gTexture, Texture bTexture) {
        this.blendMapTexture = blendMapTexture;
        this.backgroundTexture = backgroundTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
    }

    public Texture getBlendMapTexture() {
        return blendMapTexture;
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public Texture getrTexture() {
        return rTexture;
    }

    public Texture getgTexture() {
        return gTexture;
    }

    public Texture getbTexture() {
        return bTexture;
    }
}
