package com.github.zephyrquest.modulamusicbox.models;

import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class NoteReceiver implements Receiver {

    private final Keyboard keyboard;
    private int currentChannel;
    private boolean active;

    public NoteReceiver(Keyboard keyboard) {
        this.keyboard = keyboard;
        active = false;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if(!active) {
            return;
        }

        if(message instanceof ShortMessage shortMessage) {
            if(shortMessage.getChannel() == currentChannel - 1) {
                int key = shortMessage.getData1();
                int velocity = shortMessage.getData2();
                if(shortMessage.getCommand() == ShortMessage.NOTE_ON && velocity > 0) {
                    keyboard.pressKey(key);
                }
                else if(shortMessage.getCommand() == ShortMessage.NOTE_OFF || velocity == 0) {
                    keyboard.releaseKey(key);
                }
            }
        }
    }

    @Override
    public void close() {

    }

    public void setCurrentChannel(int currentChannel) {
        this.currentChannel = currentChannel;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
