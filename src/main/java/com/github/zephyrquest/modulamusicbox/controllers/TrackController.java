package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.MidiFileManager;
import com.github.zephyrquest.modulamusicbox.models.NoteReceiver;
import com.github.zephyrquest.modulamusicbox.models.TrackSequencer;
import com.github.zephyrquest.modulamusicbox.views.components.FileSelection;
import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;
import com.github.zephyrquest.modulamusicbox.views.components.TrackControls;

import java.io.File;

public class TrackController {
    private final TrackSequencer trackSequencer;
    private final MidiFileManager midiFileManager;

    private final FileSelection fileSelection;
    private final Keyboard keyboard;
    private final TrackControls trackControls;


    public TrackController(TrackSequencer trackSequencer, MidiFileManager midiFileManager,
                           FileSelection fileSelection, Keyboard keyboard, TrackControls trackControls) {
        this.trackSequencer = trackSequencer;
        this.midiFileManager = midiFileManager;
        this.fileSelection = fileSelection;
        this.keyboard = keyboard;
        this.trackControls = trackControls;

        setFileSelectionInView();
        setTrackControlsInView();

        this.trackSequencer.getTransmitter().setReceiver(new NoteReceiver(keyboard));
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

    private void setTrackControlsInView() {
        var playButton = trackControls.getPlayButton();
        var stopButton = trackControls.getStopButton();
        var rewindButton = trackControls.getRewindButton();

        playButton.setOnAction(event -> trackSequencer.startSequencer());
        stopButton.setOnAction(event -> trackSequencer.stopSequencer());
        rewindButton.setOnAction(event -> trackSequencer.rewindSequencer());
    }
}
