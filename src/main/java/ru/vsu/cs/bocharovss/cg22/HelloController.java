package ru.vsu.cs.bocharovss.cg22;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;

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
    protected void onCustomSectorButtonClick() {
        CustomSectorDialog dialog = new CustomSectorDialog();
        boolean confirmed = dialog.showDialog(canvas.getScene().getWindow());

        if (confirmed) {
            CustomSectorDialog.SectorParameters params = dialog.getParameters();

            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            SectorRenderer.drawCircleSector(
                    canvas.getGraphicsContext2D(),
                    params.centerX, params.centerY, params.radius,
                    params.startAngle, params.endAngle,
                    params.centerColor, params.edgeColor
            );

            welcomeText.setText(String.format(
                    "Пользовательский сектор: (%d,%d) R=%d, углы %.1f°-%.1f°",
                    params.centerX, params.centerY, params.radius,
                    params.startAngle, params.endAngle
            ));
        }
    }
}