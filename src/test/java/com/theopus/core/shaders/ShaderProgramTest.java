package com.theopus.core.shaders;

import com.theopus.core.base.ShaderProgram;
import com.theopus.core.window.WindowManager;
import org.junit.Test;

import java.io.IOException;

public class ShaderProgramTest {

    @Test
    public void test() throws IOException {
        WindowManager wm = new WindowManager(1, 1);
        wm.createWindow();
        int i = ShaderProgram.loadShader("static.frag", ShaderProgram.Type.FRAGMENT);
    }


}