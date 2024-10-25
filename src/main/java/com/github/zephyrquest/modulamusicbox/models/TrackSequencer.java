package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class TrackSequencer {
    private Sequencer sequencer;
    private Transmitter transmitter;
    private Sequence currentSequence;
    private Map<Integer, Channel> channels;


    public TrackSequencer() {
        initSequencer();
    }

    public void setCurrentSequenceAndUpdateChannels(File file) {
        try {
            currentSequence = MidiSystem.getSequence(file);
            sequencer.setSequence(currentSequence);
            setUpChannels();
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

    public Map<Integer, Channel> getChannels() {
        return channels;
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

    private void setUpChannels() {
        if (currentSequence == null) {
            return;
        }

        channels = new TreeMap<>();

        Track[] tracks = currentSequence.getTracks();

        for (int i = 0; i < tracks.length; i++) {
            Track track = tracks[i];

            for (int j = 0; j < track.size(); j++) {
                MidiEvent midiEvent = track.get(j);
                MidiMessage midiMessage = midiEvent.getMessage();
                if (midiMessage instanceof ShortMessage shortMessage
                        && shortMessage.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                    int channelNumber = shortMessage.getChannel();
                    Instrument instrument = KeyboardSynthesizer.getInstrument(shortMessage.getData1());
                    if (instrument != null) {
                        String instrumentName = instrument.getName().trim();
                        Channel channel = channels.get(channelNumber);
                        if (channel == null) {
                            Channel newChannel = new Channel(instrumentName);
                            channels.put(channelNumber, newChannel);
                        }
                    }
                }
            }
        }
    }
}
