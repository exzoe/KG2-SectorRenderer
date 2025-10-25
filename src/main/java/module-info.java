module ru.vsu.cs.bocharovss.cg22 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.vsu.cs.bocharovss.cg22 to javafx.fxml;
    exports ru.vsu.cs.bocharovss.cg22;
}