package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KeyboardSynthesizer {
    private Synthesizer synthesizer;
    private Instrument[] availableInstruments;
    private MidiChannel[] midiChannels;
    private Instrument[] instrumentInChannels;
    private int currentChannelNumber;
    private Properties midiNotes;
    private int currentVelocity;


    public KeyboardSynthesizer() {
        currentChannelNumber = 1;
        currentVelocity = 100;

        initSynthesizer();
        initInstruments();
        loadMidiNotes();
    }

    public void playNode(String note) {
        String s = midiNotes.getProperty(note);

        if(s != null) {
            int noteNumber = Integer.parseInt(s);

            if (noteNumber >= 0 && midiChannels != null) {
                midiChannels[currentChannelNumber - 1].noteOn(noteNumber, currentVelocity);
            }
        }
    }

    public void stopNote(String note) {
        String s = midiNotes.getProperty(note);

        if(s != null) {
            int noteNumber = Integer.parseInt(s);

            if (noteNumber >= 0 && midiChannels != null) {
                midiChannels[currentChannelNumber - 1].noteOff(noteNumber, 50);
            }
        }
    }

    public void setCurrentChannel(int channelNumber) {
        if(channelNumber > 0 && channelNumber <= midiChannels.length) {
            currentChannelNumber = channelNumber;
            if(instrumentInChannels[currentChannelNumber - 1] == null) {
                updateInstrumentInChannel(availableInstruments[0], currentChannelNumber);
            }
        }
        else {
            currentChannelNumber = 1;
        }
    }

    public void changeInstrumentInChannel(String instrumentName) {
        boolean found = false;

        for(var instrument : availableInstruments) {
            if(instrument.getName().trim().equals(instrumentName)) {
                updateInstrumentInChannel(instrument, currentChannelNumber);
                found = true;
                break;
            }
        }

        if(!found) {
            updateInstrumentInChannel(availableInstruments[0], currentChannelNumber);
        }
    }

    public void setCurrentVelocity(int currentVelocity) {
        if(currentVelocity >= 0) {
            this.currentVelocity = currentVelocity;
        }
        else {
            this.currentVelocity = 100;
        }
    }

    public MidiChannel[] getMidiChannels() {
        return midiChannels;
    }

    public MidiChannel getCurrentChannel() {
        return midiChannels[currentChannelNumber];
    }

    public Instrument[] getAvailableInstruments() {
        return availableInstruments;
    }

    public Instrument getCurrentInstrument() {
        return instrumentInChannels[currentChannelNumber - 1];
    }

    public int getCurrentVelocity() {
        return currentVelocity;
    }

    public void closeSynthesizer() {
        synthesizer.close();
    }

    private void initSynthesizer() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            midiChannels = synthesizer.getChannels();
            instrumentInChannels = new Instrument[midiChannels.length];
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private void initInstruments() {
        availableInstruments = synthesizer.getAvailableInstruments();

        updateInstrumentInChannel(availableInstruments[0], currentChannelNumber);
    }

    private void loadMidiNotes() {
        try(InputStream inputStream = getClass().getResourceAsStream("/midi_notes.properties")) {
            midiNotes = new Properties();
            midiNotes.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateInstrumentInChannel(Instrument instrument, int channelNumber) {
        instrumentInChannels[channelNumber - 1] = instrument;
        synthesizer.loadInstrument(instrument);
        midiChannels[channelNumber - 1].programChange(instrument.getPatch().getBank(),
                instrument.getPatch().getProgram());
    }
}
