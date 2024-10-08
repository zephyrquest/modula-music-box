package com.github.zephyrquest.modulamusicbox.controllers;

import com.github.zephyrquest.modulamusicbox.models.FileInputSequencer;
import com.github.zephyrquest.modulamusicbox.models.KeyboardSynthesizer;
import com.github.zephyrquest.modulamusicbox.views.components.SettingsMenu;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ApplicationExitController {
    private final Stage stage;
    private final SettingsMenu settingsMenu;
    private final KeyboardSynthesizer keyboardSynthesizer;
    private final FileInputSequencer fileInputSequencer;


    public ApplicationExitController(Stage stage, SettingsMenu settingsMenu, KeyboardSynthesizer keyboardSynthesizer,
                                     FileInputSequencer fileInputSequencer) {
        this.stage = stage;
        this.settingsMenu = settingsMenu;
        this.keyboardSynthesizer = keyboardSynthesizer;
        this.fileInputSequencer = fileInputSequencer;

        this.stage.setOnCloseRequest(event -> exitApplication());
        this.settingsMenu.getExitMenuItem().setOnAction(event -> exitApplication());
    }

    private void exitApplication() {
        keyboardSynthesizer.closeSynthesizer();
        fileInputSequencer.closeSequencer();
        Platform.exit();
    }
}
