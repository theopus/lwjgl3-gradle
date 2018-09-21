package com.theopus.core.render;

import com.theopus.core.objects.Resource;

import java.util.ArrayList;
import java.util.List;

public class MemoryContext implements AutoCloseable {

    private final List<Resource> resources = new ArrayList<>();

    public boolean put(Resource resource){
        return resources.add(resource);
    }

    @Override
    public void close() {
        resources.forEach(Resource::cleanup);
    }
}
