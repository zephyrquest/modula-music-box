package com.github.zephyrquest.modulamusicbox.views;

import com.github.zephyrquest.modulamusicbox.views.components.FileSelection;
import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;
import com.github.zephyrquest.modulamusicbox.views.components.TrackControls;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView {
    private final Stage stage;
    private final Scene scene;
    private final BorderPane borderPane;
    private final Keyboard keyboard;
    private final FileSelection fileSelection;
    private final TrackControls trackControls;


    public MainView(Stage stage, Scene scene, BorderPane borderPane, Keyboard keyboard, FileSelection fileSelection,
                    TrackControls trackControls) {
        this.stage = stage;
        this.scene = scene;
        this.borderPane = borderPane;
        this.keyboard = keyboard;
        this.fileSelection = fileSelection;
        this.trackControls = trackControls;
    }

    public void show() {
        VBox mainContainer = new VBox();
        HBox fileSelectionContainer = new HBox();
        HBox keyboardContainer = new HBox();
        HBox trackControlsContainer = new HBox();

        mainContainer.getStyleClass().add("main-view-container");
        fileSelectionContainer.getStyleClass().add("file-selection-container");
        keyboardContainer.getStyleClass().add("keyboard-container");
        trackControlsContainer.getStyleClass().add("track-controls-container");

        fileSelectionContainer.getChildren().add(fileSelection);
        keyboardContainer.getChildren().add(keyboard);
        trackControlsContainer.getChildren().add(trackControls);

        mainContainer.getChildren().addAll(fileSelectionContainer, keyboardContainer, trackControlsContainer);

        borderPane.setCenter(mainContainer);
    }
}
