package com.github.zephyrquest.modulamusicbox.views;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GrooveBoxView extends BaseView {
    public GrooveBoxView(Stage stage, Scene scene, BorderPane borderPane) {
        super(stage, scene, borderPane);
    }

    @Override
    public void show() {
        Label label = new Label("Groove Box View");
        borderPane.setCenter(label);
    }
}
