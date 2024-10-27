package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.MainFX;
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
            trackSequencer.stopSequencer();
            trackSequencer.rewindSequencer();
            keyboardSynthesizer.unloadInstruments();

            String midiFileName = midiFileComboBox.getSelectionModel().getSelectedItem();
            File midiFile = midiFileManager.getMidiFile(midiFileName);
            if(midiFile != null) {
                fileSelection.getSelectedFileLabel().setText("");

                try {
                    changeTrack(midiFile);
                }
                catch (Exception ex) {
                    removeTrack();
                }

                noteReceiverFromSequencer.setActive(true);
                keyboardSynthesizer.setActive(true);
            }
            else {
                removeTrack();
            }
        });

        var fileChooser = fileSelection.getFileChooser();
        var selectFileButton = fileSelection.getSelectFileButton();

        selectFileButton.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(MainFX.stage);

            if(selectedFile != null) {
                midiFileComboBox.getSelectionModel().selectFirst();

                noteReceiverFromSequencer.setActive(false);
                keyboardSynthesizer.setActive(false);
                keyboard.releaseAllKeys();
                trackSequencer.cleanChannels();
                trackSequencer.stopSequencer();
                trackSequencer.rewindSequencer();
                keyboardSynthesizer.unloadInstruments();

                midiFileComboBox.getSelectionModel().selectFirst();
                fileSelection.getSelectedFileLabel().setText(selectedFile.getName());

                try {
                    changeTrack(selectedFile);

                    noteReceiverFromSequencer.setActive(true);
                    keyboardSynthesizer.setActive(true);
                }
                catch (Exception ex) {
                    removeTrack();
                    fileSelection.getSelectedFileLabel().setText("");
                }
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

    private void changeTrack(File midiFile) throws Exception {
        trackSequencer.updateCurrentSequence(midiFile);
        trackSequencer.updateChannels();
        keyboardSynthesizer.setInstrumentsInChannels(trackSequencer.getChannels());
        channelsControls.updateView(trackSequencer.getChannels());

        int defaultChannelNumber = trackSequencer.getDefaultChannelNumber();
        noteReceiverFromSequencer.setCurrentChannelNumber(defaultChannelNumber);
        keyboardSynthesizer.setCurrentChannelNumber(defaultChannelNumber);
    }

    private void removeTrack() {
        trackSequencer.removeCurrentSequence();
        trackSequencer.updateChannels();
        channelsControls.updateView(new TreeMap<>());
    }

    private void changeChannelInTrack(int channelNumber) {
        noteReceiverFromSequencer.setCurrentChannelNumber(channelNumber);
        keyboardSynthesizer.setCurrentChannelNumber(channelNumber);
    }
}
