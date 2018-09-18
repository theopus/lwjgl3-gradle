package com.theopus.core.shaders;

import com.theopus.core.render.WindowManager;
import org.junit.Test;

import java.io.IOException;

public class ShaderProgramTest {

    @Test
    public void test() throws IOException {
        WindowManager wm = new WindowManager(1, 1);
        wm.createWindow(false);
        int i = ShaderProgram.loadShader("one.frag", ShaderProgram.Type.FRAGMENT);
    }


}