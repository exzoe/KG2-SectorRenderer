package ru.vsu.cs.bocharovss.cg22;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CustomSectorDialog {

    private TextField centerXField;
    private TextField centerYField;
    private TextField radiusField;
    private TextField startAngleField;
    private TextField endAngleField;
    private ColorPicker centerColorPicker;
    private ColorPicker edgeColorPicker;

    private boolean confirmed = false;
    private SectorParameters parameters;

    public static class SectorParameters {
        public int centerX;
        public int centerY;
        public int radius;
        public double startAngle;
        public double endAngle;
        public Color centerColor;
        public Color edgeColor;

        public SectorParameters(int centerX, int centerY, int radius,
                                double startAngle, double endAngle,
                                Color centerColor, Color edgeColor) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
            this.startAngle = startAngle;
            this.endAngle = endAngle;
            this.centerColor = centerColor;
            this.edgeColor = edgeColor;
        }
    }

    public boolean showDialog(Window owner) {
        Stage dialog = new Stage();
        dialog.setTitle("Создать пользовательский сектор");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner);
        dialog.setResizable(false);

        centerXField = new TextField("400");
        centerYField = new TextField("400");
        radiusField = new TextField("100");
        startAngleField = new TextField("0");
        endAngleField = new TextField("90");
        centerColorPicker = new ColorPicker(Color.WHITE);
        edgeColorPicker = new ColorPicker(Color.BLUE);

        addNumberValidation(centerXField);
        addNumberValidation(centerYField);
        addNumberValidation(radiusField);
        addNumberValidation(startAngleField);
        addNumberValidation(endAngleField);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 20));

        grid.add(new Label("Центр X:"), 0, 0);
        grid.add(centerXField, 1, 0);
        grid.add(new Label("Центр Y:"), 0, 1);
        grid.add(centerYField, 1, 1);
        grid.add(new Label("Радиус:"), 0, 2);
        grid.add(radiusField, 1, 2);
        grid.add(new Label("Начальный угол (°):"), 0, 3);
        grid.add(startAngleField, 1, 3);
        grid.add(new Label("Конечный угол (°):"), 0, 4);
        grid.add(endAngleField, 1, 4);
        grid.add(new Label("Цвет центра:"), 0, 5);
        grid.add(centerColorPicker, 1, 5);
        grid.add(new Label("Цвет края:"), 0, 6);
        grid.add(edgeColorPicker, 1, 6);

        Button okButton = new Button("Создать");
        Button cancelButton = new Button("Отмена");

        okButton.setOnAction(e -> {
            if (validateInput()) {
                confirmed = true;
                parameters = new SectorParameters(
                        Integer.parseInt(centerXField.getText()),
                        Integer.parseInt(centerYField.getText()),
                        Integer.parseInt(radiusField.getText()),
                        Double.parseDouble(startAngleField.getText()),
                        Double.parseDouble(endAngleField.getText()),
                        centerColorPicker.getValue(),
                        edgeColorPicker.getValue()
                );
                dialog.close();
            }
        });

        cancelButton.setOnAction(e -> {
            confirmed = false;
            dialog.close();
        });

        HBox buttonBox = new HBox(10, okButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 20, 20, 20));

        VBox content = new VBox(10, grid, buttonBox);

        Scene scene = new Scene(content);
        dialog.setScene(scene);
        dialog.showAndWait();

        return confirmed;
    }

    private void addNumberValidation(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
                field.setText(newValue.replaceAll("[^-\\d.]", ""));
            }
        });
    }

    private boolean validateInput() {
        try {
            int centerX = Integer.parseInt(centerXField.getText());
            int centerY = Integer.parseInt(centerYField.getText());
            int radius = Integer.parseInt(radiusField.getText());
            double startAngle = Double.parseDouble(startAngleField.getText());
            double endAngle = Double.parseDouble(endAngleField.getText());

            if (radius <= 0) {
                showError("Радиус должен быть положительным числом");
                return false;
            }

            if (startAngle < 0 || startAngle >= 360 || endAngle < 0 || endAngle > 360) {
                showError("Углы должны быть в диапазоне от 0 до 360 градусов");
                return false;
            }

            return true;

        } catch (NumberFormatException e) {
            showError("Пожалуйста, введите корректные числовые значения");
            return false;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка ввода");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public SectorParameters getParameters() {
        return parameters;
    }
}