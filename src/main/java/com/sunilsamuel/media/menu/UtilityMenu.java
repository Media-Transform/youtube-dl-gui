/**
 * 
 */
package com.sunilsamuel.media.menu;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import com.sunilsamuel.media.capture.CreateForm;
import com.sunilsamuel.media.capture.MainWidget;
import com.sunilsamuel.media.download.ProcessMedia;
import com.sunilsamuel.media.model.Store;
import com.sunilsamuel.media.util.LanguageBinding;
import com.sunilsamuel.media.util.ProcessConfiguration;
import com.sunilsamuel.media.util.RunCommand;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class UtilityMenu extends MenuBase {

	/**
	 * @param mainWidget
	 * @param pc
	 */
	public UtilityMenu(MainWidget mainWidget, ProcessConfiguration pc) {
		super(mainWidget, pc);
		menu = new Menu();
		LanguageBinding.bindTextProperty(menu.textProperty(), "menuUtility");
		menu.setId("menuUtility");
	}

	@Override
	public Menu process() {
		menu.getItems().addAll(convertFile(), mergeVideos(), mergeAudioAndVideo(), watermark(), split());
		return menu;
	}

	public MenuItem convertFile() {
		MenuItem convert = new MenuItem();
		LanguageBinding.bindTextProperty(convert.textProperty(), "menuUtility1");
		convert.setOnAction(e -> {
			processConvertFile();
		});
		return convert;
	}

	public MenuItem mergeVideos() {
		MenuItem menuItem = new MenuItem();
		LanguageBinding.bindTextProperty(menuItem.textProperty(), "menuUtility2");
		menuItem.setOnAction(e -> {
			processMergeVideos();
		});
		return menuItem;
	}

	public MenuItem mergeAudioAndVideo() {
		MenuItem menuItem = new MenuItem();
		LanguageBinding.bindTextProperty(menuItem.textProperty(), "menuUtility3");
		menuItem.setOnAction(e -> {
			processMergeAudioAndVideo();
		});
		return menuItem;
	}

	public MenuItem watermark() {
		MenuItem menuItem = new MenuItem();
		LanguageBinding.bindTextProperty(menuItem.textProperty(), "menuUtility4");
		menuItem.setOnAction(e -> {
			processWatermark();
		});
		return menuItem;
	}
	
	public MenuItem split() {
		MenuItem menuItem = new MenuItem();
		LanguageBinding.bindTextProperty(menuItem.textProperty(), "menuUtility5");
		menuItem.setOnAction(e -> {
			processSplit();
		});
		return menuItem;
	}


	public void processConvertFile() {
		String storeName = "convert-video";
		Store ytStore = pc.getStoreByKey(storeName);

		Map<String, String> result = new CreateForm() //
				.setDialogTitle("text.convert.dialog.header")
				.setButtonTitle("text.convert.button.ok", "text.convert.button.cancel") //
				.createTextMessage("text.convert.message") //
				.createTextFieldFileChooser("text.convert.dir.chooser.title", "text.convert.dir.chooser.prompt",
						"convert-file", (ytStore == null ? null : ytStore.getOutputDirectory()))
				.createSeparator().//
				createTextMessage("text.convert.to.message")
				.createTextField("text.download.file", "text.download.output.file", "output-file",
						(ytStore == null ? null : ytStore.getOutputFileName()))
				.createComboBox("text.download.ffmpeg.logLevel", "verbosity", ffmpegVerbosityLevels, 1)
				.validateFieldsListener().process();
		if (result != null) {
			if (ytStore == null) {
				ytStore = new Store();
				pc.addStoreByKey(storeName, ytStore);
			}
			ytStore.setAppUniqName(storeName);
			ytStore.setOutputDirectory(result.get("convert-file"));
			ytStore.setUrl(result.get("url"));
			if (result.get("output-file") != null && result.get("output-file").trim().length() > 0) {
				ytStore.setOutputFileName(result.get("output-file"));
			}
			pc.writeConfiguration();

			RunCommand runCommand = new ProcessMedia(pc, result.get("verbosity")).convert(result.get("convert-file"),
					result.get("output-file"));
			try {
				mainWidget.createTextAreaForRunCommand(runCommand);
			} catch (Exception e) {
				mainWidget.setAlertError("text.download.error", "text.download.content", e.getMessage());
			}
		}
	}

	public void processMergeVideos() {
		String storeName = "merge-video";
		Store ytStore = pc.getStoreByKey(storeName);

		Map<String, String> result = new CreateForm() //
				.setDialogTitle("text.convert.dialog.header")
				.setButtonTitle("text.convert.button.ok", "text.convert.button.cancel") //
				.createTextMessage("text.convert.message") //
				.createTextFieldFileChooser("text.convert.dir.chooser.title", "text.convert.dir.chooser.prompt",
						"convert-file", (ytStore == null ? null : ytStore.getOutputDirectory()), true)
				.createSeparator()//
				.createCheckBox("text.merge.res", "same-resolution") //
				.createSeparator()//
				.createTextMessage("text.convert.to.message")
				.createTextField("text.download.file", "text.download.output.file", "output-file",
						(ytStore == null ? null : ytStore.getOutputFileName()))
				.createComboBox("text.download.ffmpeg.logLevel", "verbosity", ffmpegVerbosityLevels, 1)
				.validateFieldsListener().process();
		if (result != null) {
			if (ytStore == null) {
				ytStore = new Store();
				pc.addStoreByKey(storeName, ytStore);
			}
			ytStore.setAppUniqName(storeName);
			ytStore.setOutputDirectory(result.get("convert-file"));
			ytStore.setUrl(result.get("url"));
			if (result.get("output-file") != null && result.get("output-file").trim().length() > 0) {
				ytStore.setOutputFileName(result.get("output-file"));
			}
			pc.writeConfiguration();

			RunCommand runCommand = new ProcessMedia(pc, result.get("verbosity")).merge(result.get("convert-file"),
					result.get("output-file"), result.get("same-resolution"));

			try {
				mainWidget.createTextAreaForRunCommand(runCommand);
			} catch (Exception e) {
				mainWidget.setAlertError("text.download.error", "text.download.content", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void processMergeAudioAndVideo() {
		String storeName = "merge-audio-video";
		Store ytStore = pc.getStoreByKey(storeName);

		Map<String, String> result = new CreateForm() //
				.setDialogTitle("text.mav.dialog.header")
				.setButtonTitle("text.convert.button.ok", "text.convert.button.cancel") //
				.createTextMessage("text.mav.message") //
				.createTextFieldFileChooser("text.mav.video.chooser.title", "text.mav.video.chooser.title", "mav-video",
						(ytStore == null ? null : ytStore.getOutputDirectory()))
				.createSeparator() //
				.createTextFieldFileChooser("text.mav.audio.chooser.title", "text.mav.audio.chooser.title", "mav-audio",
						(ytStore == null ? null : ytStore.getOutputDirectory()))
				.createComboBox("text.download.ffmpeg.logLevel", "verbosity", ffmpegVerbosityLevels, 1)
				.validateFieldsListener().process();

		if (result != null) {
			if (ytStore == null) {
				ytStore = new Store();
				pc.addStoreByKey(storeName, ytStore);
			}
			ytStore.setAppUniqName(storeName);
			File odirPath = new File(result.get("mav-video"));

			ytStore.setOutputDirectory(odirPath.getParent());
			ytStore.setUrl(result.get("url"));
			pc.writeConfiguration();

			RunCommand runCommand = new ProcessMedia(pc, result.get("verbosity")).mergeAV(result.get("mav-video"),
					result.get("mav-audio"));
			System.out.println("Command is [" + runCommand.getCommand() + "]");
			try {
				mainWidget.createTextAreaForRunCommand(runCommand);
			} catch (Exception e) {
				mainWidget.setAlertError("text.download.error", "text.download.content", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void processWatermark() {
		String storeName = "watermark";
		Store ytStore = pc.getStoreByKey(storeName);

		Map<String, String> result = new CreateForm() //
				.setDialogTitle("text.wm.dialog.header")
				.setButtonTitle("text.convert.button.ok", "text.convert.button.cancel") //
				.createTextMessage("text.wm.message") //
				.createSeparator() //
				.createTextFieldFileChooser("text.wm.video.chooser.title", "text.wm.video.chooser.title", "wm-video",
						(ytStore == null ? null : ytStore.getOutputDirectory()))
				.createSeparator() //
				.createTextFieldFileChooser("text.wm.image.chooser.title", "text.wm.image.chooser.title", "wm-image",
						(ytStore == null ? null : ytStore.getOutputDirectory()))
				.createSeparator() //
				.createTextMessage("text.wm.pos")
				.createRadioButton(Arrays.asList(new String[] { "text.wm.rb.tl:tl", "text.wm.rb.tr:tr",
						"text.wm.rb.bl:bl", "text.wm.rb.br:br", "text.wm.rb.center:center" }), "wm-pos")
				.createSeparator()
				.createComboBox("text.download.ffmpeg.logLevel", "verbosity", ffmpegVerbosityLevels, 1)
				.createSeparator().validateFieldsListener().process();

		if (result != null) {
			if (ytStore == null) {
				ytStore = new Store();
				pc.addStoreByKey(storeName, ytStore);
			}
			ytStore.setAppUniqName(storeName);
			File odirPath = new File(result.get("wm-video"));

			ytStore.setOutputDirectory(odirPath.getParent());
			ytStore.setUrl(result.get("url"));
			pc.writeConfiguration();

			RunCommand runCommand = new ProcessMedia(pc, result.get("verbosity")).watermark(result.get("wm-video"),
					result.get("wm-image"), result.get("wm-pos"));
			try {
				mainWidget.createTextAreaForRunCommand(runCommand);
			} catch (Exception e) {
				mainWidget.setAlertError("text.download.error", "text.download.content", e.getMessage());
				e.printStackTrace();
			}
		}

	}
	
	private void processSplit() {
		String storeName = "split";
		Store ytStore = pc.getStoreByKey(storeName);

		Map<String, String> result = new CreateForm() //
				.setDialogTitle("text.split.dialog.header")
				.setButtonTitle("text.convert.button.ok", "text.convert.button.cancel") //
				.createTextMessage("text.split.message") //
				.createSeparator() //
				.createTextFieldFileChooser("text.split.video.chooser.title", "text.split.video.chooser.title", "split-video",
						(ytStore == null ? null : ytStore.getOutputDirectory()))
				.createSeparator() //
				.createTextMessage("text.split.video.pos.message")
				.createSeparator()
				.createTextField("text.split.position", "text.download.ytl.timeout.example", "split-pos",
						(ytStore == null ? null : ytStore.getMaxRecordTime()))
				.validateFieldsListener().process();

		if (result != null) {
			if (ytStore == null) {
				ytStore = new Store();
				pc.addStoreByKey(storeName, ytStore);
			}
			ytStore.setAppUniqName(storeName);
			File odirPath = new File(result.get("wm-video"));

			ytStore.setOutputDirectory(odirPath.getParent());
			ytStore.setUrl(result.get("url"));
			pc.writeConfiguration();

			RunCommand runCommand = new ProcessMedia(pc, result.get("verbosity")).watermark(result.get("wm-video"),
					result.get("wm-image"), result.get("wm-pos"));
			try {
				mainWidget.createTextAreaForRunCommand(runCommand);
			} catch (Exception e) {
				mainWidget.setAlertError("text.download.error", "text.download.content", e.getMessage());
				e.printStackTrace();
			}
		}

	}


}
