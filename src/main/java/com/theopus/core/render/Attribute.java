package com.theopus.core.render;

public enum Attribute {
    VERTICES(0, "position"),
    TEXTURE_COORDS(1, "textureCoords"),
    NORMALS(2, "normal");


    private final int position;
    private final String placeholder;

    Attribute(int position, String placeholder) {
        this.position = position;
        this.placeholder = placeholder;
    }

    public int getPosition() {
        return position;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
