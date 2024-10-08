package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.FileInputSequencer;
import com.github.zephyrquest.modulamusicbox.models.KeyboardSynthesizer;
import com.github.zephyrquest.modulamusicbox.models.MidiInstrument;
import com.github.zephyrquest.modulamusicbox.views.components.MidiControls;

import java.io.File;

public class MidiControlsController {
    private final MidiControls midiControls;
    private final KeyboardSynthesizer keyboardSynthesizer;
    private final FileInputSequencer fileInputSequencer;


    public MidiControlsController(MidiControls midiControls, KeyboardSynthesizer keyboardSynthesizer,
                                  FileInputSequencer fileInputSequencer) {
        this.midiControls = midiControls;
        this.keyboardSynthesizer = keyboardSynthesizer;
        this.fileInputSequencer = fileInputSequencer;

        this.fileInputSequencer.getTransmitter().setReceiver(keyboardSynthesizer.getReceiver());

        setChannelsInView();
        setInstrumentsInView();
        setVelocityInView();
        setMidiInputFilesInView();
    }

    private void selectChannel(int channelNumber) {
        keyboardSynthesizer.setCurrentChannel(channelNumber);
    }

    private void selectInstrumentInChannel(String instrumentName) {
        keyboardSynthesizer.changeInstrumentInChannel(instrumentName);
    }

    private void changeVelocity(int velocity) {
        keyboardSynthesizer.setCurrentVelocity(velocity);
    }

    private void setChannelsInView() {
        var channelComboBox = midiControls.getChannelComboBox();
        var totalNumberOfChannels = keyboardSynthesizer.getMidiChannels().length;
        var instrumentComboBox = midiControls.getInstrumentComboBox();

        for(int i = 1; i <= totalNumberOfChannels; i++) {
            channelComboBox.getItems().add(i);
        }

        if(totalNumberOfChannels > 0) {
            channelComboBox.getSelectionModel().select(0);
        }

        channelComboBox.setOnAction(event -> {
            var channelNumber = channelComboBox.getSelectionModel().getSelectedItem();
            selectChannel(channelNumber);
            String instrumentNameInChannel = keyboardSynthesizer.getCurrentInstrument().getName().trim();
            instrumentComboBox.getSelectionModel().select(instrumentNameInChannel);
        });
    }

    private void setInstrumentsInView() {
        var instrumentComboBox = midiControls.getInstrumentComboBox();
        var instruments = keyboardSynthesizer.getAvailableInstruments();

        instrumentComboBox.getItems().addAll(instruments
                .stream()
                .map(MidiInstrument::getName)
                .toList());

        instrumentComboBox.getSelectionModel().select(keyboardSynthesizer.getCurrentInstrument().getName());

        instrumentComboBox.setOnAction(event -> {
            var instrumentName = instrumentComboBox.getSelectionModel().getSelectedItem();
            selectInstrumentInChannel(instrumentName);
        });
    }

    private void setVelocityInView() {
        var velocitySlider = midiControls.getVelocitySlider();
        var velocitySpinner = midiControls.getVelocitySpinner();

        int initVelocity = keyboardSynthesizer.getCurrentVelocity();
        velocitySlider.setValue(initVelocity);
        velocitySpinner.getValueFactory().setValue(initVelocity);

        velocitySlider.setOnMouseReleased(mouseEvent -> {
            int velocity = (int) Math.round(velocitySlider.getValue());
            changeVelocity(velocity);
            velocitySpinner.getValueFactory().setValue(velocity);
        });

        velocitySlider.valueProperty().addListener((observableValue, number, t1) ->
                velocitySpinner.getValueFactory().setValue(t1.intValue()));

        velocitySpinner.getValueFactory().valueProperty().addListener((observableValue, integer, t1) -> {
            changeVelocity(t1);
            velocitySlider.setValue(t1);
        });
    }

    private void setMidiInputFilesInView() {
        var midiInputFilesComboBox = midiControls.getMidiInputFileComboBox();
        var midiInputFileStartButton = midiControls.getMidiInputFileStartButton();
        var midiInputFilePauseButton = midiControls.getMidiInputFilePauseButton();
        var midiInputFileRewindButton = midiControls.getMidiInputFileRewindButton();
        var fileNames = fileInputSequencer.getMidiFiles()
                .stream()
                .map(File::getName)
                .toList();

        midiInputFilesComboBox.getItems().add("-");
        midiInputFilesComboBox.getItems().addAll(fileNames);
        midiInputFilesComboBox.getSelectionModel().select("-");

        midiInputFilesComboBox.setOnAction(event -> {
            var midiFileName = midiInputFilesComboBox.getSelectionModel().getSelectedItem();
            fileInputSequencer.setSequence(midiFileName);
        });

        midiInputFileStartButton.setOnAction(event -> {
            fileInputSequencer.startSequencer();
        });

        midiInputFilePauseButton.setOnAction(event -> {
            fileInputSequencer.stopSequencer();
        });

        midiInputFileRewindButton.setOnAction(event -> {
            fileInputSequencer.rewindSequencer();
        });
    }
}
