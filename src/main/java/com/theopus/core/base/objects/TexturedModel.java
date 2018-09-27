package com.theopus.core.base.objects;

public class TexturedModel extends Model<TexturedVao> {
    private final Texture texture;

    public TexturedModel(TexturedVao vao, Texture texture) {
        super(vao);
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        texture.cleanup();
    }
}
