package com.theopus.core.terrain;

import com.theopus.core.base.memory.MemoryContext;
import org.joml.Vector3f;
import org.junit.Test;

import static org.junit.Assert.*;

public class TerrainLoaderTest {

    @Test
    public void loadTerrain() {
        MemoryContext ctx = new MemoryContext();
        TerrainLoader loader = new TerrainLoader(ctx);
    }
}