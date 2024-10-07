package com.github.zephyrquest.modulamusicbox.views.components;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class SettingsMenu {
    private final MenuBar menuBar;
    private final Menu appMenu;
    private final MenuItem exitMenuItem;


    public SettingsMenu() {
        menuBar = new MenuBar();
        appMenu = new Menu("App");
        exitMenuItem = new MenuItem("Exit");

        appMenu.getItems().add(exitMenuItem);
        menuBar.getMenus().add(appMenu);
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public MenuItem getExitMenuItem() {
        return exitMenuItem;
    }
}
