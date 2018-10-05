package com.theopus.core.base.objects;

public class TexturePackModel extends Model<TexturedVao> {

    private BlendTexturePack texturePack;

    public TexturePackModel(TexturedVao vao, BlendTexturePack texturePack) {
        super(vao);
        this.texturePack = texturePack;
    }

    public BlendTexturePack getTexturePack() {
        return texturePack;
    }

    public void setTexturePack(BlendTexturePack texturePack) {
        this.texturePack = texturePack;
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
}
