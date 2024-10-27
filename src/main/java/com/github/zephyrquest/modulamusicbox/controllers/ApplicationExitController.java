package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.MainFX;
import com.github.zephyrquest.modulamusicbox.models.KeyboardSynthesizer;
import com.github.zephyrquest.modulamusicbox.models.TrackSequencer;
import com.github.zephyrquest.modulamusicbox.views.components.SettingsMenu;
import javafx.application.Platform;

public class ApplicationExitController {
    private final SettingsMenu settingsMenu;
    private final TrackSequencer trackSequencer;
    private final KeyboardSynthesizer keyboardSynthesizer;


    public ApplicationExitController(SettingsMenu settingsMenu,
                                     TrackSequencer trackSequencer, KeyboardSynthesizer keyboardSynthesizer) {
        this.settingsMenu = settingsMenu;
        this.trackSequencer = trackSequencer;
        this.keyboardSynthesizer = keyboardSynthesizer;

        MainFX.stage.setOnCloseRequest(event -> exitApplication());
        this.settingsMenu.getExitMenuItem().setOnAction(event -> exitApplication());
    }

    private void exitApplication() {
        trackSequencer.closeSequencer();
        keyboardSynthesizer.closeSynthesizer();
        Platform.exit();
    }
}
