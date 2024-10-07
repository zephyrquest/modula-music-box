package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class NavigationMenu {
    private final MenuBar menuBar;
    private final Menu midiSyncMenu;
    private final Menu grooveBoxMenu;


    public NavigationMenu() {
        menuBar = new MenuBar();
        midiSyncMenu = new Menu("MIDI Sync");
        grooveBoxMenu = new Menu("Groove Box");

        menuBar.getMenus().add(midiSyncMenu);
        menuBar.getMenus().add(grooveBoxMenu);

        menuBar.getStyleClass().add("navigation-menu");
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }
}
