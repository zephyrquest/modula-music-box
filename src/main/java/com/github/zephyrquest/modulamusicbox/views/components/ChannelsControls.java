package com.github.zephyrquest.modulamusicbox.views.components;

import com.github.zephyrquest.modulamusicbox.models.Channel;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChannelsControls extends VBox {
    private final ToggleGroup channelButtonsGroup;

    public ChannelsControls() {
        channelButtonsGroup = new ToggleGroup();
    }

    public void updateView(Map<Integer, Channel> channels) {
        this.getChildren().clear();
        channelButtonsGroup.getToggles().clear();
        List<RadioButton> channelButtons = new ArrayList<>();

        for(var entry : channels.entrySet()) {
            HBox channelContainer = new HBox();
            channelContainer.getStyleClass().add("channel-container");

            RadioButton radioButton = new RadioButton("Channel " + (entry.getKey() + 1));
            radioButton.setId(String.valueOf(entry.getKey()));
            radioButton.setToggleGroup(channelButtonsGroup);
            channelButtons.add(radioButton);

            Label instrumentsLabel = new Label();
            instrumentsLabel.setText(entry.getValue().getInstrument());

            channelContainer.getChildren().addAll(radioButton, instrumentsLabel);
            this.getChildren().addAll(channelContainer);
        }

        channelButtonsGroup.selectToggle(channelButtons.get(0));
    }

    public ToggleGroup getChannelButtonsGroup() {
        return channelButtonsGroup;
    }
}
