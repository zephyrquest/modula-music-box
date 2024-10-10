package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class TrackSequencer {
    private Sequencer sequencer;
    private Transmitter transmitter;
    private Sequence currentSequence;

    public TrackSequencer() {
        initSequencer();
    }

    public void setCurrentSequence(File file) {
        try {
            currentSequence = MidiSystem.getSequence(file);
            sequencer.setSequence(currentSequence);
        } catch (InvalidMidiDataException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startSequencer() {
        if(sequencer.isOpen() && sequencer.getSequence() != null) {
            sequencer.start();
        }
    }

    public void stopSequencer() {
        if(sequencer.isOpen()) {
            sequencer.stop();
        }
    }

    public void rewindSequencer() {
        if(sequencer.isOpen()) {
            sequencer.setTickPosition(0);
        }
    }

    public void closeSequencer() {
        if(sequencer.isOpen()) {
            sequencer.close();
        }
    }

    public Transmitter getTransmitter() {
        return transmitter;
    }

    public Sequence getCurrentSequence() {
        return currentSequence;
    }

    private void initSequencer() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            transmitter = sequencer.getTransmitter();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
