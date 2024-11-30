package com.github.zephyrquest.modulamusicbox.views.components;

import com.github.zephyrquest.modulamusicbox.utilities.TimeFormatter;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TrackControls extends VBox {
    private final Button playButton;
    private final Button stopButton;
    private final Button rewindButton;
    private final TextField bpmTextField;
    private final Label bpmLabel;
    private final Slider trackTimelineSlider;
    private final Label trackTimelineLabel;
    private final Label trackLengthLabel;
    private final Timeline trackTimeline;
    private final TimeFormatter timeFormatter;
    private boolean trackTimelineSliderEditable;


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

        bpmLabel = new Label("BPM");
        bpmLabel.getStyleClass().add("bpm-label");
        bpmTextField = new TextField();
        bpmTextField.getStyleClass().add("bpm-text-field");

        HBox buttonsContainer = new HBox();
        buttonsContainer.getStyleClass().add("buttons-container");
        buttonsContainer.getChildren().addAll(rewindButton, playButton, stopButton);

        HBox bpmContainer = new HBox();
        bpmContainer.getStyleClass().add("bpm-container");
        bpmContainer.getChildren().addAll(bpmLabel, bpmTextField);

        timeFormatter = new TimeFormatter();

        trackTimelineSlider = new Slider();
        trackTimelineSlider.getStyleClass().add("track-timeline-slider");
        trackTimelineSlider.setMin(0);
        trackTimelineSlider.setBlockIncrement(1);
        trackTimelineSliderEditable = true;

        trackTimelineLabel = new Label();
        trackLengthLabel = new Label();

        HBox trackTimeLineLabelContainer = new HBox();
        trackTimeLineLabelContainer.getStyleClass().add("track-timeline-label-container");
        trackTimeLineLabelContainer.getChildren().addAll(trackTimelineLabel, trackLengthLabel);

        HBox trackTimeLineContainer = new HBox();
        trackTimeLineContainer.getStyleClass().add("track-timeline-container");
        trackTimeLineContainer.getChildren().addAll(trackTimelineSlider, trackTimeLineLabelContainer);

        trackTimeline = new Timeline();
        trackTimeline.setCycleCount(Timeline.INDEFINITE);

        this.getChildren().addAll(buttonsContainer, bpmContainer, trackTimeLineContainer);
        this.getStyleClass().add("track-controls");

        hideTrackControls();
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

    public TextField getBpmTextField() {
        return bpmTextField;
    }

    public Timeline getTrackTimeline() {
        return trackTimeline;
    }

    public Slider getTrackTimelineSlider() {
        return trackTimelineSlider;
    }

    public void setTrackTimelineSliderEditable(boolean trackTimelineSliderEditable) {
        this.trackTimelineSliderEditable = trackTimelineSliderEditable;
    }

    public void updateBpm(int bpm) {
        bpmTextField.setText(String.valueOf(bpm));
    }

    public void showTrackControls() {
        playButton.setVisible(true);
        rewindButton.setVisible(true);
        stopButton.setVisible(true);
        bpmLabel.setVisible(true);
        bpmTextField.setVisible(true);
        trackTimelineSlider.setVisible(true);
        trackTimelineLabel.setVisible(true);
    }

    public void hideTrackControls() {
        playButton.setVisible(false);
        rewindButton.setVisible(false);
        stopButton.setVisible(false);
        bpmLabel.setVisible(false);
        bpmTextField.setVisible(false);
        trackTimelineSlider.setVisible(false);
        trackTimelineLabel.setVisible(false);
    }

    public void playTrack() {
        playButton.setDisable(true);
        stopButton.setDisable(false);

        trackTimeline.play();
    }

    public void stopTrack() {
        stopButton.setDisable(true);
        playButton.setDisable(false);

        trackTimeline.stop();
    }

    public void updateTrackTimeline(int seconds) {
        if(trackTimelineSliderEditable) {
            trackTimelineSlider.setValue(seconds);
        }

        trackTimelineLabel.setText(timeFormatter.format(seconds));
    }

    public void setTrackLength(int seconds) {
        trackTimelineSlider.setMax(seconds);
        trackLengthLabel.setText("/" + timeFormatter.format(seconds));
    }
}
