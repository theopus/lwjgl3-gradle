package com.theopus.core.utils;

import org.joml.Vector3f;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void generatePoints() {
        List<Vector3f> vector3fs = Objects.generatePoints(new Vector3f(-10, 0, 10), new Vector3f(10, 0, -10), 10);
        System.out.println(vector3fs);
    }
}