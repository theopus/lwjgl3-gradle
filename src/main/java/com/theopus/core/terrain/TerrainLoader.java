package com.theopus.core.terrain;

import com.theopus.core.base.exceptions.EngineException;
import com.theopus.core.base.load.MaterialModelLoader;
import com.theopus.core.base.memory.MemoryContext;
import com.theopus.core.base.objects.TexturePackModel;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerrainLoader extends MaterialModelLoader {

    public TerrainLoader(MemoryContext context) {
        super(context);
    }


    public TexturePackModel loadTerrain(String blendMap, String bg, String r, String g, String bl) {
        int VERTICES_PER_SIDE = 100;
        int TILE_SIZE = 800;
        int TOTAL_NUMBER = VERTICES_PER_SIDE * VERTICES_PER_SIDE;

        int QUADS_PER_SIDE = VERTICES_PER_SIDE - 1;
        int TOTAL_QUADS = QUADS_PER_SIDE * QUADS_PER_SIDE;


        float[] vertexes = new float[TOTAL_NUMBER * 3];
        float[] uvs = new float[TOTAL_NUMBER * 2];
        float[] normals = new float[TOTAL_NUMBER * 3];

        int[] indexes = new int[TOTAL_QUADS * 6];
        System.out.println(indexes.length);

        int vertexesCount = 0;
        int normalsCount = 0;
        int uvsCount = 0;
        //i==x
        //j==z
        for (int i = 0; i < VERTICES_PER_SIDE; i++) {
            for (int j = 0; j < VERTICES_PER_SIDE; j++) {
                vertexes[vertexesCount++] = (float)j/((float)VERTICES_PER_SIDE - 1) * TILE_SIZE;
                vertexes[vertexesCount++] = 0;
                vertexes[vertexesCount++] = (float)i/((float)VERTICES_PER_SIDE - 1) * TILE_SIZE;

                uvs[uvsCount++] = (float) j / ((float) VERTICES_PER_SIDE - 1);
                ;
                uvs[uvsCount++] = (float) i / ((float) VERTICES_PER_SIDE - 1);

                normals[normalsCount++] = 0;
                normals[normalsCount++] = 1;
                normals[normalsCount++] = 0;
            }
        }

        int indexesCount = 0;
        for (int x = 0; x < VERTICES_PER_SIDE - 1; x++) {
            for (int z = 0; z < VERTICES_PER_SIDE - 1; z++) {
                int topLeft = z + (x * VERTICES_PER_SIDE);
                int topRight = topLeft + 1;
                int bottomLeft = z + ((x + 1) * VERTICES_PER_SIDE);
                int bottomRight = bottomLeft + 1;

                indexes[indexesCount++] = topLeft;
                indexes[indexesCount++] = bottomLeft;
                indexes[indexesCount++] = topRight;
                indexes[indexesCount++] = topRight;
                indexes[indexesCount++] = bottomLeft;
                indexes[indexesCount++] = bottomRight;
            }
        }

        return loadTexturedPackModel(
                vertexes,
                indexes,
                uvs,
                normals,
                blendMap, bg, r, g, bl);
    }

    public TexturePackModel loadTerrain(Vector3f a, Vector3f b, float step, String texture, float tilingPercent,
                                        String blendMap, String bg, String r, String g, String bl) {
        if (a.y != 0 || b.y != 0) {
            throw new EngineException("Terrain model can be generated only at y=0.");
        }

        float sideA = b.x - a.x;
        float sideB = b.z - a.z;
        if (sideA != sideB) {
            throw new EngineException("Should be diagonal of sqare" + a + " - " + b);
        }

        tilingPercent = sideA / 100 * tilingPercent;

        boolean xDirection = !((a.x - b.x) > 0);
        boolean zDirection = !((a.z - b.z) > 0);

        List<Vector3f> points = new ArrayList<>();
        List<Float> vertices = new ArrayList<>();
        List<Float> textureCoords = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();

        for (float xVal = a.x; xVal < b.x; xVal = getValue(xDirection, xVal, step)) {
            for (float zVal = a.z; zVal < b.z; zVal = getValue(zDirection, zVal, step)) {
                float x1 = xVal;
                float x2 = getValue(xDirection, xVal, step);
                float z1 = zVal;
                float z2 = getValue(zDirection, zVal, step);

                Vector3f topLeft = new Vector3f(x1, 0, z2);
                Vector3f bottomLeft = new Vector3f(x1, 0, z1);
                Vector3f bottomRight = new Vector3f(x2, 0, z1);
                Vector3f topRight = new Vector3f(x2, 0, z2);

                int topLeftI = getIndex(points, vertices, textureCoords, normals, topLeft, tilingPercent);
                int bottomLeftI = getIndex(points, vertices, textureCoords, normals, bottomLeft, tilingPercent);
                int bottomRightI = getIndex(points, vertices, textureCoords, normals, bottomRight, tilingPercent);
                int topRightI = getIndex(points, vertices, textureCoords, normals, topRight, tilingPercent);

                indexes.add(topLeftI);
                indexes.add(topRightI);
                indexes.add(bottomRightI);

                indexes.add(bottomRightI);
                indexes.add(bottomLeftI);
                indexes.add(topLeftI);
            }
        }

        return loadTexturedPackModel(
                toArray(vertices),
                indexes.stream().mapToInt(Integer::intValue).toArray(),
                toArray(textureCoords),
                toArray(normals),
                blendMap, bg, r, g, bl);
    }

    private int getIndex(List<Vector3f> points, List<Float> vertices, List<Float> textureCoords, List<Float> normals, Vector3f pos, float tiling) {
        int topRightI;
        if (points.contains(pos)) {
            topRightI = points.indexOf(pos);
        } else {
            points.add(pos);
            topRightI = points.indexOf(pos);

            normals.add(0f);
            normals.add(1f);
            normals.add(0f);

            vertices.add(pos.x);
            vertices.add(pos.y);
            vertices.add(pos.z);

            textureCoords.add(pos.x / tiling);
            textureCoords.add(pos.z / tiling);
        }
        return topRightI;
    }

    private float getValue(boolean direction, float value, float step) {
        return direction ? value + step : value - step;
    }

    private float[] toArray(List<Float> floats) {
        float[] res = new float[floats.size()];
        for (int i = 0; i < floats.size(); i++) {
            res[i] = floats.get(i);
        }
        return res;
    }


}
