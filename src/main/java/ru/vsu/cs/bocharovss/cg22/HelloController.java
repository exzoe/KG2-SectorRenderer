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
        welcomeText.setText("Сравнение нашего алгоритма с JavaFX");

        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        System.out.println("=== НАЧАЛО ТЕСТИРОВАНИЯ ===");

        SectorComparison.runAllTests(canvas.getGraphicsContext2D());

        System.out.println("=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===");
    }

    @FXML
    protected void onTestSingleButtonClick() {
        welcomeText.setText("Тестирование отдельного сектора");

        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        SectorComparison.drawSingleTest(
                canvas.getGraphicsContext2D(),
                350, 300, 60, 45, 120,
                Color.WHITE, Color.RED, "Тестовый сектор"
        );
    }
}