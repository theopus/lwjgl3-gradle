package com.theopus.core;

import com.theopus.core.base.load.Loader;
import com.theopus.core.base.load.MaterialModelLoader;
import com.theopus.core.base.memory.MemoryContext;
import com.theopus.core.base.objects.Texture;
import com.theopus.core.base.objects.TexturedModel;
import com.theopus.core.base.window.WindowManager;
import com.theopus.core.modules.configs.WindowConfig;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.Vector4f;
import org.junit.Test;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class LabFiltering {

    @Test
    public void run() throws IOException {

        MaterialModelLoader loader = new MaterialModelLoader(new MemoryContext());

        WindowManager manager = new WindowManager(new WindowConfig(
                600, 400, new Vector4f(1, 0, 0, 1), false,
                0), (window, key, scancode, action, mods) -> {
        });
        manager.createWindow();
        manager.showWindow();

        TexturedModel texturedModel = loader.loadTexturedModel(new float[]{
                        -0.5f, 0.5f, 0,
                        -0.5f, -0.5f, 0,
                        0.5f, -0.5f, 0,
                        0.5f, 0.5f, 0,
                },
                new int[]{
                        0, 1, 3,
                        3, 1, 2
                },
                new float[]{
                        0, 0,
                        0, 1,
                        1, 1,
                        1, 0
                });

        texturedModel.setTexture(loadTexture("chrome1x.png"));

        LabShader shader = new LabShader("lab.vert", "lab.frag");

        while (!manager.windowShouldClose()) {

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            shader.bind();
            GL30.glBindVertexArray(texturedModel.getVao().getVaoId());

            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);


            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getTextureId());


            GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getVao().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);


            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);

            shader.unbind();
            manager.update();
        }

    }

    public ByteBuffer applyBlur(ByteBuffer from, ByteBuffer to, int width, int height) {

        Pixel[][] pixels = new Pixel[width][height];

        int ypoiner = 0;
        int xpoiner = 0;

        for (int i = 0; i < from.limit(); i += 4) {
            from.get(i);
            from.get(i + 1);
            from.get(i + 2);
            from.get(i + 3);

            pixels[xpoiner][ypoiner] = new Pixel(
                    from.get(i) & 0b11111111,
                    from.get(i + 1) & 0b11111111,
                    from.get(i + 2) & 0b11111111,
                    from.get(i + 3) & 0b11111111,
                    xpoiner,
                    ypoiner
            );

            ypoiner++;

            if (ypoiner % width == 0) {
                ypoiner = 0;
                xpoiner++;
            }
        }

        double[][] filterMatrix = kernel(3, 8f);


        double blue = 0.0;
        double green = 0.0;
        double red = 0.0;


        int filterWidth = filterMatrix.length;
        int filterHeight = filterMatrix.length;


        int filterOffset = (filterWidth - 1) / 2;
        int calcOffset = 0;


        int byteOffset = 0;


        for (int offsetY = filterOffset; offsetY < height - filterOffset; offsetY++) {
            for (int offsetX = filterOffset; offsetX < width - filterOffset; offsetX++) {
                blue = 0;
                green = 0;
                red = 0;


                byteOffset = offsetY + offsetX * 4;


                for (int filterY = -filterOffset; filterY <= filterOffset; filterY++) {
                    for (int filterX = -filterOffset; filterX <= filterOffset; filterX++) {
                        calcOffset = byteOffset + (filterX * 4) + (filterY);

                        blue += (double) (from.get(calcOffset)) * filterMatrix[filterY + filterOffset][filterX + filterOffset];
                        green += (double) (from.get(calcOffset + 1)) * filterMatrix[filterY + filterOffset][filterX + filterOffset];
                        red += (double) (from.get(calcOffset + 2)) * filterMatrix[filterY + filterOffset][filterX + filterOffset];
                    }
                }


                blue = blue;
                green = green;
                red = red;


                blue = (blue > 255 ? 255 : (blue < 0 ? 0 : blue));
                green = (green > 255 ? 255 : (green < 0 ? 0 : green));
                red = (red > 255 ? 255 : (red < 0 ? 0 : blue));


                to.put(byteOffset, (byte) (blue));
                to.put(byteOffset + 1, (byte) (green));
                to.put(byteOffset + 2, (byte) (red));
                to.put(byteOffset + 3, (byte) (255));
            }
        }

        return to;
    }

    public Texture loadTexture(String path) {

        try (InputStream resourceAsStream = Loader.class.getClassLoader().getResourceAsStream(path);) {
            PNGDecoder decoder = new PNGDecoder(resourceAsStream);
            int width = decoder.getWidth();
            int height = decoder.getHeight();

            int textureId = GL11.glGenTextures();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            ByteBuffer byteBuffer = MemoryUtil.memAlloc(4 * width * height);
            ByteBuffer byteBuffer2 = MemoryUtil.memAlloc(4 * width * height);
            decoder.decode(byteBuffer, height * 4, PNGDecoder.Format.RGBA);

            byteBuffer = applyBlur(byteBuffer, byteBuffer2, width, height);

            System.out.println(byteBuffer);
            byteBuffer.flip();

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            byteBuffer.clear();
//            MemoryUtil.memFree(byteBuffer);
//            MemoryUtil.memFree(byteBuffer2);


            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            Texture texture = new Texture(textureId, width, height);
            return texture;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Pixel {
        long r;
        long g;
        long b;
        long a;
        private int x;
        private int y;

        public Pixel(long r, long g, long b, long a, int x, int y) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Pixel{" +
                    "r=" + r +
                    ", g=" + g +
                    ", b=" + b +
                    ", a=" + a +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private double[][] kernel(int length, double weight) {
        double[][] kernel = new double[length][length];
        double sumTotal = 0;

        int kernelRadius = length / 2;
        double distance = 0;

        double calculatedEuler = 1.0 / (2.0 * Math.PI * Math.pow(weight, 2));


        for (int filterY = -kernelRadius;
             filterY <= kernelRadius; filterY++) {
            for (int filterX = -kernelRadius; filterX <= kernelRadius; filterX++) {
                distance = ((filterX * filterX) + (filterY * filterY)) / (2 * (weight * weight));

                kernel[filterY + kernelRadius][filterX + kernelRadius] = calculatedEuler * Math.exp(-distance);

                sumTotal += kernel[filterY + kernelRadius][filterX + kernelRadius];
            }
        }

        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                kernel[y][x] = kernel[y][x] * (1.0 / sumTotal);
            }
        }

        return kernel;
    }
}