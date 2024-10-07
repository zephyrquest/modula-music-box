package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class MidiControls extends GridPane {
    private final ComboBox<Integer> channelComboBox;
    private final ComboBox<String> instrumentComboBox;
    private final Slider velocitySlider;
    private final Spinner<Integer> velocitySpinner;


    public MidiControls() {
        channelComboBox = new ComboBox<>();
        channelComboBox.getStyleClass().add("channel-combo-box");
        instrumentComboBox = new ComboBox<>();
        instrumentComboBox.getStyleClass().add("instrument-combo-box");
        velocitySlider = new Slider();
        velocitySlider.setMin(0);
        velocitySlider.setMax(255.);
        velocitySlider.setShowTickMarks(false);
        velocitySlider.setShowTickLabels(false);
        velocitySlider.setMajorTickUnit(1.);
        velocitySlider.setBlockIncrement(1.);
        velocitySlider.setSnapToTicks(true);

        velocitySpinner = new Spinner<>();
        velocitySpinner.getStyleClass().add("velocity-spinner");
        velocitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 255));

        Label channelLabel = new Label("Channel");
        Label instrumentLabel = new Label("Instrument");
        Label velocityLabel = new Label("Velocity");
        HBox velocityContainer = new HBox();
        velocityContainer.getStyleClass().add("velocity-container");
        velocityContainer.getChildren().addAll(velocitySlider, velocitySpinner);

        this.add(channelLabel, 0, 0);
        this.add(channelComboBox, 0, 1);
        this.add(instrumentLabel, 1, 0);
        this.add(instrumentComboBox, 1, 1);
        this.add(velocityLabel, 2, 0);
        this.add(velocityContainer, 2, 1);

        this.getStyleClass().add("midi-controls");
    }

    public ComboBox<Integer> getChannelComboBox() {
        return channelComboBox;
    }

    public ComboBox<String> getInstrumentComboBox() {
        return instrumentComboBox;
    }

    public Slider getVelocitySlider() {
        return velocitySlider;
    }

    public Spinner<Integer> getVelocitySpinner() {
        return velocitySpinner;
    }
}
