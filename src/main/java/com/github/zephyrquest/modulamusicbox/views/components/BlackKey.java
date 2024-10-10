package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.paint.Color;

public class BlackKey extends Key {

    public BlackKey(String note, double width, double height) {
        super(note, width, height);

        this.getStyleClass().addAll("key", "black-key");
    }

    @Override
    public void release() {
        this.setFill(Color.BLACK);
    }
}
