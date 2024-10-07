package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class Keyboard extends StackPane {
    private final String[] whiteKeyNotes =
            {
            "C3", "D3", "E3", "F3", "G3", "A3", "B3",
            "C4", "D4", "E4", "F4", "G4", "A4", "B4",
            "C5", "D5", "E5", "F5", "G5", "A5", "B5"
            };
    private final String[] blackKeyNotes =
            {
            "CS3", "DS3", "FS3", "GS3", "AS3",
            "CS4", "DS4", "FS4", "GS4", "AS4",
            "CS5", "DS5", "FS5", "GS5", "AS5"
            };
    private final int totalWhiteKeys = whiteKeyNotes.length;
    private final int totalBlackKeys = blackKeyNotes.length;
    private final WhiteKey[] whiteKeys = new WhiteKey[totalWhiteKeys];
    private final BlackKey[] blackKeys = new BlackKey[totalBlackKeys];
    private final double whiteKeyWidth = 40.;
    private final double whiteKeyHeight = 120.;
    private final double blackKeyWidth = 20.;
    private final double blackKeyHeight = 60.;


    public Keyboard() {
        HBox whiteKeysContainer = new HBox();

        for (int i = 0; i < totalWhiteKeys; i++) {
            WhiteKey whiteKey = new WhiteKey(whiteKeyNotes[i % whiteKeyNotes.length], whiteKeyWidth, whiteKeyHeight);
            this.whiteKeys[i] = whiteKey;
        }

        HBox blackKeysContainer = new HBox();
        blackKeysContainer.setSpacing(whiteKeyWidth - blackKeyWidth);

        for (int i = 0; i < totalBlackKeys; i++) {
            BlackKey blackKey = new BlackKey(blackKeyNotes[i % blackKeyNotes.length], blackKeyWidth, blackKeyHeight);

            if(i < 2) {
                blackKey.setTranslateX(whiteKeyWidth - blackKeyWidth / 2 + 1 + i * 2);
            }
            else if(i < 5) {
                blackKey.setTranslateX(2 * whiteKeyWidth - blackKeyWidth / 2 + 1 + i * 2);
            }
            else if(i < 7) {
                blackKey.setTranslateX(3 * whiteKeyWidth - blackKeyWidth / 2 + 1 + i * 2);
            }
            else if(i < 10) {
                blackKey.setTranslateX(4 * whiteKeyWidth - blackKeyWidth / 2 + 1 + i * 2);
            }
            else if(i < 12) {
                blackKey.setTranslateX(5 * whiteKeyWidth - blackKeyWidth / 2 + 1 + i * 2);
            }
            else if(i < 15) {
                blackKey.setTranslateX(6 * whiteKeyWidth - blackKeyWidth / 2 + 1 + i * 2);
            }

            this.blackKeys[i] = blackKey;
        }

        whiteKeysContainer.getChildren().addAll(this.whiteKeys);
        blackKeysContainer.getChildren().addAll(this.blackKeys);

        blackKeysContainer.setPickOnBounds(false);

        this.getChildren().addAll(whiteKeysContainer, blackKeysContainer);

        this.getStyleClass().add("keyboard");
    }

    public WhiteKey[] getWhiteKeys() {
        return whiteKeys;
    }

    public BlackKey[] getBlackKeys() {
        return blackKeys;
    }
}
