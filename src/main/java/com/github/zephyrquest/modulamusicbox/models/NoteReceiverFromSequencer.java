package com.github.zephyrquest.modulamusicbox.models;

import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;

import javax.sound.midi.*;

public class NoteReceiverFromSequencer implements Receiver {

    private final Keyboard keyboard;
    private int currentChannelNumber;
    private boolean active;

    public NoteReceiverFromSequencer(Keyboard keyboard) {
        this.keyboard = keyboard;
        currentChannelNumber = -1;
        active = false;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if(!active) {
            return;
        }

        if(message instanceof ShortMessage shortMessage) {
            if(shortMessage.getChannel() == currentChannelNumber) {
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

    public void setCurrentChannelNumber(int currentChannelNumber) {
        this.currentChannelNumber = currentChannelNumber;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
