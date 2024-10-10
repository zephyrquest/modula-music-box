package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.paint.Color;

public class WhiteKey extends Key {

    public WhiteKey(String note, double width, double height) {
        super(note, width, height);

        this.getStyleClass().addAll("key", "white-key");
        this.setStrokeWidth(1.);
        this.setStroke(Color.BLACK);
    }

    @Override
    public void release() {
        this.setFill(Color.WHITE);
    }
}
