package com.theopus.core.terrain;

import com.theopus.core.base.objects.Entity;
import com.theopus.core.base.objects.TexturePackModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Terrain extends Entity {

    private float[][] heights;
    private TexturePackModel texturedModel;

    public Terrain(TexturePackModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public Terrain(Vector3f position, float rotX, float rotY, float rotZ, float scale, TexturePackModel texturedModel) {
        super(position, rotX, rotY, rotZ, scale);
        this.texturedModel = texturedModel;
    }

    public Terrain(TexturePackModel texturePackModel, float[][] heights) {
        super();
        this.texturedModel = texturePackModel;
        this.heights = heights;
    }

    public TexturePackModel getTexturedModel() {
        return texturedModel;
    }

    public float getHeightAt(float x, float z) {
        float terrainX = x - this.getPosition().x;
        float terrainZ = z - this.getPosition().z;
        float gridSquareSize = TerrainLoader.TILE_SIZE / ((float) heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

        float answer;
        if (xCoord <= (1 - zCoord)) {
            answer = barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                    heights[gridX + 1][gridZ], 0), new Vector3f(0,
                    heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            answer = barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                    heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                    heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        return answer;
    }

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }
}
