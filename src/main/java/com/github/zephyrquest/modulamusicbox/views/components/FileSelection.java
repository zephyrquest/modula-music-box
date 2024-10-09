package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FileSelection extends VBox {
    private final ComboBox<String> midiFileComboBox;


    public FileSelection() {
        midiFileComboBox = new ComboBox<>();

        this.getChildren().add(midiFileComboBox);
    }

    public ComboBox<String> getMidiFileComboBox() {
        return midiFileComboBox;
    }
}
