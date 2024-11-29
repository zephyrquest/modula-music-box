package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List<String> getAllInstrumentNames() {
        if(availableInstruments == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(availableInstruments).map(instrument -> instrument.getName().trim()).toList();
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
        for (int i = 0; i < midiChannels.length; i++) {
            if (i == 9) {
                continue;
            }

            int bank = midiChannels[i].getController(0);
            int program = midiChannels[i].getProgram();

            for (Instrument instrument : availableInstruments) {
                Patch patch = instrument.getPatch();
                if (patch.getBank() == bank && patch.getProgram() == program) {
                    synthesizer.unloadInstrument(instrument);
                    break;
                }
            }
        }
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

    public void setInstrumentsInChannels(List<Channel> channels) {
        for(var channel : channels) {
            int channelNumber = channel.getNumber();

            if (channelNumber == 9) {
                continue;
            }

            if(channelNumber < midiChannels.length) {
                var instrumentOpt = Arrays.stream(availableInstruments)
                        .filter(instrument -> instrument.getName().trim().equals(channel.getInstrument()))
                        .findFirst();

                if(instrumentOpt.isPresent()) {
                    synthesizer.loadInstrument(instrumentOpt.get());
                    midiChannels[channelNumber].programChange(instrumentOpt.get().getPatch().getBank(),
                            instrumentOpt.get().getPatch().getProgram());
                }
            }
        }
    }

    public void changeInstrumentInChannel(int channelNumber, String instrumentName) {
        var instrumentOpt = Arrays.stream(availableInstruments)
                .filter(instrument -> instrument.getName().trim().equals(instrumentName))
                .findFirst();

        if(instrumentOpt.isPresent()) {
            synthesizer.loadInstrument(instrumentOpt.get());
            midiChannels[channelNumber].programChange(instrumentOpt.get().getPatch().getBank(),
                    instrumentOpt.get().getPatch().getProgram());
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
            availableInstruments = synthesizer.getDefaultSoundbank().getInstruments();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
