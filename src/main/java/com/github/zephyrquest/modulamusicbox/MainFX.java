package com.github.zephyrquest.modulamusicbox;

import com.github.zephyrquest.modulamusicbox.controllers.*;
import com.github.zephyrquest.modulamusicbox.models.*;
import com.github.zephyrquest.modulamusicbox.views.*;
import com.github.zephyrquest.modulamusicbox.views.components.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainFX extends Application {
    public static Stage stage;

    // Models
    private TrackSynthesizer trackSynthesizer;
    private TrackSequencer trackSequencer;
    private MidiFileManager midiFileManager;
    private NoteReceiverFromSequencer noteReceiverFromSequencer;

    // Controllers
    private ApplicationExitController applicationExitController;
    private TrackController trackController;
    private KeyboardController keyboardController;

    // Views
    private MainView mainView;
    private SettingsMenu settingsMenu;
    private Keyboard keyboard;
    private FileSelection fileSelection;
    private TrackControls trackControls;
    private ChannelsControls channelsControls;

    private Scene scene;
    private BorderPane borderPane;


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setResizable(false);
        stage.setFullScreen(false);

        stage.setTitle("ModulaMusicBox");
        var icon = getClass().getResourceAsStream("/images/modulamusicbox.png");
        if(icon != null) {
            stage.getIcons().add(new Image(icon));
        }

        settingsMenu = new SettingsMenu();
        keyboard = new Keyboard();
        fileSelection = new FileSelection();
        trackControls = new TrackControls();
        channelsControls = new ChannelsControls();

        trackSynthesizer = new TrackSynthesizer();
        trackSequencer = new TrackSequencer();
        midiFileManager = new MidiFileManager();
        noteReceiverFromSequencer = new NoteReceiverFromSequencer(keyboard);

        VBox topContainerVBox = new VBox();
        topContainerVBox.getChildren().add(settingsMenu.getMenuBar());

        borderPane = new BorderPane();
        borderPane.getStyleClass().add("border-pane");

        var backgroundImage = getBackgroundImage();
        if(backgroundImage != null) {
            borderPane.setBackground(new Background(backgroundImage));
        }

        scene = new Scene(borderPane, 1200, 800);
        var styleSheet = getClass().getResource("/styles/style.css");
        if(styleSheet != null) {
            scene.getStylesheets().add(styleSheet.toExternalForm());
        }

        stage.setScene(scene);

        borderPane.setTop(topContainerVBox);
        mainView = new MainView(borderPane, keyboard, fileSelection, trackControls, channelsControls);

        applicationExitController = new ApplicationExitController(settingsMenu,
                trackSequencer, trackSynthesizer);
        trackController = new TrackController(trackSequencer, midiFileManager, trackSynthesizer,
                noteReceiverFromSequencer,
                fileSelection, keyboard, trackControls, channelsControls);
        keyboardController = new KeyboardController(trackSynthesizer, keyboard);

        mainView.show();

       stage.show();
    }

    private BackgroundImage getBackgroundImage() {
        var backgroundStream = getClass().getResourceAsStream("/images/background.jpg");

        if(backgroundStream == null) {
            return null;
        }

        Image backgroundImage = new Image(backgroundStream);
        return new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
    }
}
