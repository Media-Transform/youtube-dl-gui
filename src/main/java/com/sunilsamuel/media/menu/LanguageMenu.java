/**
 * 
 */
package com.sunilsamuel.media.menu;

import java.util.Locale;

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
public class LanguageMenu extends MenuBase {

	/**
	 * @param bundle
	 * @param mainWidget
	 * @param pc
	 */
	public LanguageMenu(MainWidget mainWidget, ProcessConfiguration pc) {
		super(mainWidget, pc);
		menu = new Menu();
		LanguageBinding.bindTextProperty(menu.textProperty(), "menuLanguage");
		menu.setId("menuLanguage");

	}

	@Override
	public Menu process() {
		menu.getItems().addAll(english(), malayalam());
		return menu;
	}

	private MenuItem english() {
		MenuItem menuLanguage1 = new MenuItem();
		LanguageBinding.bindTextProperty(menuLanguage1.textProperty(), "menuLanguage1");
		menuLanguage1.setId("menuLanguage1");

		menuLanguage1.setOnAction(e -> {
			System.out.println("In english");
			Locale.setDefault(Locale.ENGLISH);
			LanguageBinding.setLocale(Locale.ENGLISH);
		});
		return menuLanguage1;

	}

	private MenuItem malayalam() {
		MenuItem menuLanguage1 = new MenuItem();
		LanguageBinding.bindTextProperty(menuLanguage1.textProperty(), "menuLanguage2");
		menuLanguage1.setId("menuLanguage2");

		menuLanguage1.setOnAction(e -> {
			System.out.println("In malayalam");
			Locale malayalam = new Locale("ml", "Malayalam", "\u0D2E\u0D32\u0D2F\u0D3E\u0D33\u0D02");
			Locale.setDefault(malayalam);
			LanguageBinding.setLocale(malayalam);
		});
		return menuLanguage1;

	}

}
