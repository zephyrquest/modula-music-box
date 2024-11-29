package com.github.zephyrquest.modulamusicbox.models;


public class Channel {
    private final int number;
    private final String instrument;


    public Channel(int number, String instrument) {
        this.number = number;
        this.instrument = instrument;
    }

    public int getNumber() {
        return number;
    }

    public String getInstrument() {
        return instrument;
    }
}
