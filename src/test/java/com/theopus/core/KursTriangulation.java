package com.theopus.core;

import com.theopus.core.base.window.KeyListener;
import com.theopus.core.base.window.WindowManager;
import com.theopus.core.modules.configs.WindowConfig;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class KursTriangulation {

    private static class Line {
        Point a;
        Point b;

        public Line(Point a, Point b) {
            this.a = a;
            this.b = b;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Line line = (Line) o;

            if (this.a.equals(line.a) && this.b.equals(line.b)) return true;
            return this.a.equals(line.b) && this.b.equals(line.a);

        }

        @Override
        public int hashCode() {
            return a.hashCode() + b.hashCode();
        }

        double distance() {
            return Point.distance(a, b);
        }

        public static Comparator<Line> comparator = (o1, o2) -> (int) (Point.distance(o1.a, o1.b) - Point.distance(o2.a, o2.b));

        @Override
        public String toString() {
            return "Line{" +
                    "a=" + a +
                    ", b=" + b +
                    '}';
        }
    }


    private static class Point {
        float x, y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        static double distance(Point p0, Point p1) {
            return Math.sqrt(Math.pow(p1.x - p0.x, 2) + Math.pow(p1.y - p0.y, 2));
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public static void main(String[] args) {


        Point[] points = {
                new Point(-0.9f, -0.9f),
                new Point(-0.9f, 0.5f),
                new Point(0f, 0.3f),
                new Point(0.2f, 0f),
                new Point(-0.2f, -0.3f),
                new Point(0f, -0.6f),
        };

        List<Line> lines = new ArrayList<>();

        WindowManager manager = new WindowManager(new WindowConfig(
                600, 400, new Vector4f(0, 0, 0, 1), true,
                0), new KeyListener() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                lines.clear();
                lines.addAll(triangulate(points));
            }
        });

        manager.createWindow();
        manager.showWindow();


        System.out.println("------------------");

        while (!manager.windowShouldClose()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            drawLines(lines);
            manager.update();
        }

    }

    private static List<Line> triangulate(Point[] points) {

        List<Line> toDraw = new ArrayList<>();

        List<Line> triangulate = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                Point pointi = points[i];
                Point pointj = points[j];
                Line line = new Line(pointi, pointj);
                if (line.distance() != 0) {
                    if (!triangulate.contains(line)) {
                        triangulate.add(line);
                    }
                }
            }
        }
        System.out.println(points.length);
        System.out.println(toDraw.size());


        System.out.println(findMin(triangulate));

        System.out.println("To:Draw \n");
        System.out.println(toDraw);

        triangulate.sort(new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                return (int) (o1.distance() - o2.distance());
            }
        });

        while (triangulate.size() != 0) {
            Line min = findMin(triangulate);
            if (!toDraw.contains(min)) {
                if (!intersects(min, toDraw)) {
                    toDraw.add(min);
                } else {
                    triangulate.remove(min);
                }
            } else {
                triangulate.remove(min);
            }
        }


        return toDraw;
    }

    private static List<Line> getLines(Point[] points) {
        List<Line> lines = new ArrayList<>();
        Point p0 = points[0];
        for (int i = 1; i < points.length; i++) {
            lines.add(new Line(p0, points[i]));
            p0 = points[i];
        }
        lines.add(new Line(p0, points[0]));
        return lines;
    }

    private static Line findMin(List<Line> lines) {
        Line min = new Line(new Point(Float.MAX_VALUE, Float.MAX_VALUE), new Point(Float.MIN_VALUE, Float.MIN_VALUE));
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            if (line.distance() < min.distance()) {
                min = line;
            }
        }
        return min;
    }

    private static boolean intersects(Line line, List<Line> lines) {
        for (Line line1 : lines) {

            if (line.equals(line1)) {
                continue;
            }
            if ((line.a.equals(line1.b) || line.a.equals(line1.a)) || (line.b.equals(line1.a) || line.b.equals(line1.b))) {
                continue;
            }

            boolean b = doIntersect(line.a, line.b, line1.a, line1.b);
            if (b) {
                System.out.println(line);
                System.out.println(line1);
                return b;
            }
        }
        return false;
    }


    private static void drawLines(List<Line> points) {
        glBegin(GL_LINES);
        points.forEach(l -> {
            glVertex3f(l.a.x, l.a.y, 0);
            glVertex3f(l.b.x, l.b.y, 0);
        });
        glEnd();
    }

    static boolean onSegment(Point p, Point q, Point r) {
        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y))
            return true;

        return false;
    }

    static int orientation(Point p, Point q, Point r) {
        float val = (q.y - p.y) * (r.x - q.x) -
                (q.x - p.x) * (r.y - q.y);

        if (val == 0) return 0;

        return (val > 0) ? 1 : 2;
    }

    static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4) return true;
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false;
    }


    static Point beetwenPoint(Line line) {
        Point a = line.a;
        Point b = line.b;

        float mx = a.x + 0.6f * ((b.x) - a.x);
        float my = a.y + 0.6f * ((b.y) - a.y);
        return new Point(mx, my);
    }

    static boolean isInside(Point polygon[], int n, Point p) {
        // There must be at least 3 vertices in polygon[]
        if (n < 3) return false;

        // Create a point for line segment from p to infinite
        Point extreme = new Point(Float.MAX_VALUE, p.y);

        // Count intersections of the above line with sides of polygon
        int count = 0, i = 0;
        do {
            int next = (i + 1) % n;

            // Check if the line segment from 'p' to 'extreme' intersects
            // with the line segment from 'polygon[i]' to 'polygon[next]'
            if (doIntersect(polygon[i], polygon[next], p, extreme)) {
                // If the point 'p' is colinear with line segment 'i-next',
                // then check if it lies on segment. If it lies, return true,
                // otherwise false
                if (orientation(polygon[i], p, polygon[next]) == 0)
                    return onSegment(polygon[i], p, polygon[next]);

                count++;
            }
            i = next;
        } while (i != 0);

        // Return true if count is odd, false otherwise
        return (count % 2 == 1);  // Same as
    }
}
