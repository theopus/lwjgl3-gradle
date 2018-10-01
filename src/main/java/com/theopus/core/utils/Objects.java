package com.theopus.core.utils;

import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.load.TexturedModelLoader;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Objects {

    public static TexturedModel geCube(TexturedModelLoader loader){
        float[] vertices = {
                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,0.5f,-0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,-0.5f,0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                0.5f,0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                -0.5f,-0.5f,0.5f,
                -0.5f,0.5f,0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,0.5f,-0.5f,
                0.5f,0.5f,-0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,-0.5f,0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f

        };

        float[] textureCoords = {

                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0


        };

        int[] indices = {
                0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22

        };

        return loader.loadSimpleMesh(vertices, indices);
    }


    public static List<Vector3f> generatePoints(Vector3f a, Vector3f b, int n){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        List<Vector3f> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Vector3f vector3f = new Vector3f();
            vector3f.x = Math.min(a.x, b.x) + random.nextFloat() * (Math.max(a.x, b.x) - Math.min(a.x, b.x)) ;
            vector3f.y = Math.min(a.y, b.y) + random.nextFloat() * (Math.max(a.y, b.y) - Math.min(a.y, b.y)) ;
            vector3f.z = Math.min(a.z, b.z) + random.nextFloat() * (Math.max(a.z, b.z) - Math.min(a.z, b.z)) ;
            result.add(vector3f);
        }

        System.out.println(result);
        return result;
    }
}
