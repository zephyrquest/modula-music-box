package com.github.zephyrquest.modulamusicbox.views;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public abstract class BaseView {
    protected final Stage stage;
    protected final Scene scene;
    protected  BorderPane borderPane;


    public BaseView(Stage stage, Scene scene, BorderPane borderPane) {
        this.stage = stage;
        this.borderPane = borderPane;
        this.scene = scene;
    }

    public abstract void show();
}
