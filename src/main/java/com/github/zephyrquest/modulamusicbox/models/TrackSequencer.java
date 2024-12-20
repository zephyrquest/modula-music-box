package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.*;
import java.io.File;
import java.util.*;

public class TrackSequencer {
    private Sequencer sequencer;
    private Transmitter transmitter1;
    private Transmitter transmitter2;
    private Sequence currentSequence;
    private List<Channel> channels;
    private int defaultTempoBpm;
    private int currentBpm;
    private boolean isSequencePlaying;


    public TrackSequencer() {
        initSequencer();

        defaultTempoBpm = 0;
        currentBpm = 0;
        isSequencePlaying = false;
    }

    public void updateCurrentSequence(File file) throws Exception {
        currentSequence = MidiSystem.getSequence(file);
        sequencer.setSequence(currentSequence);
        defaultTempoBpm = (int) sequencer.getTempoInBPM();
        currentBpm = defaultTempoBpm;
    }

    public void startSequencer() {
        if(sequencer.isOpen() && sequencer.getSequence() != null && currentSequence!= null && !isSequencePlaying) {
            isSequencePlaying = true;
            sequencer.start();
            // Reset the current BPM to ensure that when the sequencer restarts, it does not revert to the default BPM
            sequencer.setTempoInBPM(currentBpm);
        }
    }

    public void stopSequencer() {
        if(sequencer.isOpen() && currentSequence!= null && isSequencePlaying) {
            isSequencePlaying = false;
            sequencer.stop();
        }
    }

    public void rewindSequencer() {
        if(sequencer.isOpen() && currentSequence!= null) {
            sequencer.setTickPosition(0);

            // Reset the current BPM to ensure that when the sequencer restarts, it does not revert to the default BPM
            sequencer.setTempoInBPM(currentBpm);
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
        boolean percussionChannelPresent = false;

        for (int i = 0; i < tracks.length; i++) {
            Track track = tracks[i];

            for (int j = 0; j < track.size(); j++) {
                MidiEvent midiEvent = track.get(j);
                MidiMessage midiMessage = midiEvent.getMessage();
                if (midiMessage instanceof ShortMessage shortMessage &&
                        shortMessage.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                    int channelNumber = shortMessage.getChannel();
                    if(channelNumber == 9) {
                        if(!percussionChannelPresent) {
                            percussionChannelPresent = true;
                        }

                        continue;
                    }

                    Instrument instrument = TrackSynthesizer.getInstrument(shortMessage.getData1());
                    if (instrument != null) {
                        String instrumentName = instrument.getName().trim();
                        Channel channel = getChannel(channelNumber);
                        if (channel == null) {
                            Channel newChannel = new Channel(channelNumber, instrumentName.trim(), true);
                            channels.add(newChannel);
                        }
                    }
                }
            }
        }

        if(percussionChannelPresent) {
            Channel percussionChannel = new Channel(9, "Percussion", false);
            channels.add(percussionChannel);
        }

        channels.sort(Comparator.comparingInt(Channel::getNumber));
    }

    public void removeCurrentSequence() {
        currentSequence = null;

        defaultTempoBpm = 0;
        currentBpm = 0;
    }

    public void cleanChannels() {
        channels = new ArrayList<>();
    }

    public void changeInstrumentInChannel(int channelNumber, String instrumentName) {
        Channel channel = getChannel(channelNumber);

        if(channel != null) {
            channel.setInstrument(instrumentName);
        }
    }

    public int getDefaultTempoBpm() {
        return defaultTempoBpm;
    }

    public void updateTempoBpm(int bpm) {
        currentBpm = bpm;
        sequencer.setTempoInBPM(currentBpm);
    }

    public int getDefaultChannelNumber() {
        return channels.get(0).getNumber();
    }

    public Transmitter getTransmitter1() {
        return transmitter1;
    }

    public Transmitter getTransmitter2() {
        return transmitter2;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public Channel getChannel(int channelNumber) {
        return channels
                .stream()
                .filter(channel -> channel.getNumber() == channelNumber)
                .findFirst()
                .orElse(null);
    }

    public int getCurrentSequencePosition() {
        long microsecondPosition = sequencer.getMicrosecondPosition();
        return (int) microsecondPosition / 1000000;
    }

    public void setCurrentSequencePosition(int seconds) {
        sequencer.setMicrosecondPosition(seconds * 1000000L);

        // Reset the current BPM to ensure that when the sequence position is updated, it does not revert to the default BPM
        sequencer.setTempoInBPM(currentBpm);
    }

    public int getSequenceLengthInSeconds() {
        long microsecondLength = sequencer.getMicrosecondLength();
        return (int) microsecondLength / 1000000;
    }

    public Sequencer getSequencer() {
        return sequencer;
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
