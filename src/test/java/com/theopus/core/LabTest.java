package com.theopus.core;

import com.theopus.core.modules.configs.WindowConfig;
import com.theopus.core.base.window.WindowManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.Test;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LabTest {

    private float min = -1;
    private float max = 1;
    private ThreadLocalRandom rand = ThreadLocalRandom.current();

    private List<Line> lines = new ArrayList<>();

    @Test
    public void run() {
        WindowManager manager = new WindowManager(new WindowConfig(
                600, 400, new Vector4f(0, 0, 0, 1), true,
                60), (window, key, scancode, action, mods) -> {
            if (action != GLFW.GLFW_RELEASE) {
                lines.add(generateLine());
            }
        });
        manager.createWindow();
        manager.showWindow();

//        lines.addAll(IntStream.range(0, 100).mapToObj(value -> generateLine()).collect(Collectors.toList()));

        lines.forEach(System.out::println);
        while (!manager.windowShouldClose()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            //rect
            lines.forEach(this::drawLine);
            lines.add(generateLine());
            drawRectangle(new Vector2f(-0.5f, 0.5f), 1f, new Vector3f(1, 0, 0));

            manager.update();
        }

    }


    private void drawRectangle(Vector2f point, float sideLength, Vector3f color) {
        GL11.glColor3f(color.x, color.y, color.z);
        GL11.glLineWidth(5f);

        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            GL11.glVertex2d(point.x, point.y);
            GL11.glVertex2d(point.x, point.y - sideLength);
            GL11.glVertex2d(point.x + sideLength, point.y - sideLength);
            GL11.glVertex2d(point.x + sideLength, point.y);
            GL11.glVertex2d(point.x, point.y);
        }
        GL11.glEnd();

        GL11.glLineWidth(1f);
    }

    private void drawLine(Line line){
        GL11.glColor3f(line.color.x, line.color.y, line.color.z);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glLineWidth(1f);
        {

            GL11.glVertex2d(line.positionA.x, line.positionA.y);
            GL11.glVertex2d(line.positionB.x, line.positionB.y);
        }
        GL11.glEnd();
    }


    private Line generateLine() {
        Vector2f a = generateCoord();
        Vector2f b = generateCoord();

        Vector3f color = new Vector3f(
                rand.nextFloat(),
                rand.nextFloat(),
                rand.nextFloat()
                );

        return new Line(
                a,
                b,
                color
        );
    }

    private Vector2f generateCoord(){
        float x = (float) ((Math.random())*(max-min)+min);
        float y = (float) ((Math.random())*(max-min)+min);
        return new Vector2f(
                rand.nextBoolean() ? x * 1 : x * -1,
                rand.nextBoolean() ? y * 1 : y * -1
        );
    }


    private interface DrawFunction {
        void draw();
    }

    private static class Line {
        Vector2f positionA;
        Vector2f positionB;
        Vector3f color;

        public Line(Vector2f positionA, Vector2f positionB, Vector3f color) {
            this.positionA = positionA;
            this.positionB = positionB;
            this.color = color;
        }

        @Override
        public String toString() {
            return "Line{" +
                    "positionA=" + positionA +
                    ", positionB=" + positionB +
                    ", color=" + color +
                    '}';
        }
    }
}