package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.TrackSequencer;
import com.github.zephyrquest.modulamusicbox.views.components.TrackControls;

public class TrackControlsController {
    private final TrackControls trackControls;
    private final TrackSequencer trackSequencer;


    public TrackControlsController(TrackControls trackControls, TrackSequencer trackSequencer) {
        this.trackControls = trackControls;
        this.trackSequencer = trackSequencer;

        setTrackControlsInView();
    }

    private void setTrackControlsInView() {
        var playButton = trackControls.getPlayButton();
        var stopButton = trackControls.getStopButton();
        var rewindButton = trackControls.getRewindButton();

        playButton.setOnAction(event -> trackSequencer.startSequencer());
        stopButton.setOnAction(event -> trackSequencer.stopSequencer());
        rewindButton.setOnAction(event -> trackSequencer.rewindSequencer());
    }
}
