package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.TrackSynthesizer;
import com.github.zephyrquest.modulamusicbox.views.components.Key;
import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;
import javafx.scene.input.MouseEvent;

public class KeyboardController {
    private final TrackSynthesizer trackSynthesizer;
    private final Keyboard keyboard;


    public KeyboardController(TrackSynthesizer trackSynthesizer,
                              Keyboard keyboard) {
        this.trackSynthesizer = trackSynthesizer;
        this.keyboard = keyboard;

        setKeyboardInView();
    }

    private void setKeyboardInView() {
        keyboard.getKeys().forEach((noteNumber, key) -> {
            key.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> pressKey(key, noteNumber));
            key.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> releaseKey(key, noteNumber));
        });
    }

    private void pressKey(Key key, int noteNumber) {
        key.press();
        trackSynthesizer.playNode(noteNumber);
    }

    private void releaseKey(Key key, int noteNumber) {
        key.release();
        trackSynthesizer.stopNote(noteNumber);
    }
}
