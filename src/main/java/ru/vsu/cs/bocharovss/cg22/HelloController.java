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
        welcomeText.setText("Testing FILLED sectors with COLOR INTERPOLATION...");

        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        System.out.println("Button clicked! Testing sectors with color gradients...");

        //От белого к красному (0°-90°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 160, 130, 100, 0, 90, Color.WHITE, Color.RED);

        //От голубого к синему (90°-180°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 160, 130, 100, 90, 180, Color.CYAN, Color.BLUE);

        //От желтого к зеленому (180°-270°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 160, 130, 100, 180, 270, Color.YELLOW, Color.GREEN);

        //От розового к фиолетовому (270°-360°)
        SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 160, 130, 100, 270, 360, Color.PINK, Color.PURPLE);

        // Тест 1
        //SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 160, 140, 35, 0, 1, Color.LIGHTYELLOW, Color.ORANGE);

        // Тест 2
        //SectorRenderer.drawCircleSector(canvas.getGraphicsContext2D(), 240, 140, 35, 0, 359, Color.LIGHTGRAY, Color.DARKBLUE);

        System.out.println("All gradient sectors drawing completed");
    }
}