package ru.vsu.cs.bocharovss.cg22;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

public class SectorComparison {

    public static void drawComparison(
            GraphicsContext gc,
            int x, int y, int radius,
            double startAngle, double endAngle,
            Color centerColor, Color edgeColor,
            String testName) {

        int padding = 20;
        int sectorWidth = radius * 2 + padding * 2;
        int totalWidth = sectorWidth * 2 + padding;

        gc.clearRect(x - padding, y - radius - padding, totalWidth, radius * 2 + padding * 2);

        SectorRenderer.drawCircleSector(gc, x, y, radius, startAngle, endAngle, centerColor, edgeColor);

        drawJavaFXSector(gc, x + sectorWidth + padding, y, radius, startAngle, endAngle, centerColor, edgeColor);

        drawLabels(gc, x, y, radius, sectorWidth, padding, testName, startAngle, endAngle);
    }

    private static void drawJavaFXSector(
            GraphicsContext gc,
            int centerX, int centerY, int radius,
            double startAngle, double endAngle,
            Color centerColor, Color edgeColor) {

        RadialGradient gradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, centerColor),
                new Stop(1, edgeColor)
        );

        gc.setFill(gradient);
        gc.fillArc(centerX - radius, centerY - radius,
                radius * 2, radius * 2,
                startAngle, endAngle - startAngle,
                javafx.scene.shape.ArcType.ROUND);
    }

    private static void drawLabels(
            GraphicsContext gc,
            int x, int y, int radius, int sectorWidth, int padding,
            String testName, double startAngle, double endAngle) {

        gc.setFill(Color.BLACK);

        String angleInfo = String.format("%.0f°-%.0f°", startAngle, endAngle);
        gc.fillText(testName + " " + angleInfo, x - radius, y - radius - 5);
    }

    public static void drawSingleComparison(
            GraphicsContext gc,
            int x, int y, int radius,
            double startAngle, double endAngle,
            Color centerColor, Color edgeColor) {

        int padding = 20;
        int sectorWidth = radius * 2 + padding * 2;

        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        SectorRenderer.drawCircleSector(gc, x, y, radius, startAngle, endAngle, centerColor, edgeColor);
        drawJavaFXSector(gc, x + sectorWidth + padding, y, radius, startAngle, endAngle, centerColor, edgeColor);

        gc.setFill(Color.BLACK);
        String angleInfo = String.format("Наш алгоритм (%.0f°-%.0f°)", startAngle, endAngle);
        gc.fillText(angleInfo, x - radius, y - radius - 5);

        String javaFXInfo = String.format("JavaFX (%.0f°-%.0f°)", startAngle, endAngle);
        gc.fillText(javaFXInfo, x + sectorWidth + padding - radius, y - radius - 5);
    }

    public static void runAllTests(GraphicsContext gc) {
        System.out.println("=== ЗАПУСК ВСЕХ ТЕСТОВ СРАВНЕНИЯ ===");

        int startX = 150;
        int startY = 120;
        int radius = 50;
        int verticalSpacing = 150;
        int horizontalSpacing = 600;

        drawComparison(gc, startX, startY, radius, 0, 90,
                Color.WHITE, Color.RED, "1-я четверть");

        drawComparison(gc, startX, startY + verticalSpacing, radius, 90, 180,
                Color.CYAN, Color.BLUE, "2-я четверть");

        drawComparison(gc, startX, startY + verticalSpacing * 2, radius, 180, 270,
                Color.YELLOW, Color.GREEN, "3-я четверть");

        drawComparison(gc, startX, startY + verticalSpacing * 3, radius, 270, 360,
                Color.PINK, Color.PURPLE, "4-я четверть");

        drawComparison(gc, startX + horizontalSpacing, startY, radius, 0, 180,
                Color.LIGHTYELLOW, Color.ORANGE, "Полукруг");

        drawComparison(gc, startX + horizontalSpacing, startY + verticalSpacing, radius, 0, 270,
                Color.LIGHTCYAN, Color.DARKBLUE, "3/4 окружности");

        drawComparison(gc, startX + horizontalSpacing, startY + verticalSpacing * 2, radius, 0, 360,
                Color.LIGHTGRAY, Color.BLACK, "Полный круг");

        drawComparison(gc, startX, startY + verticalSpacing * 4, radius, 45, 315,
                Color.LIGHTGREEN, Color.DARKGREEN, "Пакман вправо");

        drawComparison(gc, startX + horizontalSpacing, startY + verticalSpacing * 3, radius, 0, 1,
                Color.WHITE, Color.BLUE, "Маленький 1°");

        drawComparison(gc, startX + horizontalSpacing, startY + verticalSpacing * 4, radius, 0, 359,
                Color.WHITE, Color.RED, "Почти полный круг");

        System.out.println("=== ВСЕ ТЕСТЫ ЗАВЕРШЕНЫ ===");
    }

}