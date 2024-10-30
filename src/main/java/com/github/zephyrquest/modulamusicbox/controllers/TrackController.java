package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.MainFX;
import com.github.zephyrquest.modulamusicbox.models.*;
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
    private final TrackSynthesizer trackSynthesizer;
    private final NoteReceiverFromSequencer noteReceiverFromSequencer;

    private final FileSelection fileSelection;
    private final Keyboard keyboard;
    private final TrackControls trackControls;
    private final ChannelsControls channelsControls;


    public TrackController(TrackSequencer trackSequencer, MidiFileManager midiFileManager,
                           TrackSynthesizer trackSynthesizer,
                           NoteReceiverFromSequencer noteReceiverFromSequencer,
                           FileSelection fileSelection, Keyboard keyboard, TrackControls trackControls,
                           ChannelsControls channelsControls) {
        this.trackSequencer = trackSequencer;
        this.midiFileManager = midiFileManager;
        this.trackSynthesizer = trackSynthesizer;
        this.fileSelection = fileSelection;
        this.keyboard = keyboard;
        this.trackControls = trackControls;
        this.channelsControls = channelsControls;
        this.noteReceiverFromSequencer = noteReceiverFromSequencer;
        this.trackSequencer.getTransmitter1().setReceiver(this.trackSynthesizer.getReceiver());
        this.trackSequencer.getTransmitter2().setReceiver(this.noteReceiverFromSequencer);

        setFileSelectionInView();
        setTrackControlsInView();
        setChannelsControlsInView();
    }

    private void setFileSelectionInView() {
        handleMidiFileMenuSelection();

        handleMidiFileFileSystemSelection();
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
                trackSynthesizer.setCanUserInteract(false);
                keyboard.releaseAllKeys();

                RadioButton selectedChannelButton = (RadioButton) t1;
                int channelNumber = Integer.parseInt(selectedChannelButton.getId());
                changeChannelInTrack(channelNumber);

                noteReceiverFromSequencer.setActive(true);
                trackSynthesizer.setCanUserInteract(true);
            }
        });
    }

    private void changeTrack(File midiFile) throws Exception {
        trackSequencer.updateCurrentSequence(midiFile);
        trackSequencer.updateChannels();
        trackSynthesizer.setInstrumentsInChannels(trackSequencer.getChannels());
        trackSynthesizer.unmuteAllChannels();
        trackSynthesizer.unsoloAllChannels();
        channelsControls.updateView(trackSequencer.getChannels());
        handleMuteChannelCheckboxes();
        handleSoloChannelCheckBoxes();

        int defaultChannelNumber = trackSequencer.getDefaultChannelNumber();
        noteReceiverFromSequencer.setCurrentChannelNumber(defaultChannelNumber);
        trackSynthesizer.setCurrentChannelNumber(defaultChannelNumber);
    }

    private void removeTrack() {
        trackSequencer.removeCurrentSequence();
        trackSequencer.updateChannels();
        trackSynthesizer.unmuteAllChannels();
        trackSynthesizer.unsoloAllChannels();
        channelsControls.updateView(new TreeMap<>());
    }

    private void changeChannelInTrack(int channelNumber) {
        noteReceiverFromSequencer.setCurrentChannelNumber(channelNumber);
        trackSynthesizer.setCurrentChannelNumber(channelNumber);
    }

    private void handleMidiFileMenuSelection() {
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
            trackSynthesizer.setCanUserInteract(false);
            keyboard.releaseAllKeys();
            trackSequencer.cleanChannels();
            trackSequencer.stopSequencer();
            trackSequencer.rewindSequencer();
            trackSynthesizer.unloadAllInstrumentsFromSynthesizer();

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
                trackSynthesizer.setCanUserInteract(true);
            }
            else {
                removeTrack();
            }
        });
    }

    private void handleMidiFileFileSystemSelection() {
        var midiFileComboBox = fileSelection.getMidiFileComboBox();
        var fileChooser = fileSelection.getFileChooser();
        var selectFileButton = fileSelection.getSelectFileButton();

        selectFileButton.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(MainFX.stage);

            if(selectedFile != null) {
                midiFileComboBox.getSelectionModel().selectFirst();

                noteReceiverFromSequencer.setActive(false);
                trackSynthesizer.setCanUserInteract(false);
                keyboard.releaseAllKeys();
                trackSequencer.cleanChannels();
                trackSequencer.stopSequencer();
                trackSequencer.rewindSequencer();
                trackSynthesizer.unloadAllInstrumentsFromSynthesizer();

                midiFileComboBox.getSelectionModel().selectFirst();
                fileSelection.getSelectedFileLabel().setText(selectedFile.getName());

                try {
                    changeTrack(selectedFile);

                    noteReceiverFromSequencer.setActive(true);
                    trackSynthesizer.setCanUserInteract(true);
                }
                catch (Exception ex) {
                    removeTrack();
                    fileSelection.getSelectedFileLabel().setText("");
                }
            }
        });
    }

    private void handleMuteChannelCheckboxes() {
        for(var muteChannelCheckbox : channelsControls.getMuteChannelCheckboxes()) {
            muteChannelCheckbox.setOnAction(event -> {
                String id = muteChannelCheckbox.getId();

                if(muteChannelCheckbox.isSelected()) {
                    trackSynthesizer.muteChannel(Integer.parseInt(id));
                }
                else {
                    trackSynthesizer.unmuteChannel(Integer.parseInt(id));
                }
            });
        }
    }

    private void handleSoloChannelCheckBoxes() {
        for(var soloChannelCheckbox : channelsControls.getSoloChannelCheckboxes()) {
            soloChannelCheckbox.setOnAction(event -> {
                String id = soloChannelCheckbox.getId();

                if(soloChannelCheckbox.isSelected()) {
                    trackSynthesizer.unsoloAllChannels();
                    trackSynthesizer.unmuteAllChannels();
                    channelsControls.deselectAllSoloChannelCheckboxes();
                    channelsControls.selectAllMuteChannelCheckboxes();

                    channelsControls.selectSoloChannel(id);
                    channelsControls.deselectMuteChannel(id);
                    channelsControls.disableAllMuteChannelCheckboxes();
                    trackSynthesizer.soloChannel(Integer.parseInt(id));
                }
                else {
                    trackSynthesizer.unsoloChannel(Integer.parseInt(id));
                    channelsControls.enableAllMuteChannelCheckboxes();
                    channelsControls.deselectAllMuteChannelCheckboxes();
                }
            });
        }
    }
}
