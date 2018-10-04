package com.theopus.core.base.objects;

public class MaterialModel extends Model<TexturedVao> {

    private final Material material;

    public MaterialModel(TexturedVao vao, Material material) {
        super(vao);
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        material.cleanup();
    }
}
