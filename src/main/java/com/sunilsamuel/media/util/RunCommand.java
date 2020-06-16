/**
 * 
 */
package com.sunilsamuel.media.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class RunCommand extends Thread {
	private final List<?> commands;
	private List<String> command;
	private ProcessBuilder processBuilder;
	private TextArea textArea;
	private Button okButton;
	private Process process;
	private Button stopButton;

	private int totalLines = 0;

	public RunCommand(List<?> commands) {
		this.commands = commands;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public void run() {
		/**
		 * Check if we have a list of commands. That is, multiple commands to run in one
		 * single instance.
		 */
		if (!commands.isEmpty() && commands.get(0) instanceof List) {
			for (Object obj : commands) {
				command = (List<String>) obj;
				processBuilder = new ProcessBuilder(command);
				processBuilder.redirectErrorStream(true);
				processBuilder.redirectInput();
				runEachCommand();
			}
		} else {
			command = (List<String>) commands;
			processBuilder = new ProcessBuilder(command);
			processBuilder.redirectErrorStream(true);
			processBuilder.redirectInput();
			runEachCommand();
		}
	}

	public void setTextArea(TextArea textArea) {
		this.textArea = textArea;
	}

	public void setOkButton(Button okButton) {
		this.okButton = okButton;
	}

	/**
	 * @param stopButton
	 */
	public void setStopButton(Button stopButton) {
		this.stopButton = stopButton;
	}

	public void stopProcess() {
		try {
			process.destroyForcibly().waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void runEachCommand() {
		textArea.appendText("\n\nRunning command [" + command + "]\n");
		totalLines++;
		okButton.setDisable(true);
		stopButton.setDisable(false);
		try {
			process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line != null) {
					String nline = line + "\n";
					Platform.runLater(() -> textArea.appendText(nline));
					totalLines++;
				}
			}
			int exitCode = process.waitFor();
			if (exitCode == 0) {
				int ntotalLines = totalLines;
				Platform.runLater(() -> {
					textArea.appendText(">>>>>>>>>>>>Command ran successfully with exit code [" + exitCode
							+ "] and total number of lines written [" + ntotalLines + "]");
					okButton.setDisable(false);
					okButton.requestFocus();
					stopButton.setDisable(true);
				});
			} else {
				int ntotalLines = totalLines;
				Platform.runLater(() -> {
					textArea.appendText(">>>>>>>>>>>>Command did not run successfully.  Received exit code [" + exitCode
							+ "] and total number of lines written [" + ntotalLines + "]");
					okButton.setDisable(false);
					okButton.requestFocus();
					stopButton.setDisable(true);
				});
			}
		} catch (IOException | InterruptedException e) {
			// e.printStackTrace();

		}
	}

	public List<String> getCommand() {
		return command;
	}

}
