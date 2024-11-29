package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.MainFX;
import com.github.zephyrquest.modulamusicbox.models.*;
import com.github.zephyrquest.modulamusicbox.views.components.ChannelsControls;
import com.github.zephyrquest.modulamusicbox.views.components.FileSelection;
import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;
import com.github.zephyrquest.modulamusicbox.views.components.TrackControls;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.ArrayList;

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
        handleFileMenuSelection();

        handleFileSystemSelection();
    }

    private void setTrackControlsInView() {
        var playButton = trackControls.getPlayButton();
        var stopButton = trackControls.getStopButton();
        var rewindButton = trackControls.getRewindButton();

        playButton.setOnAction(event -> {
            playTrack();
        });

        stopButton.setOnAction(event -> {
            stopTrack();
        });

        rewindButton.setOnAction(event -> {
            rewindTrack();
        });

        var bpmTextfield = trackControls.getBpmTextField();
        bpmTextfield.textProperty().addListener((observableValue, oldValue, newValue) -> {
            handleBpmChange(bpmTextfield, newValue);
        });
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

    private void playTrack() {
        trackSequencer.startSequencer();
        trackControls.playTrack();
    }

    private void stopTrack() {
        trackSequencer.stopSequencer();
        trackControls.stopTrack();
    }

    private void rewindTrack() {
        trackSequencer.rewindSequencer();
    }

    private void changeTrackBpm(int bpm) {
        trackSequencer.updateTempoBpm(bpm);
    }

    private void changeTrack(File midiFile) throws Exception {
        trackSequencer.updateCurrentSequence(midiFile);
        trackSequencer.updateChannels();

        trackSynthesizer.setInstrumentsInChannels(trackSequencer.getChannels());
        trackSynthesizer.unmuteAllChannels();
        trackSynthesizer.unsoloAllChannels();

        int defaultChannelNumber = trackSequencer.getDefaultChannelNumber();
        noteReceiverFromSequencer.setCurrentChannelNumber(defaultChannelNumber);
        trackSynthesizer.setCurrentChannelNumber(defaultChannelNumber);
    }

    private void removeTrack() {
        trackSequencer.removeCurrentSequence();
        trackSequencer.updateChannels();

        trackSynthesizer.unmuteAllChannels();
        trackSynthesizer.unsoloAllChannels();

        channelsControls.clearChannels();
        channelsControls.updateChannels(new ArrayList<>());
    }

    private void changeChannelInTrack(int channelNumber) {
        noteReceiverFromSequencer.setCurrentChannelNumber(channelNumber);
        trackSynthesizer.setCurrentChannelNumber(channelNumber);
    }

    private void handleFileMenuSelection() {
        var midiFileComboBox = fileSelection.getMidiFileComboBox();

        var fileNames = midiFileManager.getMidiFiles()
                .stream()
                .map(File::getName)
                .toList();

        midiFileComboBox.getItems().add("-");
        midiFileComboBox.getItems().addAll(fileNames);
        midiFileComboBox.getSelectionModel().select("-");

        midiFileComboBox.setOnAction(event -> {
            resetTrackAndSynth();
            String selectedFileName = midiFileComboBox.getSelectionModel().getSelectedItem();
            fileSelection.getSelectedFileLabel().setText("");
            loadSelectedMidiFile(selectedFileName != null ? midiFileManager.getMidiFile(selectedFileName) : null);
        });
    }

    private void handleFileSystemSelection() {
        var midiFileComboBox = fileSelection.getMidiFileComboBox();
        var fileChooser = fileSelection.getFileChooser();
        var selectFileButton = fileSelection.getSelectFileButton();

        selectFileButton.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(MainFX.stage);
            if (selectedFile != null) {
                resetTrackAndSynth();
                midiFileComboBox.getSelectionModel().selectFirst();
                fileSelection.getSelectedFileLabel().setText(selectedFile.getName());
                loadSelectedMidiFile(selectedFile);
            }
        });
    }

    private void loadSelectedMidiFile(File midiFile) {
        if (midiFile != null) {
            try {
                changeTrack(midiFile);
                updateUIControls();
            } catch (Exception ex) {
                handleLoadingError();
            }
        } else {
            handleLoadingError();
        }
    }

    private void resetTrackAndSynth() {
        noteReceiverFromSequencer.setActive(false);
        trackSynthesizer.setCanUserInteract(false);
        keyboard.releaseAllKeys();
        trackSequencer.cleanChannels();
        trackSequencer.stopSequencer();
        trackSequencer.rewindSequencer();
        trackSynthesizer.unloadAllInstrumentsFromSynthesizer();
        trackControls.stopTrack();
    }

    private void updateUIControls() {
        channelsControls.clearChannels();
        channelsControls.updateChannels(trackSequencer.getChannels());

        handleMuteChannelCheckboxes();
        handleSoloChannelCheckBoxes();
        handleInstrumentComboBoxes();
        handleResetInstrumentButtons();

        trackControls.updateBpm(trackSequencer.getDefaultTempoBpm());
        trackControls.showTrackControls();

        noteReceiverFromSequencer.setActive(true);
        trackSynthesizer.setCanUserInteract(true);
    }

    private void handleLoadingError() {
        removeTrack();
        fileSelection.getSelectedFileLabel().setText("");
        fileSelection.getMidiFileComboBox().getSelectionModel().selectFirst();
        trackControls.hideTrackControls();
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

    private void handleBpmChange(TextField bpmTextfield, String bpm) {
        if(bpm.isEmpty()) {
            return;
        }

        if (!bpm.matches("\\d*")) {
            bpm = bpm.replaceAll("\\D", "");
            bpmTextfield.setText(bpm);
        }

        int newBpm = Integer.parseInt(bpm);
        int defaultBpm = trackSequencer.getDefaultTempoBpm();

        if(newBpm == 0 || newBpm > 240) {
            newBpm = defaultBpm;
            bpmTextfield.setText(String.valueOf(newBpm));
        }

        changeTrackBpm(newBpm);
    }

    private void handleInstrumentComboBoxes() {
        for (var instrumentComboBox : channelsControls.getInstrumentCheckboxes()) {
            instrumentComboBox.setOnAction(event -> {
                String id = instrumentComboBox.getId();
                String instrumentName = instrumentComboBox.getSelectionModel().getSelectedItem();

                changeInstrumentInChannel(Integer.parseInt(id), instrumentName);
            });
        }
    }

    private void changeInstrumentInChannel(int channelNumber, String instrumentName) {
        trackSynthesizer.changeInstrumentInChannel(channelNumber, instrumentName);
    }

    private void handleResetInstrumentButtons() {
        for (var resetInstrumentButton : channelsControls.getResetInstrumentButtons()) {
            resetInstrumentButton.setOnAction(event -> {
                String id = resetInstrumentButton.getId();

                Channel channel = trackSequencer.getChannel(Integer.parseInt(id));

                if(channel != null) {
                    changeInstrumentInChannel(Integer.parseInt(id), channel.getDefaultInstrument());
                    channelsControls.selectInstrument(id, channel.getDefaultInstrument());
                }
            });
        }
    }
}
