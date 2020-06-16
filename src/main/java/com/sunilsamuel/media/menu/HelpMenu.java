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
public class HelpMenu extends MenuBase {

	/**
	 * @param bundle
	 * @param mainWidget
	 * @param pc
	 */
	public HelpMenu(MainWidget mainWidget, ProcessConfiguration pc) {
		super(mainWidget, pc);
		menu = new Menu();
		LanguageBinding.bindTextProperty(menu.textProperty(), "menuHelp");
		menu.setId("menuHelp");

	}

	@Override
	public Menu process() {
		menu.getItems().addAll(howToDownload(), aboutUs());
		return menu;
	}

	private MenuItem howToDownload() {
		MenuItem howToDownload = new MenuItem();
		LanguageBinding.bindTextProperty(howToDownload.textProperty(), "menuHelp1");
		howToDownload.setId("menuHelp1");

		howToDownload.setOnAction(e -> {
			mainWidget.setHtml("html/media-downloader-help-page.html");
		});
		return howToDownload;
	}

	private MenuItem aboutUs() {
		MenuItem about = new MenuItem();
		LanguageBinding.bindTextProperty(about.textProperty(), "menuHelp2");

		about.setId("menuHelp2");

		about.setOnAction(e -> {
			mainWidget.setAlertInformation("text.about.title", "text.about");
		});
		return about;
	}

}
