package com.github.zephyrquest.modulamusicbox.models;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    private final List<String> instruments;


    public Channel() {
        this.instruments = new ArrayList<>();
    }

    public List<String> getInstruments() {
        return instruments;
    }
}
