package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class FileSelection extends VBox {
    private final ComboBox<String> midiFileComboBox;
    private final FileChooser fileChooser;
    private final Button selectFileButton;
    private final Label selectedFileLabel;


    public FileSelection() {
        midiFileComboBox = new ComboBox<>();
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDI Files", "*.mid"));
        selectFileButton = new Button("Select a MIDI file");
        selectedFileLabel = new Label();

        HBox fileInputContainer = new HBox();
        fileInputContainer.getChildren().addAll(selectFileButton, selectedFileLabel);

        this.getChildren().addAll(midiFileComboBox, fileInputContainer);
    }

    public ComboBox<String> getMidiFileComboBox() {
        return midiFileComboBox;
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public Button getSelectFileButton() {
        return selectFileButton;
    }

    public Label getSelectedFileLabel() {
        return selectedFileLabel;
    }
}
