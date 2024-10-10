package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.sound.midi.Track;

public class ChannelsControls extends VBox {
    private final ToggleGroup channelButtonsGroup;
    private RadioButton[] channelButtons;

    public ChannelsControls() {
        channelButtonsGroup = new ToggleGroup();
    }

    public void updateView(Track[] tracks) {
        this.getChildren().clear();
        channelButtonsGroup.getToggles().clear();
        channelButtons = new RadioButton[tracks.length];

        for(int i = 0; i < tracks.length; i++) {
            Track track = tracks[i];

            HBox channelContainer = new HBox();

            channelButtons[i] = new RadioButton("Channel " + (i + 1));
            channelButtons[i].setId(String.valueOf(i + 1));
            channelButtons[i].setToggleGroup(channelButtonsGroup);

            channelContainer.getChildren().add(channelButtons[i]);
            this.getChildren().add(channelContainer);
        }

        channelButtonsGroup.selectToggle(channelButtons[0]);
    }

    public ToggleGroup getChannelButtonsGroup() {
        return channelButtonsGroup;
    }
}
