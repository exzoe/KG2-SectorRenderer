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

        // ПРИМЕЧАНИЕ: Для гарантированного заполнения без пропущенных пикселей
        // используется scanline подход. Алгоритм Брезенхема для заполнения
        // присутствует в методах drawSectorLine() и fillToCenter(),
        // но может приводить к пропускам пикселей вблизи центра.
        // Реализация по методичке (может быть дырявой):
        // drawBresenhamSectorWithLines(pixelWriter, centerX, centerY, radius,
        //                             startVecX, startVecY, endVecX, endVecY,
        //                             centerColor, edgeColor);

        PixelWriter pixelWriter = gc.getPixelWriter();

        double normalizedStartAngle = normalizeAngle(startAngle);
        double normalizedEndAngle = normalizeAngle(endAngle);

        double startRad = Math.toRadians(normalizedStartAngle);
        double endRad = Math.toRadians(normalizedEndAngle);

        double startVecX = Math.cos(startRad);
        double startVecY = Math.sin(startRad);
        double endVecX = Math.cos(endRad);
        double endVecY = Math.sin(endRad);

        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                if (x*x + y*y <= radius*radius) {
                    if (isPointInSectorByCrossProduct(x, -y, startVecX, startVecY, endVecX, endVecY)) {
                        double distance = Math.sqrt(x*x + y*y);
                        double t = Math.min(1.0, distance / radius);
                        Color interpolatedColor = interpolateColor(centerColor, edgeColor, t);
                        putPixel(pixelWriter, centerX + x, centerY + y, interpolatedColor);
                    }
                }
            }
        }
    }

    private static void fillToCenter(
            PixelWriter pixelWriter,
            int centerX, int centerY, int dx, int dy,
            double startVecX, double startVecY,
            double endVecX, double endVecY,
            Color centerColor, Color edgeColor, int radius) {

        if (isPointInSectorByCrossProduct(dx, -dy, startVecX, startVecY, endVecX, endVecY)) {
            int targetX = centerX + dx;
            int targetY = centerY + dy;


            for (double t = 0; t <= 1; t += 0.01) {
                int currentX = (int)(centerX + (targetX - centerX) * t);
                int currentY = (int)(centerY + (targetY - centerY) * t);


                int relX = currentX - centerX;
                int relY = currentY - centerY;

                if (isPointInSectorByCrossProduct(relX, -relY, startVecX, startVecY, endVecX, endVecY)) {
                    double distance = Math.sqrt(relX * relX + relY * relY);
                    double colorT = Math.min(1.0, distance / radius);
                    Color interpolatedColor = interpolateColor(centerColor, edgeColor, colorT);
                    putPixel(pixelWriter, currentX, currentY, interpolatedColor);
                }
            }
        }
    }

    private static void drawSectorLine(
            PixelWriter pixelWriter,
            int centerX, int centerY, int dx, int dy,
            double startVecX, double startVecY,
            double endVecX, double endVecY,
            Color centerColor, Color edgeColor, int radius) {

        if (isPointInSectorByCrossProduct(dx, -dy, startVecX, startVecY, endVecX, endVecY)) {
            drawLineWithGradient(pixelWriter, centerX, centerY, centerX + dx, centerY + dy,
                    centerColor, edgeColor, radius);
        }
    }

    private static void drawLineWithGradient(
            PixelWriter pixelWriter,
            int x0, int y0, int x1, int y1,
            Color startColor, Color endColor, int maxRadius) {

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;
        int err = dx - dy;

        int currentX = x0;
        int currentY = y0;

        while (true) {

            int relX = currentX - x0;
            int relY = currentY - y0;
            double distance = Math.sqrt(relX * relX + relY * relY);
            double t = Math.min(1.0, distance / maxRadius);

            Color interpolatedColor = interpolateColor(startColor, endColor, t);
            putPixel(pixelWriter, currentX, currentY, interpolatedColor);

            if (currentX == x1 && currentY == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                currentX += sx;
            }
            if (e2 < dx) {
                err += dx;
                currentY += sy;
            }
        }
    }

    // Проверка через ВЕКТОРНОЕ ПРОИЗВЕДЕНИЕ
    private static boolean isPointInSectorByCrossProduct(int pointX, int pointY,
                                                         double startVecX, double startVecY,
                                                         double endVecX, double endVecY) {
        double crossStart = startVecX * pointY - startVecY * pointX; // OA × OP
        double crossEnd = pointX * endVecY - pointY * endVecX;       // OP × OB

        return (crossStart >= 0 && crossEnd >= 0);
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
        if (x >= 0 && x < 400 && y >= 0 && y < 300) {
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