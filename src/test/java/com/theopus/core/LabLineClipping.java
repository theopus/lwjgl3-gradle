package com.theopus.core;

import com.theopus.core.base.window.WindowManager;
import com.theopus.core.modules.configs.WindowConfig;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.Test;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.ToIntFunction;

public class LabLineClipping {

    private float min = -1;
    private float max = 1;
    private ThreadLocalRandom rand = ThreadLocalRandom.current();

    private List<Line> lines = new ArrayList<>();

    @Test
    public void run() {

        Vector4f rectangle = new Vector4f(-0.5f, 0.5f, -0.5f, 0.5f);

        WindowManager manager = new WindowManager(new WindowConfig(
                600, 400, new Vector4f(0, 0, 0, 1), true,
                0), (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_1 & action != GLFW.GLFW_RELEASE) {
                lines.forEach(line -> mCoenThetherland(line, rectangle));
            }
            if (key == GLFW.GLFW_KEY_2 & action != GLFW.GLFW_RELEASE) {
                lines.forEach(line -> mLianBarski(line, rectangle));
            }
            if (key == GLFW.GLFW_KEY_SPACE & action != GLFW.GLFW_RELEASE) {
                lines.add(generateLine());
            }
            if (key == GLFW.GLFW_KEY_Q & action != GLFW.GLFW_RELEASE) {
                lines.clear();
            }

        });
        manager.createWindow();
        manager.showWindow();

        //lines.add(new Line(new Vector2f(-1, -1), new Vector2f(1, 1), new Vector3f(1, 0, 0)));

        while (!manager.windowShouldClose()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            lines.forEach(this::drawLine);
            drawRectangle(rectangle, new Vector3f(1, 0, 0));

            manager.update();
        }

    }

    private void mCoenThetherland(Line line, Vector4f rectangle) {

        int LEFT = 0b0001;
        int RIGHT = 0b0010;
        int TOP = 0b1000;
        int BOT = 0b0100;

        float xMin = rectangle.x;
        float xMax = rectangle.y;
        float yMin = rectangle.z;
        float yMax = rectangle.w;

        ToIntFunction<Vector2f> pointCode = p -> {
            int code = 0;

            if (p.x > xMax) {
                code += 2;
            } else if (p.x < xMin) {
                code += 1;
            }

            if (p.y > yMax) {
                code += 8;
            } else if (p.y < yMin) {
                code += 4;
            }

            return code;
        };

        Vector2f posA = line.positionA;
        Vector2f posB = line.positionB;

        int code = 0;
        Vector2f pos = null;
        int aCode = pointCode.applyAsInt(posA);
        int bCode = pointCode.applyAsInt(posB);

        while ((aCode | bCode) != 0) {

            if ((aCode & bCode) != 0) {
                makeInvisible(posA, posB);
                return;
            }

            if (aCode != 0) {
                code = aCode;
                pos = posA;
            } else if (bCode != 0) {
                code = bCode;
                pos = posB;
            }

            if ((code & LEFT) != 0) {
                pos.y += (posA.y - posB.y) * (xMin - pos.x) / (posA.x - posB.x);
                pos.x = xMin;
            } else if ((code & RIGHT) != 0) {
                pos.y += (posA.y - posB.y) * (xMax - pos.x) / (posA.x - posB.x);
                pos.x = xMax;
            } else if ((code & TOP) != 0) {
                pos.x += (posA.x - posB.x) * (yMax - pos.y) / (posA.y - posB.y);
                pos.y = yMax;
            } else if ((code & BOT) != 0) {
                pos.x += (posA.x - posB.x) * (yMin - pos.y) / (posA.y - posB.y);
                pos.y = yMin;
            }

            aCode = pointCode.applyAsInt(posA);
            bCode = pointCode.applyAsInt(posB);
        }
    }

    private void mLianBarski(Line line, Vector4f rectangle) {
        Vector2f posA = line.positionA;
        Vector2f posB = line.positionB;

        float xMin = rectangle.x;
        float xMax = rectangle.y;
        float yMin = rectangle.z;
        float yMax = rectangle.w;

        float x0 = line.positionA.x;
        float y0 = line.positionA.y;

        float x1 = line.positionB.x;
        float y1 = line.positionB.y;

        float dx = x1 - x0;
        float dy = y1 - y0;

        float pl = -dx;
        float pr = dx;
        float pt = -dy;
        float pb = dy;

        float ql = x0 - xMin;
        float qr = xMax - x0;
        float qt = y0 - yMin;
        float qb = yMax - y0;

        ArrayList<Float> pos = new ArrayList<>();
        ArrayList<Float> neg = new ArrayList<>();


        if ((pl == 0 && ql < 0) || (pt == 0 && qt < 0)) {
            makeInvisible(posA, posB);
            return;
        }

        if (pl != 0) {
            float rl = ql / pl;
            float rr = qr / pr;
            if (pl < 0) {
                neg.add(rl);
                pos.add(rr);
            } else {
                neg.add(rr);
                pos.add(rl);
            }
        }

        if (pt != 0) {
            float rt = qt / pt;
            float rb = qb / pb;

            if (pt < 0) {
                neg.add(rt);
                pos.add(rb);
            } else {
                neg.add(rb);
                pos.add(rt);
            }


        }

        float u1, u2;

        neg.add(0f);
        pos.add(1f);

        u1 = Collections.max(neg);
        u2 = Collections.min(pos);


        if (u1 > u2) {
            makeInvisible(posA, posB);
            return;
        }

        float nx0, ny0, nx1, ny1;

        nx0 = x0 + dx * u1;
        ny0 = y0 + dy * u1;

        nx1 = x0 + dx * u2;
        ny1 = y0 + dy * u2;


        line.positionA.x = nx0;
        line.positionA.y = ny0;

        line.positionB.x = nx1;
        line.positionB.y = ny1;
    }

    private void makeInvisible(Vector2f posA, Vector2f posB) {
        posA.x = -1;
        posA.y = -1;
        posB.x = -1;
        posB.y = -1;
    }


    private void drawRectangle(Vector4f rectangle, Vector3f color) {
        GL11.glColor3f(color.x, color.y, color.z);
        GL11.glLineWidth(5f);

        float xMin = rectangle.x;
        float xMax = rectangle.y;
        float yMin = rectangle.z;
        float yMax = rectangle.w;


        GL11.glBegin(GL11.GL_LINE_STRIP);
        {

            GL11.glVertex2d(xMin, yMin);
            GL11.glVertex2d(xMax, yMin);
            GL11.glVertex2d(xMax, yMax);
            GL11.glVertex2d(xMin, yMax);
            GL11.glVertex2d(xMin, yMin);
        }
        GL11.glEnd();

        GL11.glLineWidth(1f);
    }

    private void drawLine(Line line) {
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

    private Vector2f generateCoord() {
        float x = (float) ((Math.random()) * (max - min) + min);
        float y = (float) ((Math.random()) * (max - min) + min);
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