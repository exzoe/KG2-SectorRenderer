package ru.vsu.cs.bocharovss.cg22;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class SectorRenderer {

    public static void drawCircleSector(
            GraphicsContext graphicsContext,
            int centerX, int centerY, int radius,
            double startAngle, double endAngle,
            Color centerColor, Color edgeColor) {

        System.out.println("Drawing FILLED circle sector with Bresenham algorithm");
        System.out.println("Center: (" + centerX + "," + centerY + "), radius: " + radius);
        System.out.println("Sector angles: " + startAngle + "° to " + endAngle + "°");

        drawFilledBresenhamSector(graphicsContext, centerX, centerY, radius, startAngle, endAngle, centerColor, edgeColor);
    }

    private static void drawFilledBresenhamSector(
            GraphicsContext gc,
            int centerX, int centerY, int radius,
            double startAngle, double endAngle,
            Color centerColor, Color edgeColor) {

        PixelWriter pixelWriter = gc.getPixelWriter();

        double normalizedStartAngle = normalizeAngle(startAngle);
        double normalizedEndAngle = normalizeAngle(endAngle);

        double startRad = Math.toRadians(normalizedStartAngle);
        double endRad = Math.toRadians(normalizedEndAngle);

        System.out.println("Normalized angles: " + normalizedStartAngle + "° to " + normalizedEndAngle + "°");
        System.out.println("Sector size: " + (normalizedEndAngle - normalizedStartAngle) + "°");

        int minX = centerX - radius;
        int maxX = centerX + radius;
        int minY = centerY - radius;
        int maxY = centerY + radius;

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                int dx = x - centerX;
                int dy = y - centerY;

                if (dx * dx + dy * dy <= radius * radius) {
                    if (isPointInSector(dx, -dy, startRad, endRad)) {
                        double distance = Math.sqrt(dx * dx + dy * dy);
                        double t = Math.min(1.0, distance / radius);
                        Color interpolatedColor = interpolateColor(centerColor, edgeColor, t);
                        putPixel(pixelWriter, x, y, interpolatedColor);
                    }
                }
            }
        }

        System.out.println("Filled sector drawing completed");
    }

    private static boolean isPointInSector(int dx, int dy, double startRad, double endRad) {
        double angle = Math.atan2(dy, dx);

        if (angle < 0) {
            angle += 2 * Math.PI;
        }

        double normalizedStart = startRad;
        double normalizedEnd = endRad;

        if (normalizedEnd < normalizedStart) {
            normalizedEnd += 2 * Math.PI;
        }

        double normalizedAngle = angle;
        if (angle < normalizedStart) {
            normalizedAngle += 2 * Math.PI;
        }

        return (normalizedAngle >= normalizedStart && normalizedAngle <= normalizedEnd);
    }

    private static Color interpolateColor(Color startColor, Color endColor, double t) {
        t = Math.max(0, Math.min(1, t));

        double red = startColor.getRed() + (endColor.getRed() - startColor.getRed()) * t;
        double green = startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * t;
        double blue = startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * t;
        double alpha = startColor.getOpacity() + (endColor.getOpacity() - startColor.getOpacity()) * t;

        return new Color(red, green, blue, alpha);
    }

    private static double normalizeAngle(double angle) {
        return ((angle % 360) + 360) % 360;
    }

    private static void putPixel(PixelWriter pixelWriter, int x, int y, Color color) {
        if (x >= 0 && x < 800 && y >= 0 && y < 600) {
            pixelWriter.setColor(x, y, color);
        }
    }

    // Метод для обратной совместимости
    public static void drawCircleSector(
            GraphicsContext graphicsContext,
            int centerX, int centerY, int radius,
            double startAngle, double endAngle,
            Color color) {
        drawCircleSector(graphicsContext, centerX, centerY, radius, startAngle, endAngle, color, color);
    }
}