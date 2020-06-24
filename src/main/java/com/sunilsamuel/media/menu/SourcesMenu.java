/**
 * 
 */
package com.sunilsamuel.media.menu;

import com.sunilsamuel.media.capture.MainWidget;
import com.sunilsamuel.media.util.LanguageBinding;
import com.sunilsamuel.media.util.ProcessConfiguration;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class SourcesMenu extends MenuBase {
	public SourcesMenu(MainWidget mainWidget, ProcessConfiguration pc) {
		super(mainWidget, pc);
		menu = new Menu();
		LanguageBinding.bindTextProperty(menu.textProperty(), "menu2");
		menu.setId("menu2");
	}

	@Override
	public Menu process() {
		menu.getItems().addAll(createGenericUrl(), createYouTubeMenu(), createTubiTvMenu());
		return menu;
	}

	private MenuItem createGenericUrl() {
		MenuItem directUrl = new MenuItem();
		LanguageBinding.bindTextProperty(directUrl.textProperty(), "menu20");

		directUrl.setId("menu20");

		directUrl.setOnAction(e -> {
			String url = mainWidget.setDialog("text.youtube.dialog.title", "text.youtube.dialog.text",
					LanguageBinding.get("text.download.ytv.url.example"));
			if (url != null) {
				mainWidget.setUrl(url);
			}
		});
		return directUrl;

	}

	/**
	 * Create the YouTube Links
	 * 
	 * @return
	 */
	private Menu createYouTubeMenu() {
		Menu menuYouTube = new Menu();
		LanguageBinding.bindTextProperty(menuYouTube.textProperty(), "menu21");
		menuYouTube.setId("menu21");

		MenuItem menu211 = new MenuItem();
		LanguageBinding.bindTextProperty(menu211.textProperty(), "menu211");

		menu211.setId("menu211");
		menu211.setOnAction(e -> {
			mainWidget.setUrl("https://www.youtube.com");
		});

		MenuItem menu212 = new MenuItem();
		LanguageBinding.bindTextProperty(menu212.textProperty(), "menu212");

		menu212.setId("menu212");
		menu212.setOnAction(e -> {
			String url = mainWidget.setDialog("text.youtube.dialog.title", "text.youtube.dialog.text",
					LanguageBinding.get("text.download.ytv.url.example"));
			if (url != null) {
				mainWidget.setUrl(url);
			}
		});

		menuYouTube.getItems().addAll(menu211, menu212);
		return menuYouTube;
	}

	/**
	 * Create the TubiTv Links
	 * 
	 * @return
	 */
	private Menu createTubiTvMenu() {
		Menu menuTubiTvMenu = new Menu();
		menuTubiTvMenu.setId("menu22");
		LanguageBinding.bindTextProperty(menuTubiTvMenu.textProperty(), "menu22");

		MenuItem menu221 = new MenuItem();
		LanguageBinding.bindTextProperty(menu221.textProperty(), "menu221");
		menu221.setId("menu221");

		menu221.setOnAction(e -> {
			mainWidget.setUrl("https://www.tubitv.com");
		});

		MenuItem menu222 = new MenuItem();
		LanguageBinding.bindTextProperty(menu222.textProperty(), "menu222");

		menu222.setOnAction(e -> {
			String url = mainWidget.setDialog("text.tubitv.dialog.title", "text.tubitv.dialog.text",
					"https://tubitv.com/movies/521967/absolute_deception");
			if (url != null) {
				mainWidget.setUrl(url);
			}
		});

		menuTubiTvMenu.getItems().addAll(menu221, menu222);
		return menuTubiTvMenu;
	}

}
