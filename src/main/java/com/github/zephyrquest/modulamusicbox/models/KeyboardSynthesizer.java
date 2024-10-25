package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.*;
import java.util.Arrays;
import java.util.Map;

public class KeyboardSynthesizer {
    private static Instrument[] availableInstruments;

    private Synthesizer synthesizer;
    private MidiChannel[] midiChannels;
    private int currentChannelNumber;
    private int currentVelocity;


    public KeyboardSynthesizer() {
        initSynthesizer();
        currentChannelNumber = -1;
        currentVelocity = 200;
    }

    public static Instrument getInstrument(int index) {
        if(availableInstruments == null || index < 0 || index >= availableInstruments.length) {
            return null;
        }

        return availableInstruments[index];
    }

    public void playNode(int noteNumber) {
        if (midiChannels != null && noteNumber >= 0
                && currentChannelNumber >= 0 && currentChannelNumber < midiChannels.length) {
            midiChannels[currentChannelNumber].noteOn(noteNumber, currentVelocity);
        }
    }

    public void stopNote(int noteNumber) {
        if(midiChannels != null && noteNumber > 0
                && currentChannelNumber >= 0 && currentChannelNumber < midiChannels.length) {
            midiChannels[currentChannelNumber].noteOff(noteNumber);
        }
    }

    public void setCurrentChannelNumber(int currentChannelNumber) {
        this.currentChannelNumber = currentChannelNumber;
    }

    public void setInstrumentsInChannels(Map<Integer, Channel> channels) {
        for(var entry : channels.entrySet()) {
            int channelNumber = entry.getKey();
            String instrumentName = entry.getValue().getInstrument();

            if(channelNumber >= 0 && channelNumber < midiChannels.length) {
                var instrumentOpt = Arrays.stream(availableInstruments)
                        .filter(instrument -> instrument.getName().trim().equals(instrumentName))
                        .findFirst();
                if(instrumentOpt.isPresent()) {
                    synthesizer.loadInstrument(instrumentOpt.get());
                    midiChannels[channelNumber].programChange(instrumentOpt.get().getPatch().getBank(),
                            instrumentOpt.get().getPatch().getProgram());
                }
            }
        }
    }

    private void initSynthesizer() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            midiChannels = synthesizer.getChannels();
            availableInstruments = synthesizer.getAvailableInstruments();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
