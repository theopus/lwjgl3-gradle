package com.theopus.core.modules;

import com.theopus.core.App;
import com.theopus.core.render.*;
import com.theopus.core.shaders.ShaderFactory;
import com.theopus.core.shaders.ShaderProgram;
import dagger.Module;
import dagger.Provides;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.theopus.core.modules.PropertiesModule.WINDOW_HEIGHT;
import static com.theopus.core.modules.PropertiesModule.WINDOW_WIDTH;

@Module
public class MainModule {


    @Singleton
    @Provides
    public GLFWKeyCallbackI listener(){
        return new KeyListener();
    }

    @Singleton
    @Provides
    @Inject
    public WindowManager windowManager(@Named(WINDOW_WIDTH) Integer width,
                                       @Named(WINDOW_HEIGHT) Integer height,
                                       GLFWKeyCallbackI listener){
        return new WindowManager(width, height, listener);
    }

    @Singleton
    @Provides
    @Inject
    public Renderer renderer(ShaderProgram shaderProgram){
        return new RawModelStaticShaderRenderer(shaderProgram);
    }

    @Singleton
    @Provides
    @Inject
    public ShaderProgram staticShader(WindowManager windowManager){
        windowManager.createWindow();
        return ShaderFactory.createStaticShader("one.vert", "one.frag");
    }

    @Singleton
    @Provides
    @Inject
    public MemoryContext ctx(){
        return new MemoryContext();
    }

    @Provides
    @Singleton
    @Inject
    public App app(WindowManager wm,
                   Renderer renderer,
                   MemoryContext ctx) {
        return new App(wm, renderer, ctx);
    }
}
