package com.theopus.core.base.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MemoryContext implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryContext.class);

    private final List<Resource> resources = new ArrayList<>();

    public boolean put(Resource resource){
        return resources.add(resource);
    }

    @Override
    public void close() {
        resources.stream().peek(resource -> LOGGER.info("Cleared {}.", resource)).forEach(Resource::cleanup);
    }
}
