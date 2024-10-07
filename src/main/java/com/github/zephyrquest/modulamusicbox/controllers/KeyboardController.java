package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.KeyboardSynthesizer;
import com.github.zephyrquest.modulamusicbox.views.components.Key;
import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;
import javafx.scene.input.MouseEvent;


public class KeyboardController {
    private final Keyboard keyboard;
    private final KeyboardSynthesizer keyboardSynthesizer;


    public KeyboardController(Keyboard keyboard, KeyboardSynthesizer keyboardSynthesizer) {
        this.keyboard = keyboard;
        this.keyboardSynthesizer = keyboardSynthesizer;

        for (var whiteKey: this.keyboard.getWhiteKeys()) {
            whiteKey.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> keyPressed(whiteKey));
            whiteKey.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> keyReleased(whiteKey));
        }

        for (var blackKey: this.keyboard.getBlackKeys()) {
            blackKey.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> keyPressed(blackKey));
            blackKey.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> keyPressed(blackKey));
        }
    }

    private void keyPressed(Key key) {
        String note = key.getNote();

        keyboardSynthesizer.playNode(note);
    }

    private void keyReleased(Key key) {
        String note = key.getNote();

        keyboardSynthesizer.stopNote(note);
    }
}
