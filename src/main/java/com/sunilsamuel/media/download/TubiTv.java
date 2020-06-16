/**
 * 
 */
package com.sunilsamuel.media.download;

import java.util.ArrayList;
import java.util.List;

import com.sunilsamuel.media.util.ProcessConfiguration;
import com.sunilsamuel.media.util.RunCommand;

/**
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class TubiTv extends DownloadBase {

	private final ProcessConfiguration pc;
	private final String url;
	private final String logLevel;
	private final String outputDir;
	private final String outputFile;

	public TubiTv(ProcessConfiguration pc, String url, String logLevel, String outputDir, String outputFile) {
		this.pc = pc;
		this.url = url;
		this.logLevel = logLevel;
		this.outputDir = outputDir;
		this.outputFile = outputFile;
	}

	// youtube-dl --no-call-home --verbose --all-subs --add-metadata
	// --ffmpeg-location . --output "%(title)s.%(ext)s"
	// "https://tubitv.com/tv-shows/464147/s03_e01_the_loaded_goat"
	public RunCommand process() throws Exception {
		List<String> cmd = new ArrayList<String>();

		cmd.add(pc.getConfiguration().getYoutubeDl().getAbsolutePath());
		cmd.add("--no-call-home");
		/**
		 * If loglevel is set to debug, then add verbose flag.
		 */
		if (logLevel != null && logLevel.toLowerCase().equals("debug")) {
			cmd.add("--verbose");
		}

		/**
		 * Download all of the subtitles if available.
		 */
		cmd.add("--all-subs");
		/**
		 * Add additional meta data, such as mp4 video title and other data into the
		 * file.
		 */
		cmd.add("--add-metadata");
		/**
		 * Provide the location of the ffmpeg since youtube-dl uses it to convert.
		 */
		cmd.add("--ffmpeg-location");
		cmd.add(pc.getConfiguration().getFfmpeg().getParent());

		cmd.add("--output");
		// --output "%(title)s.%(ext)s"
		String outputDirFile = createOutputFile(outputDir, outputFile);
		if (outputFile == null || outputFile.trim().isEmpty()) {
			outputDirFile = outputDirFile + "%(title)s.%(ext)s";
		}
		cmd.add(outputDirFile);
		cmd.add(url);

		return new RunCommand(cmd);
	}

}
