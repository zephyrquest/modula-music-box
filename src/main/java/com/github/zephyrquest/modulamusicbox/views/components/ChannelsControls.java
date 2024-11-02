package com.github.zephyrquest.modulamusicbox.views.components;

import com.github.zephyrquest.modulamusicbox.models.Channel;
import javafx.scene.control.CheckBox;
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
    private final List<CheckBox> muteChannelCheckboxes;
    private final List<CheckBox> soloChannelCheckboxes;

    public ChannelsControls() {
        channelButtonsGroup = new ToggleGroup();
        muteChannelCheckboxes = new ArrayList<>();
        soloChannelCheckboxes = new ArrayList<>();
    }

    public void updateView(Map<Integer, Channel> channels) {
        this.getChildren().clear();
        channelButtonsGroup.getToggles().clear();
        muteChannelCheckboxes.clear();
        soloChannelCheckboxes.clear();

        List<RadioButton> channelButtons = new ArrayList<>();

        for(var entry : channels.entrySet()) {
            HBox channelContainer = new HBox();
            channelContainer.getStyleClass().add("channel-container");

            String id = String.valueOf(entry.getKey());

            RadioButton channelRadioButton = new RadioButton("Channel " + (entry.getKey() + 1));
            channelRadioButton.getStyleClass().add("channel-radio-button");
            channelRadioButton.setId(id);
            channelRadioButton.setToggleGroup(channelButtonsGroup);
            channelButtons.add(channelRadioButton);

            Label instrumentLabel = new Label();
            instrumentLabel.getStyleClass().add("instrument-label");
            StringBuilder sb = new StringBuilder();
            for(var instrumentName : entry.getValue().getInstruments()) {
                sb.append(instrumentName).append("\t");
            }
            instrumentLabel.setText(sb.toString());

            CheckBox muteChannelCheckBox = new CheckBox("Mute");
            muteChannelCheckBox.setId(id);
            muteChannelCheckBox.setSelected(false);
            muteChannelCheckboxes.add(muteChannelCheckBox);

            CheckBox soloChannelCheckBox = new CheckBox("Solo");
            soloChannelCheckBox.setId(id);
            soloChannelCheckBox.setSelected(false);
            soloChannelCheckboxes.add(soloChannelCheckBox);

            channelContainer.getChildren().addAll(channelRadioButton, muteChannelCheckBox, soloChannelCheckBox, instrumentLabel);
            this.getChildren().addAll(channelContainer);
        }

        if(!channelButtons.isEmpty()) {
            channelButtonsGroup.selectToggle(channelButtons.get(0));
        }
    }

    public void selectAllMuteChannelCheckboxes() {
        for(var muteChannelCheckbox : muteChannelCheckboxes) {
            muteChannelCheckbox.setSelected(true);
        }
    }

    public void selectMuteChannel(String id) {
        var muteChannelCheckBoxOpt = muteChannelCheckboxes
                .stream()
                .filter(muteChannelCheckBox -> muteChannelCheckBox.getId().equals(id))
                .findFirst();

        muteChannelCheckBoxOpt.ifPresent(checkBox -> checkBox.setSelected(true));
    }

    public void deselectMuteChannel(String id) {
        var muteChannelCheckBoxOpt = muteChannelCheckboxes
                .stream()
                .filter(muteChannelCheckBox -> muteChannelCheckBox.getId().equals(id))
                .findFirst();

        muteChannelCheckBoxOpt.ifPresent(checkBox -> checkBox.setSelected(false));
    }

    public void deselectAllMuteChannelCheckboxes() {
        for(var muteChannelCheckbox : muteChannelCheckboxes) {
            muteChannelCheckbox.setSelected(false);
        }
    }

    public void enableAllMuteChannelCheckboxes() {
        for(var muteChannelCheckbox : muteChannelCheckboxes) {
            muteChannelCheckbox.setDisable(false);
        }
    }

    public void disableAllMuteChannelCheckboxes() {
        for(var muteChannelCheckbox : muteChannelCheckboxes) {
            muteChannelCheckbox.setDisable(true);
        }
    }

    public void selectAllSoloChannelCheckboxes() {
        for(var soloChannelCheckbox : soloChannelCheckboxes) {
            soloChannelCheckbox.setSelected(true);
        }
    }

    public void selectSoloChannel(String id) {
        var soloChannelCheckBoxOpt = soloChannelCheckboxes
                .stream()
                .filter(soloChannelCheckBox -> soloChannelCheckBox.getId().equals(id))
                .findFirst();

        soloChannelCheckBoxOpt.ifPresent(checkBox -> checkBox.setSelected(true));
    }

    public void deselectAllSoloChannelCheckboxes() {
        for(var soloChannelCheckbox : soloChannelCheckboxes) {
            soloChannelCheckbox.setSelected(false);
        }
    }

    public ToggleGroup getChannelButtonsGroup() {
        return channelButtonsGroup;
    }

    public List<CheckBox> getMuteChannelCheckboxes() {
        return muteChannelCheckboxes;
    }

    public List<CheckBox> getSoloChannelCheckboxes() {
        return soloChannelCheckboxes;
    }
}
