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

        System.out.println("Drawing circle sector with Bresenham algorithm");
        System.out.println("Center: (" + centerX + "," + centerY + "), radius: " + radius);
        System.out.println("Sector angles: " + startAngle + "° to " + endAngle + "°");

        PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        double normalizedStartAngle = normalizeAngle(startAngle);
        double normalizedEndAngle = normalizeAngle(endAngle);

        if (Math.abs(normalizedEndAngle - normalizedStartAngle - 360) > 1e-10) {
            if(normalizedStartAngle == 0 && (normalizedEndAngle == 359 || normalizedEndAngle == 1 || normalizedEndAngle == 0)){
                normalizedEndAngle -= 1.0;
            }

        }

        boolean isFullCircle = Math.abs(normalizedEndAngle - normalizedStartAngle - 360) < 1e-10 ||
                (Math.abs(normalizedStartAngle - normalizedEndAngle) < 1e-10);

        System.out.println("Normalized angles: " + normalizedStartAngle + "° to " + normalizedEndAngle + "°");
        System.out.println("Is full circle: " + isFullCircle);

        drawBresenhamSector(pixelWriter, centerX, centerY, radius,
                normalizedStartAngle, normalizedEndAngle, centerColor, edgeColor);

        System.out.println("Sector drawing completed");
    }

    private static void drawBresenhamSector(
            PixelWriter pixelWriter,
            int centerX, int centerY, int radius,
            double startAngle, double endAngle,
            Color centerColor, Color edgeColor) {

        double startRad = Math.toRadians(startAngle);
        double endRad = Math.toRadians(endAngle);

        for (int scanY = -radius; scanY <= radius; scanY++) {
            int xLeft = Integer.MAX_VALUE;
            int xRight = Integer.MIN_VALUE;

            for (int scanX = -radius; scanX <= radius; scanX++) {
                if (scanX * scanX + scanY * scanY <= radius * radius) {
                    if (isPointInSector(scanX, -scanY, startRad, endRad)) {
                        xLeft = Math.min(xLeft, scanX);
                        xRight = Math.max(xRight, scanX);
                    }
                }
            }

            if (xLeft <= xRight) {
                for (int x = xLeft; x <= xRight; x++) {
                    double distance = Math.sqrt(x * x + scanY * scanY);
                    double t = Math.min(1.0, distance / radius);
                    Color interpolatedColor = interpolateColor(centerColor, edgeColor, t);
                    putPixel(pixelWriter, centerX + x, centerY + scanY, interpolatedColor);
                }
            }
        }
    }

    private static void drawSectorOctant(
            PixelWriter pixelWriter,
            int cx, int cy, int x, int y,
            double startRad, double endRad,
            Color centerColor, Color edgeColor, int maxRadius) {

        int[][] points = {
                {x, y}, {y, x}, {y, -x}, {x, -y},
                {-x, -y}, {-y, -x}, {-y, x}, {-x, y}
        };

        for (int[] point : points) {
            int px = point[0];
            int py = point[1];

            if (isPointInSector(px, -py, startRad, endRad)) {
                drawFilledLine(pixelWriter, cx, cy, px, py, startRad, endRad,
                        centerColor, edgeColor, maxRadius);
            }
        }
    }

    private static void drawFilledLine(
            PixelWriter pixelWriter,
            int centerX, int centerY, int dx, int dy,
            double startRad, double endRad,
            Color centerColor, Color edgeColor, int maxRadius) {

        // Используем алгоритм Брезенхема для линии от центра к точке на окружности
        int x0 = 0, y0 = 0;
        int x1 = dx, y1 = dy;

        int dxVal = Math.abs(x1 - x0);
        int dyVal = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dxVal - dyVal;

        int x = x0;
        int y = y0;

        while (true) {
            int currentX = centerX + x;
            int currentY = centerY + y;

            if (isPointInSector(x, -y, startRad, endRad)) {
                double distance = Math.sqrt(x * x + y * y);
                double t = Math.min(1.0, distance / maxRadius);
                Color interpolatedColor = interpolateColor(centerColor, edgeColor, t);
                putPixel(pixelWriter, currentX, currentY, interpolatedColor);
            }

            if (x == x1 && y == y1) break;

            int e2 = 2 * err;
            if (e2 > -dyVal) {
                err -= dyVal;
                x += sx;
            }
            if (e2 < dxVal) {
                err += dxVal;
                y += sy;
            }
        }
    }

    private static void drawRadialLine(
            PixelWriter pixelWriter,
            int centerX, int centerY, int dx, int dy,
            Color centerColor, Color edgeColor, int maxRadius) {

        int x0 = 0, y0 = 0;
        int x1 = dx, y1 = dy;

        int dxVal = Math.abs(x1 - x0);
        int dyVal = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dxVal - dyVal;

        int x = x0;
        int y = y0;

        while (true) {
            int currentX = centerX + x;
            int currentY = centerY + y;

            double distance = Math.sqrt(x * x + y * y);
            double t = Math.min(1.0, distance / maxRadius);
            Color interpolatedColor = interpolateColor(centerColor, edgeColor, t);

            putPixel(pixelWriter, currentX, currentY, interpolatedColor);

            if (x == x1 && y == y1) break;

            int e2 = 2 * err;
            if (e2 > -dyVal) {
                err -= dyVal;
                x += sx;
            }
            if (e2 < dxVal) {
                err += dxVal;
                y += sy;
            }
        }
    }

    private static boolean isPointInSector(int dx, int dy, double startRad, double endRad) {
        if (dx == 0 && dy == 0) {
            return true;
        }

        double angle = Math.atan2(dy, dx);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }

        if (Math.abs(endRad - startRad - 2 * Math.PI) < 1e-10) {
            return true;
        }

        double normalizedStart = startRad;
        double normalizedEnd = endRad;

        if (normalizedEnd < normalizedStart) {
            normalizedEnd += 2 * Math.PI;
        }

        double normalizedAngle = angle;
        if (normalizedAngle < normalizedStart) {
            normalizedAngle += 2 * Math.PI;
        }

        final double EPSILON = 1e-10;
        return (normalizedAngle >= normalizedStart - EPSILON &&
                normalizedAngle <= normalizedEnd + EPSILON);
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
        angle = angle % 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    private static void putPixel(PixelWriter pixelWriter, int x, int y, Color color) {
        if (x >= 0 && x < 1200 && y >= 0 && y < 800) {
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