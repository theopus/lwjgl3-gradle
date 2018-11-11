package com.theopus.core;

import com.theopus.core.base.window.WindowManager;
import com.theopus.core.modules.configs.WindowConfig;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.junit.Test;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

public class LabRastering {

    @Test
    public void run() {

        WindowManager manager = new WindowManager(new WindowConfig(
                600, 400, new Vector4f(0, 0, 0, 1), true,
                0), (window, key, scancode, action, mods) -> {
        });
        manager.createWindow();
        manager.showWindow();


        final float step = 0.05f;
        List<Line> grid = createGrid(step);

        List<Pixel> dda = dda(-10, -2, 9, -18, step);
        List<Pixel> bresenham = bresenham(-0, 9, 19, -8, step);

        while (!manager.windowShouldClose()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            grid.forEach(Line::draw);

            drawPixels(dda);

            manager.update();
        }

    }

    void drawPixels(List<Pixel> pixels){
        pixels.forEach(Pixel::draw);
    }

    List<Pixel> bresenham(int x0, int y0, int x1, int y1, float st) {
        if (abs(y1 - y0) < abs(x1 - x0)) {
            if (x0 > x1) {
                return bLineLow(x1, y1, x0, y0, st);
            } else {
                return bLineLow(x0, y0, x1, y1, st);
            }
        } else {
            if (y0 > y1) {
                return bLineHigh(x1, y1, x0, y0, st);
            } else {
                return bLineHigh(x0, y0, x1, y1, st);
            }
        }
    }

    private List<Pixel> bLineHigh(int x0, int y0, int x1, int y1, float st) {
        List<Pixel> result = new ArrayList<>();
        float dx = x1 - x0;
        float dy = y1 - y0;
        int xi = 1;
        if (dx < 0) {
            xi = -1;
            dx = -dx;
        }

        float D = 2 * dx - dy;
        float x = x0;

        for (int y = x0; y < y1; y++) {
            result.add(new Pixel((int) x, y, st));
            if (D > 0) {
                x = x + xi;
                D = D - 2 * dy;
            }
            D = D + 2 * dx;
        }

        return result;
    }
    private List<Pixel> bLineLow(int x0, int y0, int x1, int y1, float st) {
        List<Pixel> result = new ArrayList<>();
        float dx = x1 - x0;
        float dy = y1 - y0;

        int yi = 1;
        if (dy < 0) {
            yi = -1;
            dy = -dy;
        }

        float D = 2 * dy - dx;
        float y = y0;

        for (int x = x0; x < x1; x++) {
            result.add(new Pixel(x, (int) y, st));
            if (D > 0) {
                y = y + yi;
                D = D - 2 * dx;
            }
            D = D + 2 * dy;
        }

        return result;
    }

    List<Pixel> dda(int x0, int y0, int x1, int y1, float st) {
        List<Pixel> pixels = new ArrayList<>();
        int dx = x1 - x0;
        int dy = y1 - y0;

        int steps = abs(dx) > abs(dy) ? abs(dx) : abs(dy);
        float xinc = dx / (float) steps;
        float yinc = dy / (float) steps;

        float x = x0;
        float y = y0;
        for (int i = 0; i <= steps; i++) {
            pixels.add(new Pixel(((int) x), ((int) y), st));
            x += xinc;
            y += yinc;
        }
        return pixels;
    }

    private List<Line> createGrid(float step) {
        float xMin = -1;
        float xMax = 1;
        float yMin = -1;
        float yMax = 1;

        List<Line> lines = new ArrayList<>();
        float x = 0;

        while (x <= xMax) {

            lines.add(new Line(
                    new Vector2f(x, yMin),
                    new Vector2f(x, yMax)
            ));

            x += step;
        }
        x = 0;
        while (x >= xMin) {

            lines.add(new Line(
                    new Vector2f(x, yMin),
                    new Vector2f(x, yMax)
            ));

            x -= step;
        }

        float y = yMin;
        while (y <= yMax) {

            lines.add(new Line(
                    new Vector2f(xMin, y),
                    new Vector2f(xMax, y)
            ));

            y += step;
        }
        y = 0;
        while (y >= yMin) {

            lines.add(new Line(
                    new Vector2f(xMin, y),
                    new Vector2f(xMax, y)
            ));

            y -= step;
        }
        return lines;
    }

    private class Pixel {
        float x;
        float y;
        float step;

        public Pixel(int x, int y, float step) {
            this.x = x * step;
            this.y = y * step;
            this.step = step;
        }

        void draw() {
            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glVertex2f(x, y);
                GL11.glVertex2f(x + step, y);
                GL11.glVertex2f(x + step, y + step);
                GL11.glVertex2f(x, y + step);
            }
            GL11.glEnd();
        }

        @Override
        public String toString() {
            return "Pixel{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private class Line {
        Vector2f a;
        Vector2f b;

        public Line(Vector2f a, Vector2f b) {
            this.a = a;
            this.b = b;
        }

        void draw() {
            GL11.glColor3f(1, 0, 0);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            {

                GL11.glVertex2d(a.x, a.y);
                GL11.glVertex2d(b.x, b.y);
            }
            GL11.glEnd();
        }

        @Override
        public String toString() {
            return "Line{" +
                    "a=" + a +
                    ", b=" + b +
                    '}';
        }
    }

    private interface DrawFunction {
        void draw();
    }

}