package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.*;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class TrackSequencer {
    private Sequencer sequencer;
    private Transmitter transmitter1;
    private Transmitter transmitter2;
    private Sequence currentSequence;
    private Map<Integer, Channel> channels;
    private int defaultTempoBpm;


    public TrackSequencer() {
        initSequencer();

        defaultTempoBpm = 0;
    }

    public void updateCurrentSequence(File file) throws Exception {
        currentSequence = MidiSystem.getSequence(file);
        sequencer.setSequence(currentSequence);
        defaultTempoBpm = (int) sequencer.getTempoInBPM();
    }

    public void startSequencer() {
        if(sequencer.isOpen() && sequencer.getSequence() != null && currentSequence!= null) {
            sequencer.start();
        }
    }

    public void stopSequencer() {
        if(sequencer.isOpen() && currentSequence!= null) {
            sequencer.stop();
        }
    }

    public void rewindSequencer() {
        if(sequencer.isOpen() && currentSequence!= null) {
            sequencer.setTickPosition(0);
        }
    }

    public void closeSequencer() {
        if(sequencer.isOpen()) {
            sequencer.close();
        }
    }

    public void updateChannels() {
        if (currentSequence == null) {
            return;
        }

        Track[] tracks = currentSequence.getTracks();

        for (int i = 0; i < tracks.length; i++) {
            Track track = tracks[i];

            for (int j = 0; j < track.size(); j++) {
                MidiEvent midiEvent = track.get(j);
                MidiMessage midiMessage = midiEvent.getMessage();
                if (midiMessage instanceof ShortMessage shortMessage
                        && shortMessage.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                    int channelNumber = shortMessage.getChannel();
                    Instrument instrument = TrackSynthesizer.getInstrument(shortMessage.getData1());
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

    public void removeCurrentSequence() {
        currentSequence = null;

        defaultTempoBpm = 0;
    }

    public void cleanChannels() {
        channels = new TreeMap<>();
    }

    public int getDefaultTempoBpm() {
        return defaultTempoBpm;
    }

    public void updateTempoBpm(int bpm) {
        sequencer.setTempoInBPM(bpm);
    }

    public int getDefaultChannelNumber() {
        return channels.keySet().iterator().next();
    }

    public Transmitter getTransmitter1() {
        return transmitter1;
    }

    public Transmitter getTransmitter2() {
        return transmitter2;
    }

    public Map<Integer, Channel> getChannels() {
        return channels;
    }

    private void initSequencer() {
        try {
            sequencer = MidiSystem.getSequencer(false);
            sequencer.open();
            transmitter1 = sequencer.getTransmitter();
            transmitter2 = sequencer.getTransmitter();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
