package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Key extends Rectangle {
    private final String note;


    public Key(String note, double width, double height) {
        this.note = note;

        this.setWidth(width);
        this.setHeight(height);
    }

    public String getNote() {
        return note;
    }

    public void press() {
        this.setFill(Color.rgb(220, 220, 255));
    }

    public abstract void release();
}
