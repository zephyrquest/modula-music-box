package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.KeyboardSynthesizer;
import com.github.zephyrquest.modulamusicbox.views.components.Key;
import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;
import javafx.scene.input.MouseEvent;

public class KeyboardController {
    private final KeyboardSynthesizer keyboardSynthesizer;
    private final Keyboard keyboard;


    public KeyboardController(KeyboardSynthesizer keyboardSynthesizer, Keyboard keyboard) {
        this.keyboardSynthesizer = keyboardSynthesizer;
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
        keyboardSynthesizer.playNode(noteNumber);
    }

    private void releaseKey(Key key, int noteNumber) {
        key.release();
        keyboardSynthesizer.stopNote(noteNumber);
    }
}
