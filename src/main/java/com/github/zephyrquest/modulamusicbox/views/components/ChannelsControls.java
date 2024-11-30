package com.github.zephyrquest.modulamusicbox.views.components;

import com.github.zephyrquest.modulamusicbox.models.Channel;
import com.github.zephyrquest.modulamusicbox.models.TrackSynthesizer;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ChannelsControls extends VBox {
    private final ToggleGroup channelButtonsGroup;
    private final List<CheckBox> muteChannelCheckboxes;
    private final List<CheckBox> soloChannelCheckboxes;
    private final List<ComboBox<String>> instrumentCheckboxes;
    private final List<Button> resetInstrumentButtons;

    public ChannelsControls() {
        channelButtonsGroup = new ToggleGroup();
        muteChannelCheckboxes = new ArrayList<>();
        soloChannelCheckboxes = new ArrayList<>();
        instrumentCheckboxes = new ArrayList<>();
        resetInstrumentButtons = new ArrayList<>();

        this.getStyleClass().add("channels-controls");
        hideControls();
    }

    public void updateChannels(List<Channel> channels) {
        List<RadioButton> channelButtons = new ArrayList<>();

        for(var channel : channels) {
            int channelNumber = channel.getNumber();

            HBox channelContainer = new HBox();
            channelContainer.getStyleClass().add("channel-container");

            String id = String.valueOf(channelNumber);

            RadioButton channelRadioButton = new RadioButton("Channel " + (channelNumber + 1));
            channelRadioButton.getStyleClass().add("channel-radio-button");
            channelRadioButton.setId(id);
            channelRadioButton.setToggleGroup(channelButtonsGroup);
            channelButtons.add(channelRadioButton);

            CheckBox muteChannelCheckBox = new CheckBox("Mute");
            muteChannelCheckBox.getStyleClass().add("mute-channel-box");
            muteChannelCheckBox.setId(id);
            muteChannelCheckBox.setSelected(false);
            muteChannelCheckboxes.add(muteChannelCheckBox);

            CheckBox soloChannelCheckBox = new CheckBox("Solo");
            soloChannelCheckBox.getStyleClass().add("solo-channel-box");
            soloChannelCheckBox.setId(id);
            soloChannelCheckBox.setSelected(false);
            soloChannelCheckboxes.add(soloChannelCheckBox);

            ComboBox<String> instrumentComboBox = new ComboBox<>();
            instrumentComboBox.getStyleClass().add("instrument-combo-box");
            instrumentComboBox.setId(id);
            instrumentComboBox.getItems().addAll(TrackSynthesizer.getAllInstrumentNames());
            instrumentComboBox.getSelectionModel().select(channel.getDefaultInstrument());
            instrumentComboBox.setDisable(!channel.isInstrumentEditable());
            instrumentCheckboxes.add(instrumentComboBox);

            channelContainer.getChildren().addAll(
                    channelRadioButton,
                    muteChannelCheckBox,
                    soloChannelCheckBox,
                    instrumentComboBox);

            if(channel.isInstrumentEditable()) {
                Button resetInstrumentButton = new Button("Reset Instrument");
                resetInstrumentButton.getStyleClass().add("reset-instrument-button");
                resetInstrumentButton.setId(id);
                resetInstrumentButtons.add(resetInstrumentButton);

                channelContainer.getChildren().add(resetInstrumentButton);
            }

            this.getChildren().addAll(channelContainer);
        }

        if(!channelButtons.isEmpty()) {
            channelButtonsGroup.selectToggle(channelButtons.get(0));
        }
    }

    public void clearChannels() {
        this.getChildren().clear();
        channelButtonsGroup.getToggles().clear();
        muteChannelCheckboxes.clear();
        soloChannelCheckboxes.clear();
        instrumentCheckboxes.clear();
        resetInstrumentButtons.clear();
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

    public void selectInstrument(String id, String instrumentName) {
        var instrumentComboBoxOpt = instrumentCheckboxes
                .stream()
                .filter(instrumentComboBox -> instrumentComboBox.getId().equals(id))
                .findFirst();

        instrumentComboBoxOpt.ifPresent(comboBox -> comboBox.getSelectionModel().select(instrumentName));
    }

    public void showControls() {
        this.setVisible(true);
    }

    public void hideControls() {
        this.setVisible(false);
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

    public List<ComboBox<String>> getInstrumentCheckboxes() {
        return instrumentCheckboxes;
    }

    public List<Button> getResetInstrumentButtons() {
        return resetInstrumentButtons;
    }
}
