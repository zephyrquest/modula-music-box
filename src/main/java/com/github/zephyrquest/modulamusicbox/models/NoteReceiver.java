package com.github.zephyrquest.modulamusicbox.models;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class NoteReceiver implements Receiver {

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if(message instanceof ShortMessage shortMessage) {
            if(shortMessage.getCommand() == ShortMessage.NOTE_ON) {
                int key = shortMessage.getData1();
                System.out.println("Note ON: " + key);
            }
        }
    }

    @Override
    public void close() {

    }
}
