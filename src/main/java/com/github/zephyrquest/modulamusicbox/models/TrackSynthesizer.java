package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.*;
import java.util.Arrays;
import java.util.Map;

public class TrackSynthesizer {
    private static Instrument[] availableInstruments;
    private Synthesizer synthesizer;
    private MidiChannel[] midiChannels;
    private Receiver receiver;
    private int currentChannelNumber;
    private int currentVelocity;
    private boolean canUserInteract;


    public TrackSynthesizer() {
        initSynthesizer();
        currentVelocity = 200;
        canUserInteract = false;
    }

    public static Instrument getInstrument(int index) {
        if(availableInstruments == null || index < 0 || index >= availableInstruments.length) {
            return null;
        }

        return availableInstruments[index];
    }

    public void playNode(int noteNumber) {
        if(canUserInteract) {
            midiChannels[currentChannelNumber].noteOn(noteNumber, currentVelocity);
        }
    }

    public void stopNote(int noteNumber) {
        if(canUserInteract) {
            midiChannels[currentChannelNumber].noteOff(noteNumber);
        }
    }

    public void closeSynthesizer() {
        synthesizer.close();
    }

    public void unloadAllInstrumentsFromSynthesizer() {
        synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
    }

    public void muteChannel(int channelNumber) {
        if(channelNumber < midiChannels.length) {
            midiChannels[channelNumber].setMute(true);
        }
    }

    public void unmuteChannel(int channelNumber) {
        if(channelNumber < midiChannels.length) {
            midiChannels[channelNumber].setMute(false);
        }
    }

    public void unmuteAllChannels() {
        for(var midiChannel : midiChannels) {
            midiChannel.setMute(false);
        }
    }

    public void soloChannel(int channelNumber) {
        if(channelNumber < midiChannels.length) {
            midiChannels[channelNumber].setSolo(true);
        }
    }

    public void unsoloChannel(int channelNumber) {
        if(channelNumber < midiChannels.length) {
            midiChannels[channelNumber].setSolo(false);
        }
    }

    public void unsoloAllChannels() {
        for(var midiChannel : midiChannels) {
            midiChannel.setSolo(false);
        }
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

    public Receiver getReceiver() {
        return receiver;
    }

    public void setCurrentChannelNumber(int currentChannelNumber) {
        if(currentChannelNumber >= midiChannels.length) {
            this.currentChannelNumber = 0;
        }
        else {
            this.currentChannelNumber = currentChannelNumber;
        }
    }

    public void setCanUserInteract(boolean canUserInteract) {
        this.canUserInteract = canUserInteract;
    }

    private void initSynthesizer() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            midiChannels = synthesizer.getChannels();
            receiver = synthesizer.getReceiver();
            availableInstruments = synthesizer.getAvailableInstruments();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
