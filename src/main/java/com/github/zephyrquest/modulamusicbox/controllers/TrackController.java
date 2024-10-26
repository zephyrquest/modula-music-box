package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.KeyboardSynthesizer;
import com.github.zephyrquest.modulamusicbox.models.MidiFileManager;
import com.github.zephyrquest.modulamusicbox.models.NoteReceiverFromSequencer;
import com.github.zephyrquest.modulamusicbox.models.TrackSequencer;
import com.github.zephyrquest.modulamusicbox.views.components.ChannelsControls;
import com.github.zephyrquest.modulamusicbox.views.components.FileSelection;
import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;
import com.github.zephyrquest.modulamusicbox.views.components.TrackControls;
import javafx.scene.control.RadioButton;

import java.io.File;
import java.util.TreeMap;

public class TrackController {
    private final TrackSequencer trackSequencer;
    private final MidiFileManager midiFileManager;
    private final KeyboardSynthesizer keyboardSynthesizer;
    private final NoteReceiverFromSequencer noteReceiverFromSequencer;

    private final FileSelection fileSelection;
    private final Keyboard keyboard;
    private final TrackControls trackControls;
    private final ChannelsControls channelsControls;


    public TrackController(TrackSequencer trackSequencer, MidiFileManager midiFileManager,
                           KeyboardSynthesizer keyboardSynthesizer, NoteReceiverFromSequencer noteReceiverFromSequencer,
                           FileSelection fileSelection, Keyboard keyboard, TrackControls trackControls,
                           ChannelsControls channelsControls) {
        this.trackSequencer = trackSequencer;
        this.midiFileManager = midiFileManager;
        this.keyboardSynthesizer = keyboardSynthesizer;
        this.fileSelection = fileSelection;
        this.keyboard = keyboard;
        this.trackControls = trackControls;
        this.channelsControls = channelsControls;
        this.noteReceiverFromSequencer = noteReceiverFromSequencer;
        this.trackSequencer.getTransmitter().setReceiver(this.noteReceiverFromSequencer);

        setFileSelectionInView();
        setTrackControlsInView();
        setChannelsControlsInView();
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
            noteReceiverFromSequencer.setActive(false);
            keyboardSynthesizer.setActive(false);
            keyboard.releaseAllKeys();
            trackSequencer.cleanChannels();
            keyboardSynthesizer.unloadInstruments();

            String midiFileName = midiFileComboBox.getSelectionModel().getSelectedItem();
            File midiFile = midiFileManager.getMidiFile(midiFileName);
            if(midiFile != null) {
                changeTrack(midiFile);

                noteReceiverFromSequencer.setActive(true);
                keyboardSynthesizer.setActive(true);
            }
            else {
                removeTrack();
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

    private void setChannelsControlsInView() {
        var channelButtonsGroup = channelsControls.getChannelButtonsGroup();
        channelButtonsGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if(t1 != null) {
                noteReceiverFromSequencer.setActive(false);
                keyboardSynthesizer.setActive(false);
                keyboard.releaseAllKeys();

                RadioButton selectedChannelButton = (RadioButton) t1;
                int channelNumber = Integer.parseInt(selectedChannelButton.getId());
                changeChannelInTrack(channelNumber);

                noteReceiverFromSequencer.setActive(true);
                keyboardSynthesizer.setActive(true);
            }
        });
    }

    private void changeTrack(File midiFile) {
        trackSequencer.setCurrentSequenceAndUpdateChannels(midiFile);
        keyboardSynthesizer.setInstrumentsInChannels(trackSequencer.getChannels());
        channelsControls.updateView(trackSequencer.getChannels());

        int defaultChannelNumber = trackSequencer.getDefaultChannelNumber();
        noteReceiverFromSequencer.setCurrentChannelNumber(defaultChannelNumber);
        keyboardSynthesizer.setCurrentChannelNumber(defaultChannelNumber);
    }

    private void removeTrack() {
        trackSequencer.setCurrentSequenceAndUpdateChannels(null);
        channelsControls.updateView(new TreeMap<>());
    }

    private void changeChannelInTrack(int channelNumber) {
        noteReceiverFromSequencer.setCurrentChannelNumber(channelNumber);
        keyboardSynthesizer.setCurrentChannelNumber(channelNumber);
    }
}
