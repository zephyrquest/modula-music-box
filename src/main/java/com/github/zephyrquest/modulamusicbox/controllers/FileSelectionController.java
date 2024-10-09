package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.MidiFileManager;
import com.github.zephyrquest.modulamusicbox.models.TrackSequencer;
import com.github.zephyrquest.modulamusicbox.views.components.FileSelection;

import java.io.File;

public class FileSelectionController {
    private final FileSelection fileSelection;
    private final TrackSequencer trackSequencer;
    private final MidiFileManager midiFileManager;


    public FileSelectionController(FileSelection fileSelection, TrackSequencer trackSequencer, MidiFileManager midiFileManager) {
        this.fileSelection = fileSelection;
        this.trackSequencer = trackSequencer;
        this.midiFileManager = midiFileManager;

        setFileSelectionInView();
    }

    private void setFileSelectionInView() {
        var midiFileComboBox = fileSelection.getMidiFileComboBox();

        var fileNames = midiFileManager.getMidiFiles()
                .stream()
                .map(File::getName)
                .toList();

        midiFileComboBox.getItems().add("-");
        midiFileComboBox.getItems().addAll(fileNames);
        midiFileComboBox.getSelectionModel().select("-");

        midiFileComboBox.setOnAction(event -> {
            String midiFileName = midiFileComboBox.getSelectionModel().getSelectedItem();
            File midiFile = midiFileManager.getMidiFile(midiFileName);
            if(midiFile != null) {
                trackSequencer.setSequence(midiFile);
            }
        });
    }
}
