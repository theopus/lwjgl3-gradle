package com.theopus.core.base.objects;

import com.theopus.core.base.objects.Point;
import org.joml.Vector3f;

public class Light extends Point {

    private Vector3f color;

    public Light(Vector3f position, Vector3f color) {
        super(position);
        this.color = color;
    }

    public Light(Vector3f color) {
        super();
        this.color = color;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }
}
