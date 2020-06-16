package com.sunilsamuel.media.capture;

import com.sunilsamuel.media.menu.DownloadMenu;
import com.sunilsamuel.media.menu.FileMenu;
import com.sunilsamuel.media.menu.HelpMenu;
import com.sunilsamuel.media.menu.LanguageMenu;
import com.sunilsamuel.media.menu.SourcesMenu;
import com.sunilsamuel.media.menu.UtilityMenu;
import com.sunilsamuel.media.util.ProcessConfiguration;

import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;

/**
 * Create the menu system for the application by decoupling the different
 * elements.
 * 
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class CreateMenu {
	private MenuBar menuBar;
	private MenuBar rightMenuBar;
	private MainWidget mainWidget;
	private ProcessConfiguration pc = ProcessConfiguration.getInstance();

	private final VBox mainBox;

	public CreateMenu(VBox mainBox, MainWidget mainWidget) {
		this.mainWidget = mainWidget;
		this.mainBox = mainBox;
	}

	public CreateMenu(VBox mainBox) {
		this.mainBox = mainBox;
	}

	public CreateMenu setMainWidget(MainWidget mainWidget) {
		this.mainWidget = mainWidget;
		return this;
	}

	public CreateMenu process() {
		menu();
		return this;
	}

	private void menu() {
		menuBar = new MenuBar();
		menuBar.setId("menuBar");
		// menuBar.prefWidthProperty().bind(mainBox.widthProperty().subtract(100));
		menuBar.getMenus().addAll( //
				new FileMenu(mainWidget, pc).process(), // File Menu
				new SourcesMenu(mainWidget, pc).process(), // Sources Menu
				new DownloadMenu(mainWidget, pc).process(), // Download Menu
				new LanguageMenu(mainWidget, pc).process(), // Language Menu
				new UtilityMenu(mainWidget, pc).process() // Utility Menu
		);

		Region spacer = new Region();
		/**
		 * Do not change this class name "menu-bar" since the background of the spacer
		 * should be the same as the menu bar.
		 */
		spacer.getStyleClass().add("menu-bar");
		HBox.setHgrow(spacer, Priority.SOMETIMES);

		rightMenuBar = new MenuBar();
		rightMenuBar.setId("rightMenuBar");
		rightMenuBar.getMenus().addAll(new HelpMenu(mainWidget, pc).process());

		/**
		 * This is the VBox that will hold the menuBar.
		 */
		HBox box = new HBox(menuBar, spacer, rightMenuBar);
		box.setId("menu-box");

		mainBox.getChildren().addAll(box);

		mainWidget.setRootPage();
	}
}
