package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class TrackSequencer {
    private Sequencer sequencer;



    public TrackSequencer() {
        initSequencer();
    }

    public void setSequence(File file) {
        try {
            Sequence sequence = MidiSystem.getSequence(file);
            sequencer.setSequence(sequence);
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

    private void initSequencer() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
