package ru.vsu.cs.bocharovss.cg22;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SectorRenderer {

    public static void drawCircleSector(
            GraphicsContext graphicsContext,
            int centerX, int centerY, int radius,
            double startAngle, double endAngle,
            Color color) {

        System.out.println("Drawing circle sector: center=(" + centerX + "," + centerY + "), radius=" + radius);
        System.out.println("Sector angles: " + startAngle + "° to " + endAngle + "°");

        drawBresenhamCircle(graphicsContext, centerX, centerY, radius, color);
    }

    private static void drawBresenhamCircle(
            GraphicsContext gc,
            int centerX, int centerY, int radius,
            Color color) {

        int x = 0;
        int y = radius;
        int error = 3 - 2 * radius;

        System.out.println("Starting Bresenham circle algorithm...");


        while (x <= y) {

            drawCirclePoints(gc, centerX, centerY, x, y, color);

            if (error < 0) {
                error = error + 4 * x + 6;
            } else {
                error = error + 4 * (x - y) + 10;
                y--;
            }
            x++;
        }

        System.out.println("Circle drawing completed");
    }

    private static void drawCirclePoints(
            GraphicsContext gc,
            int centerX, int centerY,
            int x, int y,
            Color color) {

        putPixel(gc, centerX + x, centerY + y, color);
        putPixel(gc, centerX - x, centerY + y, color);
        putPixel(gc, centerX + x, centerY - y, color);
        putPixel(gc, centerX - x, centerY - y, color);
        putPixel(gc, centerX + y, centerY + x, color);
        putPixel(gc, centerX - y, centerY + x, color);
        putPixel(gc, centerX + y, centerY - x, color);
        putPixel(gc, centerX - y, centerY - x, color);
    }

    private static void putPixel(GraphicsContext gc, int x, int y, Color color) {
        if (x >= 0 && x < 320 && y >= 0 && y < 200) { // Проверка границ Canvas
            gc.getPixelWriter().setColor(x, y, color);
        }
    }
}