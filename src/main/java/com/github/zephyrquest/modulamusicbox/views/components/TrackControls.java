package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TrackControls extends VBox {
    private final Button playButton;
    private final Button stopButton;
    private final Button rewindButton;


    public TrackControls() {
        playButton = new Button("Play");
        stopButton = new Button("Stop");
        rewindButton = new Button("Rewind");

        HBox buttonsContainer = new HBox();
        buttonsContainer.getStyleClass().add("buttons-container");
        buttonsContainer.getChildren().addAll(rewindButton, playButton, stopButton);

        this.getChildren().add(buttonsContainer);
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public Button getRewindButton() {
        return rewindButton;
    }
}
