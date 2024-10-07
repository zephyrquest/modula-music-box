package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.KeyboardSynthesizer;
import com.github.zephyrquest.modulamusicbox.views.components.MidiControls;

import java.util.Arrays;

public class MidiControlsController {
    private final MidiControls midiControls;
    private final KeyboardSynthesizer keyboardSynthesizer;


    public MidiControlsController(MidiControls midiControls, KeyboardSynthesizer keyboardSynthesizer) {
        this.midiControls = midiControls;
        this.keyboardSynthesizer = keyboardSynthesizer;

        setChannelsInView();
        setInstrumentsInView();
        setVelocityInView();
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

        instrumentComboBox.getItems().addAll(Arrays
                .stream(instruments)
                .map(instrument -> instrument.getName().trim())
                .toList());

        instrumentComboBox.getSelectionModel().select(keyboardSynthesizer.getCurrentInstrument().getName().trim());

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
}
