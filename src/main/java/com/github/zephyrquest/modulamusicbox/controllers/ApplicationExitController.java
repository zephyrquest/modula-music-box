package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.KeyboardSynthesizer;
import com.github.zephyrquest.modulamusicbox.views.components.SettingsMenu;
import javafx.application.Platform;

public class ApplicationExitController {
    private final SettingsMenu settingsMenu;
    private final KeyboardSynthesizer keyboardSynthesizer;


    public ApplicationExitController(SettingsMenu settingsMenu, KeyboardSynthesizer keyboardSynthesizer) {
        this.settingsMenu = settingsMenu;
        this.keyboardSynthesizer = keyboardSynthesizer;

        this.settingsMenu.getExitMenuItem().setOnAction(event -> exitApplication());
    }

    private void exitApplication() {
        keyboardSynthesizer.closeSynthesizer();
        Platform.exit();
    }
}
