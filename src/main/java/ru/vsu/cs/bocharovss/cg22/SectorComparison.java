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
        gc.fillText("Наш алгоритм", x - radius, y + radius + 20);
        gc.fillText("JavaFX", x + sectorWidth + padding - radius, y + radius + 20);

        String angleInfo = String.format("%.0f°-%.0f°", startAngle, endAngle);
        gc.fillText(testName + " " + angleInfo, x - radius, y - radius - 5);
    }

    public static void runAllTests(GraphicsContext gc) {
        System.out.println("=== ЗАПУСК ВСЕХ ТЕСТОВ СРАВНЕНИЯ ===");

        int startX = 150;
        int startY = 120;
        int radius = 40;
        int verticalSpacing = 150;
        int horizontalSpacing = 600;

        drawComparison(gc, startX, startY, radius, 0, 90,
                Color.WHITE, Color.RED, "Первая четверть");

        drawComparison(gc, startX, startY + verticalSpacing, radius, 0, 180,
                Color.CYAN, Color.BLUE, "Полукруг");

        drawComparison(gc, startX, startY + verticalSpacing * 2, radius, 0, 270,
                Color.YELLOW, Color.GREEN, "Три четверти");

        drawComparison(gc, startX, startY + verticalSpacing * 3, radius, 0, 360,
                Color.PINK, Color.PURPLE, "Полный круг");

        drawComparison(gc, startX + horizontalSpacing, startY, radius, 90, 300,
                Color.LIGHTYELLOW, Color.ORANGE, "Большой сектор");

        drawComparison(gc, startX + horizontalSpacing, startY + verticalSpacing, radius, 300, 60,
                Color.LIGHTCYAN, Color.DARKBLUE, "Пересекающий 0°");

        drawComparison(gc, startX + horizontalSpacing, startY + verticalSpacing * 2, radius, 0, 1,
                Color.LIGHTGRAY, Color.BLACK, "Маленький сектор");

        drawComparison(gc, startX + horizontalSpacing, startY + verticalSpacing * 3, radius, 0, 359,
                Color.LIGHTGREEN, Color.DARKGREEN, "Почти полный круг");

        System.out.println("=== ВСЕ ТЕСТЫ ЗАВЕРШЕНЫ ===");
    }

    public static void drawSingleTest(
            GraphicsContext gc,
            int x, int y, int radius,
            double startAngle, double endAngle,
            Color centerColor, Color edgeColor,
            String testName) {

        int padding = 50;
        int sectorWidth = radius * 2 + padding * 2;
        int totalWidth = sectorWidth * 2 + padding;

        gc.clearRect(x - padding, y - radius - padding, totalWidth, radius * 2 + padding * 2);

        SectorRenderer.drawCircleSector(gc, x, y, radius, startAngle, endAngle, centerColor, edgeColor);

        drawJavaFXSector(gc, x + sectorWidth + padding, y, radius, startAngle, endAngle, centerColor, edgeColor);

        gc.setFill(Color.BLACK);
        gc.fillText("Наш алгоритм", x - radius, y + radius + 25);
        gc.fillText("JavaFX", x + sectorWidth + padding - radius, y + radius + 25);

        String angleInfo = String.format("%.0f°-%.0f°", startAngle, endAngle);
        gc.fillText(testName + " " + angleInfo, x - radius, y - radius - 10);
    }
}