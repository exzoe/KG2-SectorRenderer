package ru.vsu.cs.bocharovss.cg22;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class SectorRenderer {

    // Новый метод с интерполяцией цвета
    public static void drawCircleSector(
            GraphicsContext graphicsContext,
            int centerX, int centerY, int radius,
            double startAngle, double endAngle,
            Color centerColor, Color edgeColor) {

        System.out.println("Drawing FILLED circle sector with color interpolation");
        System.out.println("Center: (" + centerX + "," + centerY + "), radius: " + radius);
        System.out.println("Sector angles: " + startAngle + "° to " + endAngle + "°");
        System.out.println("Colors: center=" + centerColor + ", edge=" + edgeColor);

        drawFilledSectorWithGradient(graphicsContext, centerX, centerY, radius, startAngle, endAngle, centerColor, edgeColor);
    }

    // Старый метод для обратной совместимости
    public static void drawCircleSector(
            GraphicsContext graphicsContext,
            int centerX, int centerY, int radius,
            double startAngle, double endAngle,
            Color color) {

        drawCircleSector(graphicsContext, centerX, centerY, radius, startAngle, endAngle, color, color);
    }

    private static void drawFilledSectorWithGradient(
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

                        double t = distance / radius;
                        Color interpolatedColor = interpolateColor(centerColor, edgeColor, t);

                        putPixel(pixelWriter, x, y, interpolatedColor);
                    }
                }
            }
        }

        System.out.println("Filled sector with gradient drawing completed");
    }

    private static Color interpolateColor(Color startColor, Color endColor, double t) {
        t = Math.max(0, Math.min(1, t));

        double red = startColor.getRed() + (endColor.getRed() - startColor.getRed()) * t;
        double green = startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * t;
        double blue = startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * t;
        double alpha = startColor.getOpacity() + (endColor.getOpacity() - startColor.getOpacity()) * t;

        return new Color(red, green, blue, alpha);
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

        if (angle >= normalizedStart && angle <= normalizedEnd) {
            return true;
        }

        if (angle + 2 * Math.PI >= normalizedStart && angle + 2 * Math.PI <= normalizedEnd) {
            return true;
        }

        return false;
    }

    private static double normalizeAngle(double angle) {
        return ((angle % 360) + 360) % 360;
    }

    private static void putPixel(PixelWriter pixelWriter, int x, int y, Color color) {
        if (x >= 0 && x < 400 && y >= 0 && y < 300) {
            pixelWriter.setColor(x, y, color);
        }
    }
}