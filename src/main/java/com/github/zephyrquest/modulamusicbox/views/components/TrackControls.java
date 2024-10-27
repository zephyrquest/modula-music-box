package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TrackControls extends VBox {
    private final Button playButton;
    private final Button stopButton;
    private final Button rewindButton;


    public TrackControls() {
        var playIconStream = getClass().getResourceAsStream("/icons/play-24.png");
        if(playIconStream != null) {
            Image playIcon = new Image(playIconStream);
            ImageView playImageView = new ImageView(playIcon);
            playButton = new Button("Play", playImageView);
        }
        else {
            playButton = new Button("Play");
        }
        playButton.getStyleClass().add("play-button");

        var stopIconStream = getClass().getResourceAsStream("/icons/pause-24.png");
        if(stopIconStream != null) {
            Image stopIcon = new Image(stopIconStream);
            ImageView stopImageView = new ImageView(stopIcon);
            stopButton = new Button("Stop", stopImageView);
        }
        else {
            stopButton = new Button("Stop");
        }
        stopButton.getStyleClass().add("stop-button");

        var rewindIconStream = getClass().getResourceAsStream("/icons/rewind-24.png");
        if(rewindIconStream != null) {
            Image rewindIcon = new Image(rewindIconStream);
            ImageView rewindImageView = new ImageView(rewindIcon);
            rewindButton = new Button("Rewind", rewindImageView);
        }
        else {
            rewindButton = new Button("Rewind");
        }
        rewindButton.getStyleClass().add("rewind-button");

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
