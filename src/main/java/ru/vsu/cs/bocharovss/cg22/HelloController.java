package ru.vsu.cs.bocharovss.cg22;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Canvas canvas;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Testing sectors...");

        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        System.out.println("Button clicked! Testing sectors...");

        // Первая четверть (0°-90°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 100, 80, 40, 0, 90, Color.WHITE, Color.RED);

        // Полукруг (0°-180°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 200, 80, 40, 0, 180, Color.CYAN, Color.BLUE);

        // Три четверти (0°-270°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 300, 80, 40, 0, 270, Color.YELLOW, Color.GREEN);

        // Полный круг (0°-360°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 400, 80, 40, 0, 360, Color.PINK, Color.PURPLE);

        // Большой сектор (90°-300°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 100, 200, 40, 90, 300, Color.LIGHTYELLOW, Color.ORANGE);

        // Пересекающий 0° (300°-60°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 200, 200, 40, 300, 60, Color.LIGHTCYAN, Color.DARKBLUE);

        // Очень маленький сектор (0°-1°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 300, 200, 40, 0, 1, Color.LIGHTGRAY, Color.BLACK);

        // Почти полный круг (0°-359°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 400, 200, 40, 0, 359, Color.LIGHTGREEN, Color.DARKGREEN);

        System.out.println("All FIXED sectors drawing completed");
    }
}