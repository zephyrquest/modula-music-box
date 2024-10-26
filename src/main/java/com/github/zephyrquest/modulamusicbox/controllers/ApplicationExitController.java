package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.KeyboardSynthesizer;
import com.github.zephyrquest.modulamusicbox.models.TrackSequencer;
import com.github.zephyrquest.modulamusicbox.views.components.SettingsMenu;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ApplicationExitController {
    private final Stage stage;
    private final SettingsMenu settingsMenu;
    private final TrackSequencer trackSequencer;
    private final KeyboardSynthesizer keyboardSynthesizer;


    public ApplicationExitController(Stage stage, SettingsMenu settingsMenu,
                                     TrackSequencer trackSequencer, KeyboardSynthesizer keyboardSynthesizer) {
        this.stage = stage;
        this.settingsMenu = settingsMenu;
        this.trackSequencer = trackSequencer;
        this.keyboardSynthesizer = keyboardSynthesizer;

        this.stage.setOnCloseRequest(event -> exitApplication());
        this.settingsMenu.getExitMenuItem().setOnAction(event -> exitApplication());
    }

    private void exitApplication() {
        trackSequencer.closeSequencer();
        keyboardSynthesizer.closeSynthesizer();
        Platform.exit();
    }
}
