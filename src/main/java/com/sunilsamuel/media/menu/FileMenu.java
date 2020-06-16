/**
 * 
 */
package com.sunilsamuel.media.menu;

import java.io.File;

import com.sunilsamuel.media.capture.MainWidget;
import com.sunilsamuel.media.util.LanguageBinding;
import com.sunilsamuel.media.util.ProcessConfiguration;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;

/**
 * Create the file menu and return the menu that will be added to the menubar.
 * 
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class FileMenu extends MenuBase {

	public FileMenu(MainWidget mainWidget, ProcessConfiguration pc) {
		super(mainWidget, pc);
		menu = new Menu();
		LanguageBinding.bindTextProperty(menu.textProperty(), "menu1");
		menu.setId("menu1");
	}

	@Override
	public Menu process() {
		menu.getItems().addAll(createHomeMenu(), createPreferencesMenu(), createExitMenu());
		return menu;
	}

	/**
	 * Create home menu.
	 * 
	 * @return
	 */
	private MenuItem createHomeMenu() {
		MenuItem menu11 = new MenuItem();
		LanguageBinding.bindTextProperty(menu11.textProperty(), "menu11");

		menu11.setId("menu11");
		menu11.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
		menu11.setOnAction(e -> {
			mainWidget.setRootPage();
		});
		return menu11;
	}

	private Menu createPreferencesMenu() {
		Menu menu12 = new Menu();
		LanguageBinding.bindTextProperty(menu12.textProperty(), "menu12");

		menu12.setId("menu12");

		selectFfmpeg(menu12);
		selectPython(menu12);
		selectYoutubeDl(menu12);
		return menu12;
	}

	private MenuItem createExitMenu() {
		MenuItem menu13 = new MenuItem();
		menu13.setId("menu13");
		LanguageBinding.bindTextProperty(menu13.textProperty(), "menu13");

		menu13.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));

		/**
		 * Set onAction when the user selects to Exit.
		 */
		menu13.setOnAction(e -> {
			ButtonType button = mainWidget.setAlertConfirm("exit.title", "exit.content");
			if (button != null && button == ButtonType.OK) {
				System.exit(0);
			}
		});
		return menu13;
	}

	/**
	 * @param menu12
	 */
	private void selectFfmpeg(Menu menu12) {
		/**
		 * Select ffmpeg
		 */
		MenuItem menu121 = new MenuItem();
		menu121.setId("menu121");
		LanguageBinding.bindTextProperty(menu121.textProperty(), "menu121");

		menu121.setAccelerator(new KeyCodeCombination(KeyCode.M, KeyCombination.CONTROL_DOWN));
		menu121.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			if (pc.getConfiguration().getFfmpeg() != null) {
				fileChooser.setInitialDirectory(pc.getConfiguration().getFfmpeg().getParentFile());
			}
			File pickedFile = fileChooser.showOpenDialog(null);
			if (pickedFile != null) {
				pc.getConfiguration().setFfmpeg(pickedFile);
				pc.writeConfiguration();
			}
		});
		menu12.getItems().add(menu121);
	}

	private void selectPython(Menu menu12) {
		MenuItem menu122 = new MenuItem();
		menu122.setId("menu122");
		LanguageBinding.bindTextProperty(menu122.textProperty(), "menu122");

		menu122.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
		menu122.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			if (pc.getConfiguration().getPython() != null) {
				fileChooser.setInitialDirectory(pc.getConfiguration().getPython().getParentFile());
			}
			File pickedFile = fileChooser.showOpenDialog(null);
			if (pickedFile != null) {
				pc.getConfiguration().setPython(pickedFile);
				pc.writeConfiguration();
			}
		});
		menu12.getItems().add(menu122);
	}

	private void selectYoutubeDl(Menu menu12) {
		/**
		 * Select youtube-dl
		 */
		MenuItem menu123 = new MenuItem();
		LanguageBinding.bindTextProperty(menu123.textProperty(), "menu123");

		menu123.setId("menu123");
		menu123.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
		menu123.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			if (pc.getConfiguration().getYoutubeDl() != null) {
				fileChooser.setInitialDirectory(pc.getConfiguration().getYoutubeDl().getParentFile());
			}
			File pickedFile = fileChooser.showOpenDialog(null);
			if (pickedFile != null) {
				pc.getConfiguration().setYoutubeDl(pickedFile);
				pc.writeConfiguration();
			}
		});
		menu12.getItems().add(menu123);
	}

}
