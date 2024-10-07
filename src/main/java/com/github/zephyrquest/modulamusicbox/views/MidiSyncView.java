package com.github.zephyrquest.modulamusicbox.views;

import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;
import com.github.zephyrquest.modulamusicbox.views.components.MidiControls;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MidiSyncView extends BaseView {
    private final Keyboard keyboard;
    private final MidiControls midiControls;

    public MidiSyncView(Stage stage, Scene scene, BorderPane borderPane,
                        Keyboard keyboard, MidiControls midiControls) {
        super(stage, scene, borderPane);

        this.keyboard = keyboard;
        this.midiControls = midiControls;
    }

    @Override
    public void show() {
        VBox mainContainer = new VBox();
        HBox keyboardContainer = new HBox();
        HBox midiControlsContainer = new HBox();

        mainContainer.getStyleClass().add("midi-sync-container");
        keyboardContainer.getStyleClass().add("keyboard-container");
        midiControlsContainer.getStyleClass().add("midi-controls-container");

        keyboardContainer.getChildren().add(keyboard);
        midiControlsContainer.getChildren().add(midiControls);

        mainContainer.getChildren().addAll(keyboardContainer, midiControlsContainer);

        borderPane.setCenter(mainContainer);
    }
}
