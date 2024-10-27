package com.github.zephyrquest.modulamusicbox.views;

import com.github.zephyrquest.modulamusicbox.views.components.ChannelsControls;
import com.github.zephyrquest.modulamusicbox.views.components.FileSelection;
import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;
import com.github.zephyrquest.modulamusicbox.views.components.TrackControls;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainView {
    private final BorderPane borderPane;
    private final Keyboard keyboard;
    private final FileSelection fileSelection;
    private final TrackControls trackControls;
    private final ChannelsControls channelsControls;


    public MainView(BorderPane borderPane, Keyboard keyboard, FileSelection fileSelection,
                    TrackControls trackControls, ChannelsControls channelsControls) {
        this.borderPane = borderPane;
        this.keyboard = keyboard;
        this.fileSelection = fileSelection;
        this.trackControls = trackControls;
        this.channelsControls = channelsControls;
    }

    public void show() {
        VBox mainContainer = new VBox();
        HBox fileSelectionContainer = new HBox();
        HBox keyboardContainer = new HBox();
        HBox trackControlsContainer = new HBox();
        HBox channelsControlsContainer = new HBox();

        mainContainer.getStyleClass().add("main-view-container");
        fileSelectionContainer.getStyleClass().add("file-selection-container");
        keyboardContainer.getStyleClass().add("keyboard-container");
        trackControlsContainer.getStyleClass().add("track-controls-container");
        channelsControlsContainer.getStyleClass().add("channels-controls-container");

        fileSelectionContainer.getChildren().add(fileSelection);
        keyboardContainer.getChildren().add(keyboard);
        trackControlsContainer.getChildren().add(trackControls);
        channelsControlsContainer.getChildren().add(channelsControls);

        mainContainer.getChildren().addAll(fileSelectionContainer, keyboardContainer, trackControlsContainer,
                channelsControlsContainer);

        borderPane.setCenter(mainContainer);
    }
}
