package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.TrackSequencer;
import com.github.zephyrquest.modulamusicbox.views.components.SettingsMenu;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ApplicationExitController {
    private final Stage stage;
    private final SettingsMenu settingsMenu;
    private final TrackSequencer trackSequencer;


    public ApplicationExitController(Stage stage, SettingsMenu settingsMenu, TrackSequencer trackSequencer) {
        this.stage = stage;
        this.settingsMenu = settingsMenu;
        this.trackSequencer = trackSequencer;

        this.stage.setOnCloseRequest(event -> exitApplication());
        this.settingsMenu.getExitMenuItem().setOnAction(event -> exitApplication());
    }

    private void exitApplication() {
        trackSequencer.closeSequencer();
        Platform.exit();
    }
}
