package com.github.zephyrquest.modulamusicbox.views.components;

public class BlackKey extends Key {

    public BlackKey(String note, double width, double height) {
        super(note, width, height);

        this.getStyleClass().addAll("key", "black-key");
    }
}
