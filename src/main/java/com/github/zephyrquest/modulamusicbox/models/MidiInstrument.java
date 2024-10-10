package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.Instrument;

public class MidiInstrument {
    private final Instrument instrument;
    private final String name;
    private final String type;
    private final boolean hasSustain;


    public MidiInstrument(Instrument instrument, String name, String type, boolean hasSustain) {
        this.instrument = instrument;
        this.name = name;
        this.type = type;
        this.hasSustain = hasSustain;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean hasSustain() {
        return hasSustain;
    }
}
