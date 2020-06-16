/**
 * 
 */
package com.sunilsamuel.media.menu;

import java.util.Map;

import com.sunilsamuel.media.capture.CreateForm;
import com.sunilsamuel.media.capture.MainWidget;
import com.sunilsamuel.media.download.TubiTv;
import com.sunilsamuel.media.download.YouTube;
import com.sunilsamuel.media.download.YouTube.Type;
import com.sunilsamuel.media.model.Store;
import com.sunilsamuel.media.util.LanguageBinding;
import com.sunilsamuel.media.util.ProcessConfiguration;
import com.sunilsamuel.media.util.RunCommand;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class DownloadMenu extends MenuBase {
	public DownloadMenu(MainWidget mainWidget, ProcessConfiguration pc) {
		super(mainWidget, pc);
		menu = new Menu();
		LanguageBinding.bindTextProperty(menu.textProperty(), "menu3");

		menu.setId("menu3");

	}

	@Override
	public Menu process() {
		menu.getItems().addAll(youTubeLive(), youTubeVideo(), tubiTvVideo());
		return menu;
	}

	private MenuItem youTubeLive() {
		MenuItem menu31 = new MenuItem();
		LanguageBinding.bindTextProperty(menu31.textProperty(), "menu31");

		menu31.setId("menu31");

		menu31.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
		menu31.setOnAction(e -> {
			if (pc.getConfiguration().getFfmpeg() == null) {
				mainWidget.setAlertWarning("text.ffmpeg.null.title", "text.ffmpeg.null.text");
			} else {
				processYouTubeLiveForm(null);
			}
		});
		return menu31;
	}

	private MenuItem youTubeVideo() {
		MenuItem menu32 = new MenuItem();
		LanguageBinding.bindTextProperty(menu32.textProperty(), "menu32");

		menu32.setId("menu32");

		menu32.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
		menu32.setOnAction(e -> {
			if (pc.getConfiguration().getYoutubeDl() == null) {
				mainWidget.setAlertWarning("text.youtubedl.null.title", "text.youtubedl.null.text");
			} else if (pc.getConfiguration().getFfmpeg() == null) {
				mainWidget.setAlertWarning("text.ffmpeg.null.title", "text.ffmpeg.null.text");
			} else {
				processYouTubeVideoForm(null);
			}
		});
		return menu32;
	}

	private MenuItem tubiTvVideo() {
		MenuItem menu33 = new MenuItem();
		LanguageBinding.bindTextProperty(menu33.textProperty(), "menu33");

		menu33.setId("menu33");

		menu33.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN));
		menu33.setOnAction(e -> {
			if (pc.getConfiguration().getYoutubeDl() == null) {
				mainWidget.setAlertWarning("text.youtubedl.null.title", "text.youtubedl.null.text");
			} else if (pc.getConfiguration().getFfmpeg() == null) {
				mainWidget.setAlertWarning("text.ffmpeg.null.title", "text.ffmpeg.null.text");
			} else {
				processTubiTvVideoForm(null);
			}
		});
		return menu33;
	}

	/**
	 * Process the YouTube Video form, but creating the correct fields within the
	 * form for the user to run the command.
	 * 
	 * @param url - If null, then default to whatever was in the Store for this key,
	 *            otherwise use the URL provided.
	 */
	public void processYouTubeVideoForm(String url) {
		String storeName = "youtube-video";

		Store ytStore = pc.getStoreByKey(storeName);
		String storeUrl = ytStore == null || ytStore.getUrl() == null ? null : ytStore.getUrl();
		String newUrl = url == null ? storeUrl : url;

		Map<String, String> result = new CreateForm() //
				.setDialogTitle("text.download.ytv.dialog.header")
				.setButtonTitle("text.download.ytv.button.ok", "text.download.ytv.button.cancel") //
				.createTextMessage("text.download.yt.message")
				.createTextField("text.download.ytv.url", "text.download.ytv.url.example", "url", newUrl)
				.createTextFieldDirChooser("text.download.dir", "text.download.output.dir", "output-dir",
						(ytStore == null ? null : ytStore.getOutputDirectory()))
				.createTextMessage("text.convert.output.message")
				.createTextField("text.download.file", "text.download.output.file", "output-file",
						(ytStore == null ? null : ytStore.getOutputFileName()))
				.createComboBox("text.download.ffmpeg.logLevel", "verbosity", youtubeDlVerbosityLevels, 1)
				.validateFieldsListener().process();

		if (result != null) {
			if (ytStore == null) {
				ytStore = new Store();
				pc.addStoreByKey(storeName, ytStore);
			}
			ytStore.setAppUniqName(storeName);
			ytStore.setOutputDirectory(result.get("output-dir"));
			ytStore.setOutputFileName(result.get("output-file"));
			ytStore.setUrl(result.get("url"));
			pc.writeConfiguration();

			YouTube ytl = new YouTube(pc, result.get("url"), result.get("verbosity"), result.get("output-dir"),
					result.get("output-file"), result.get("timeout"), Type.Video);
			try {
				RunCommand runCommand = ytl.process();
				mainWidget.createTextAreaForRunCommand(runCommand);
			} catch (Exception e) {
				mainWidget.setAlertError("text.download.error", "text.download.content", e.getMessage());
			}
		}
	}

	public void processYouTubeLiveForm(String url) {
		String storeName = "youtube-live";

		Store ytStore = pc.getStoreByKey(storeName);
		String storeUrl = ytStore == null || ytStore.getUrl() == null ? null : ytStore.getUrl();
		String newUrl = url == null ? storeUrl : url;

		Map<String, String> result = new CreateForm() //
				.setDialogTitle("text.download.ytl.dialog.title")
				.setButtonTitle("text.download.ytv.button.ok", "text.download.ytv.button.cancel") //
				.createTextMessage("text.download.yt.message")
				.createTextField("text.download.ytl.url", "text.download.ytl.url.example", "url", newUrl)
				.createTextFieldDirChooser("text.download.dir", "text.download.output.dir", "output-dir",
						(ytStore == null ? null : ytStore.getOutputDirectory()))
				.createTextField("text.download.file", "text.download.output.file", "output-file",
						(ytStore == null ? null : ytStore.getOutputFileName()))
				.createComboBox("text.download.ffmpeg.logLevel", "verbosity", ffmpegVerbosityLevels, 2)
				.createTextMessage("text.download.yt.timeout.message")
				.createTextField("text.download.timeout", "text.download.ytl.timeout.example", "timeout",
						(ytStore == null ? null : ytStore.getMaxRecordTime()))
				.validateFieldsListener().process();

		if (result != null) {
			if (ytStore == null) {
				ytStore = new Store();
				pc.addStoreByKey(storeName, ytStore);
			}
			ytStore.setAppUniqName(storeName);
			ytStore.setMaxRecordTime(result.get("timeout"));
			ytStore.setOutputDirectory(result.get("output-dir"));
			ytStore.setOutputFileName(result.get("output-file"));
			ytStore.setUrl(result.get("url"));
			pc.writeConfiguration();

			YouTube ytl = new YouTube(pc, result.get("url"), result.get("verbosity"), result.get("output-dir"),
					result.get("output-file"), result.get("timeout"), Type.Live);
			try {
				RunCommand runCommand = ytl.process();
				mainWidget.createTextAreaForRunCommand(runCommand);
			} catch (Exception e) {
				mainWidget.setAlertError("text.download.error", "text.download.content", e.getMessage());
			}
		}
	}

	public void processTubiTvVideoForm(String url) {
		String storeName = "tubitv-video";

		Store ytStore = pc.getStoreByKey(storeName);
		String storeUrl = ytStore == null || ytStore.getUrl() == null ? null : ytStore.getUrl();
		String newUrl = url == null ? storeUrl : url;

		Map<String, String> result = new CreateForm() //
				.setDialogTitle("text.download.tubi.dialog.header")
				.setButtonTitle("text.download.tubi.button.ok", "text.download.tubi.button.cancel") //
				.createTextMessage("text.download.tubi.message") //
				.createTextField("text.download.tubi.url", "text.download.tubi.url.example", "url", newUrl)
				.createTextFieldDirChooser("text.download.dir", "text.download.output.dir", "output-dir",
						(ytStore == null ? null : ytStore.getOutputDirectory()))
				.createTextMessage("text.download.file.empty.message")
				.createTextField("text.download.file", "text.download.output.file", "output-file",
						(ytStore == null ? null : ytStore.getOutputFileName()))
				.createComboBox("text.download.ffmpeg.logLevel", "verbosity", youtubeDlVerbosityLevels, 1)
				.validateFieldsListener(new String[] { "url", "output-dir" }).process();
		if (result != null) {
			if (ytStore == null) {
				ytStore = new Store();
				pc.addStoreByKey(storeName, ytStore);
			}
			ytStore.setAppUniqName(storeName);
			ytStore.setOutputDirectory(result.get("output-dir"));
			ytStore.setUrl(result.get("url"));
			if (result.get("output-file") != null || result.get("output-file").trim().length() > 0) {
				ytStore.setOutputFileName(result.get("output-file"));
			}
			pc.writeConfiguration();

			TubiTv tubi = new TubiTv(pc, result.get("url"), result.get("verbosity"), result.get("output-dir"),
					result.get("output-file"));
			try {
				RunCommand runCommand = tubi.process();
				mainWidget.createTextAreaForRunCommand(runCommand);
			} catch (Exception e) {
				mainWidget.setAlertError("text.download.error", "text.download.content", e.getMessage());
			}

		}
	}
}
