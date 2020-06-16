/**
 * 
 */
package com.sunilsamuel.media.menu;

import com.sunilsamuel.media.capture.MainWidget;
import com.sunilsamuel.media.util.ProcessConfiguration;

import javafx.scene.control.Menu;

/**
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public abstract class MenuBase {
	// protected final ResourceBundle bundle;
	protected final MainWidget mainWidget;
	protected final ProcessConfiguration pc;
	protected Menu menu;

	protected final String[] ffmpegVerbosityLevels = new String[] { "quiet", "debug", "verbose", "info", "warning",
			"error", "fatal", "panic" };

	protected final String[] youtubeDlVerbosityLevels = new String[] { "debug", "normal" };

	// public MenuBase(ResourceBundle bundle, MainWidget mainWidget,
	// ProcessConfiguration pc) {
	public MenuBase(MainWidget mainWidget, ProcessConfiguration pc) {
		// this.bundle = bundle;
		this.mainWidget = mainWidget;
		this.pc = pc;
	}

	public abstract Menu process();
}
