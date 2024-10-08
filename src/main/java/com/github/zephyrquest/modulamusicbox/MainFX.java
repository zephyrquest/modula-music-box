package com.github.zephyrquest.modulamusicbox;

import com.github.zephyrquest.modulamusicbox.controllers.ApplicationExitController;
import com.github.zephyrquest.modulamusicbox.controllers.KeyboardController;
import com.github.zephyrquest.modulamusicbox.controllers.MidiControlsController;
import com.github.zephyrquest.modulamusicbox.models.FileInputSequencer;
import com.github.zephyrquest.modulamusicbox.models.KeyboardSynthesizer;
import com.github.zephyrquest.modulamusicbox.views.*;
import com.github.zephyrquest.modulamusicbox.views.components.Keyboard;
import com.github.zephyrquest.modulamusicbox.views.components.MidiControls;
import com.github.zephyrquest.modulamusicbox.views.components.NavigationMenu;
import com.github.zephyrquest.modulamusicbox.views.components.SettingsMenu;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainFX extends Application {
    // Models
    private KeyboardSynthesizer keyboardSynthesizer;
    private FileInputSequencer fileInputSequencer;

    // Controllers
    private ApplicationExitController applicationExitController;
    private KeyboardController keyboardController;
    private MidiControlsController midiControlsController;

    // Views
    private SettingsMenu settingsMenu;
    private NavigationMenu navigationMenu;
    private MidiSyncView midiSyncView;
    private GrooveBoxView grooveBoxView;
    private Keyboard keyboard;
    private MidiControls midiControls;

    private Stage stage;
    private Scene scene;
    private BorderPane borderPane;



    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        this.stage.setTitle("ModulaMusicBox");
        var icon = getClass().getResourceAsStream("/images/modulamusicbox.png");
        if(icon != null) {
            this.stage.getIcons().add(new Image(icon));
        }

        keyboardSynthesizer = new KeyboardSynthesizer();
        fileInputSequencer = new FileInputSequencer();

        settingsMenu = new SettingsMenu();
        navigationMenu = new NavigationMenu();
        keyboard = new Keyboard();
        midiControls = new MidiControls();

        HBox navigationMenuContainerHBox = new HBox();
        navigationMenuContainerHBox.setAlignment(Pos.CENTER);
        navigationMenuContainerHBox.getChildren().add(navigationMenu.getMenuBar());

        VBox topContainerVBox = new VBox();
        //topContainerVBox.getChildren().addAll(settingsMenu.getMenuBar(), navigationMenuContainerHBox);
        topContainerVBox.getChildren().add(settingsMenu.getMenuBar());

        borderPane = new BorderPane();

        scene = new Scene(borderPane, 1200, 800);
        var styleSheet = getClass().getResource("/styles/style.css");
        if(styleSheet != null) {
            scene.getStylesheets().add(styleSheet.toExternalForm());
        }

        this.stage.setScene(scene);

        borderPane.setTop(topContainerVBox);
        midiSyncView = new MidiSyncView(this.stage, scene, borderPane, keyboard, midiControls);
        grooveBoxView = new GrooveBoxView(this.stage, scene, borderPane);

        applicationExitController = new ApplicationExitController(this.stage, settingsMenu, keyboardSynthesizer, fileInputSequencer);
        keyboardController = new KeyboardController(keyboard, keyboardSynthesizer);
        midiControlsController = new MidiControlsController(midiControls, keyboardSynthesizer, fileInputSequencer);

        midiSyncView.show();

        this.stage.show();
    }
}
