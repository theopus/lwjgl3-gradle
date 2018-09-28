package com.theopus.core;

import com.theopus.core.modules.configs.LoopConfig;
import com.theopus.core.utils.Sync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Loop {

    private static final Logger LOGGER = LoggerFactory.getLogger(Loop.class);

    private boolean showLogs;

    private int fps;
    private int ups;

    private int cfps;
    private int cups;

    private long updNanoPerFrame;
    private long fNanoPerFrame;

    private long counterStart;

    private Action render;
    private Action update;
    private Action input;
    private Interrupt interrupt;

    private Sync sync = new Sync();

    public Loop(LoopConfig loopConfig) {
        this.fps(loopConfig.getFps());
        this.ups(loopConfig.getUps());
        this.showLogs = loopConfig.isLogsEnabled();
    }

    public void loop() {
        long before = sync.getTime();
        long now;
        long elapsed;
        long accelerated = 0;
        counterStart = sync.getTime();

        input.process();
        while (!interrupt.check()) {

            if (showLogs) {
                if ((sync.getTime() - counterStart) > 1000_000_000) {
                    counterStart = sync.getTime();
                    LOGGER.info("FPS: [{}], UPS: [{}]", cfps, cups);
                    cups = 0;
                    cfps = 0;
                }
            }

            now = sync.getTime();

            elapsed = now - before;
            before = now;
            accelerated += elapsed;

            while (accelerated >= updNanoPerFrame) {
                update.process();
                accelerated -= updNanoPerFrame;
                cups++;
            }
            this.render.process();
            cfps++;
            this.sync.sync(fps);
        }
    }

    public Loop render(Action render) {
        this.render = render;
        return this;
    }

    public Loop update(Action update) {
        this.update = update;
        return this;
    }

    public Loop input(Action input) {
        this.input = input;
        return this;
    }

    public Loop interruptOn(Interrupt interrupt) {
        this.interrupt = interrupt;
        return this;
    }

    public Loop fps(int fps) {
        this.fps = fps;
        this.fNanoPerFrame = (long) ((1d / fps) * 1000_000_000);
        return this;
    }

    public Loop ups(int ups) {
        this.ups = ups;
        this.updNanoPerFrame = (long) ((1d / ups) * 1_000_000_000);
        return this;
    }

    public interface Action {
        void process();
    }

    public interface Interrupt {
        boolean check();
    }
}
