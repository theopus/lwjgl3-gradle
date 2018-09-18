package com.theopus.core.modules;

import com.theopus.core.App;
import com.theopus.core.render.KeyListener;
import com.theopus.core.render.RawModelStaticShaderRenderer;
import com.theopus.core.render.Renderer;
import com.theopus.core.render.WindowManager;
import com.theopus.core.shaders.ShaderFactory;
import com.theopus.core.shaders.ShaderProgram;
import com.theopus.core.shaders.StaticShader;
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
    public ShaderProgram staticShader(){
        return ShaderFactory.createStaticShader("one.vert", "one.frag");
    }

    @Provides
    @Singleton
    @Inject
    public App app(WindowManager wm,
                   Renderer renderer) {
        return new App(wm);
    }
}
