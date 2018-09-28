package com.theopus.core.modules.configs;

public class LoopConfig {
    private final int fps;
    private final int ups;
    private final boolean logsEnabled;

    public LoopConfig(int fps, int ups, boolean logsEnabled) {
        this.fps = fps;
        this.ups = ups;
        this.logsEnabled = logsEnabled;
    }

    public int getFps() {
        return fps;
    }

    public int getUps() {
        return ups;
    }

    public boolean isLogsEnabled() {
        return logsEnabled;
    }
}
