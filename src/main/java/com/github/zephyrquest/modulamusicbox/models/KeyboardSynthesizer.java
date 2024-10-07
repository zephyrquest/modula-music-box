package com.github.zephyrquest.modulamusicbox.models;

import org.yaml.snakeyaml.Yaml;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class KeyboardSynthesizer {
    private Synthesizer synthesizer;
    private List<MidiInstrument> availableInstruments;
    private MidiChannel[] midiChannels;
    private MidiInstrument[] instrumentInChannels;
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
                updateInstrumentInChannel(availableInstruments.get(0), currentChannelNumber);
            }
        }
        else {
            currentChannelNumber = 1;
        }
    }

    public void changeInstrumentInChannel(String instrumentName) {
        boolean found = false;

        for(var instrument : availableInstruments) {
            if(instrument.getName().equals(instrumentName)) {
                updateInstrumentInChannel(instrument, currentChannelNumber);
                found = true;
                break;
            }
        }

        if(!found) {
            updateInstrumentInChannel(availableInstruments.get(0), currentChannelNumber);
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

    public List<MidiInstrument> getAvailableInstruments() {
        return availableInstruments;
    }

    public MidiInstrument getCurrentInstrument() {
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
            instrumentInChannels = new MidiInstrument[midiChannels.length];
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private void initInstruments() {
        availableInstruments = new ArrayList<>();
        var allInstruments = synthesizer.getAvailableInstruments();

        try(var inputStream = getClass().getResourceAsStream("/instruments.yml")) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(inputStream);

            var instrumentsData = (List<Map<String, Object>>) data.get("instruments");
            for(var instrumentData : instrumentsData) {
                String name = (String) instrumentData.get("name");

                var instrumentOpt = Arrays.stream(allInstruments)
                        .filter(i -> i.getName().trim().equals(name))
                        .findFirst();
                if(instrumentOpt.isPresent()) {
                    String type = (String) instrumentData.get("type");
                    boolean hasSustain = (Boolean) instrumentData.get("hasSustain");
                    var instrument = new MidiInstrument(instrumentOpt.get(), name, type, hasSustain);
                    availableInstruments.add(instrument);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        updateInstrumentInChannel(availableInstruments.get(0), currentChannelNumber);
    }

    private void loadMidiNotes() {
        try(InputStream inputStream = getClass().getResourceAsStream("/midi_notes.properties")) {
            midiNotes = new Properties();
            midiNotes.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateInstrumentInChannel(MidiInstrument instrument, int channelNumber) {
        instrumentInChannels[channelNumber - 1] = instrument;
        synthesizer.loadInstrument(instrument.getInstrument());
        midiChannels[channelNumber - 1].programChange(instrument.getInstrument().getPatch().getBank(),
               instrument.getInstrument().getPatch().getProgram());
    }
}
