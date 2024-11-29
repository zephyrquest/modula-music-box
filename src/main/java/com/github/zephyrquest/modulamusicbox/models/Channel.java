package com.github.zephyrquest.modulamusicbox.models;


public class Channel {
    private final int number;
    private final String defaultInstrument;
    private String instrument;
    private final boolean instrumentEditable;


    public Channel(int number, String instrument, boolean instrumentEditable) {
        this.number = number;
        this.defaultInstrument = instrument;
        this.instrument = instrument;
        this.instrumentEditable = instrumentEditable;
    }

    public int getNumber() {
        return number;
    }

    public String getDefaultInstrument() {
        return defaultInstrument;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public boolean isInstrumentEditable() {
        return instrumentEditable;
    }
}
