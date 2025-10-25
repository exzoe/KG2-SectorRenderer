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
        welcomeText.setText("Testing circle sector...");

        System.out.println("Button clicked! Attempting to draw...");
        System.out.println("Canvas: " + canvas);
        System.out.println("GraphicsContext: " + canvas.getGraphicsContext2D());
        System.out.println("PixelWriter: " + canvas.getGraphicsContext2D().getPixelWriter());

        SectorRenderer.drawCircleSector(
                canvas.getGraphicsContext2D(),
                160, 100, 50,
                0, 90,
                Color.RED
        );

        System.out.println("Drawing completed");
    }

}