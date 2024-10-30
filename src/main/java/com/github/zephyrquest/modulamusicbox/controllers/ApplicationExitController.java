package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.MainFX;
import com.github.zephyrquest.modulamusicbox.models.TrackSequencer;
import com.github.zephyrquest.modulamusicbox.models.TrackSynthesizer;
import com.github.zephyrquest.modulamusicbox.views.components.SettingsMenu;
import javafx.application.Platform;

public class ApplicationExitController {
    private final SettingsMenu settingsMenu;
    private final TrackSequencer trackSequencer;
    private final TrackSynthesizer trackSynthesizer;


    public ApplicationExitController(SettingsMenu settingsMenu,
                                     TrackSequencer trackSequencer, TrackSynthesizer trackSynthesizer) {
        this.settingsMenu = settingsMenu;
        this.trackSequencer = trackSequencer;
        this.trackSynthesizer = trackSynthesizer;

        MainFX.stage.setOnCloseRequest(event -> exitApplication());
        this.settingsMenu.getExitMenuItem().setOnAction(event -> exitApplication());
    }

    private void exitApplication() {
        trackSequencer.closeSequencer();
        trackSynthesizer.closeSynthesizer();
        Platform.exit();
    }
}
