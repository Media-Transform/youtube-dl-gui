/**
 * 
 */
package com.sunilsamuel.media.download;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.sunilsamuel.media.util.ProcessConfiguration;
import com.sunilsamuel.media.util.RunCommand;

/**
 * This class is used to merge, convert, ... one or more videos that are local
 * to the system.
 * 
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class ProcessMedia extends DownloadBase {
	private final ProcessConfiguration pc;
	private String destFileName;
	private final String logLevel;
	private boolean sameResolution;

	/**
	 * This is the File representation of the first source file.
	 */
	private File firstSourceFile;

	/**
	 * There could be multiple source files, so we want to separate them out and
	 * save them into a list.
	 */
	private List<String> sourceFileNames;
	private List<String> tmpFile = new ArrayList<String>();

	/**
	 * Constructor for the ProcessMedia
	 * 
	 * @param pc       ProcessConfiguration
	 * @param logLevel log level
	 */
	public ProcessMedia(ProcessConfiguration pc, String logLevel) {
		this.pc = pc;
		this.logLevel = logLevel;
	}

	/**
	 * Given several video files, merge them into one file.
	 * 
	 * @param sourceFileName A comma separated list of files
	 * @param destFileName   The destination file
	 * @param sameRes        Boolean string value if all of the files are same
	 *                       resolution and format
	 * @return
	 */
	public RunCommand merge(String sourceFileName, String destFileName, String sameRes) {
		this.sourceFileNames = Arrays.asList(sourceFileName.replaceAll("[\\[\\]]", "").split(","));
		this.firstSourceFile = new File(sourceFileNames.get(0));
		this.destFileName = destFileName;
		sameResolution = sameRes != null && sameRes.trim().toLowerCase().matches("^yes|^true|^1") ? true : false;
		return processMerge();
	}

	/**
	 * Convert a source file with a given extension to a destination file with
	 * another extension format.
	 * 
	 * @param sourceFileName
	 * @param destFileName
	 * @return
	 */
	public RunCommand convert(String sourceFileName, String destFileName) {
		this.sourceFileNames = Arrays.asList(sourceFileName.replaceAll("[\\[\\]]", "").split(","));
		this.firstSourceFile = new File(sourceFileNames.get(0));
		this.destFileName = destFileName;
		sameResolution = false;

		return processConvert();
	}

	/**
	 * ffmpeg -i video.mp4 -i audio.wav -c:v copy -c:a aac output.mp4<br>
	 * ffmpeg -i video.mp4 -i audio.wav -c:v copy -c:a aac -map 0:v:0 -map 1:a:0
	 * output.mp4<br>
	 * ffmpeg -i video.mp4 -i audio.wav -c:v copy -c:a aac -map 0:v:0 -map 1:a:0
	 * output.mp4 <br>
	 * 
	 * @param video
	 * @param audio
	 * @return
	 */
	public RunCommand mergeAV(String video, String audio) {
		File file = new File(video);

		Optional<String> ext = getExtensionByString(video);
		String fileExtension = ext.isPresent() ? ext.get() : "mp4";

		String outputFileName = file.getParent() + "/" + file.getName() + "-merged." + fileExtension;

		return new RunCommand(createCommandFromStrings(pc.getConfiguration().getFfmpeg().getAbsolutePath(), "-loglevel",
				logLevel, "-y", "-i", video, "-i", audio, "-c:v", "copy", "-c:a", "aac", "-map", "0:v:0?", "-map",
				"1:a:0?", "-shortest", outputFileName));

	}

	/**
	 * main_h the video's height<br>
	 * main_w the video's width<br>
	 * overlay_h the overlay's height<br>
	 * overlay_w the overlay's width<br>
	 * 
	 * Center: <br>
	 * ffmpeg -i test.mp4 -i watermark.png \ -filter_complex
	 * "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2" test2.mp4 <br>
	 * Top-Left:<br>
	 * ffmpeg -i text.mp4 -i watermark.png -filter_complex "overlay=10:10"
	 * birds1.mp4
	 * 
	 * 
	 * List of positions<br>
	 * tl (top-left), tr (top-right), bl- (bottom-left), br (bottom-right), center
	 * 
	 * @param video
	 * @param image
	 * @return
	 */
	public RunCommand watermark(String video, String image, String pos) {
		String overlay;
		if (pos.equals("tl")) {
			overlay = "overlay=10:10";
		} else if (pos.equals("tr")) {
			overlay = "overlay=x=(main_w-overlay_w-10):y=10";
		} else if (pos.equals("bl")) {
			overlay = "overlay=x=10:y=(main_h-overlay_h-10)";
		} else if (pos.equals("br")) {
			overlay = "overlay=x=(main_w-overlay_w-10):y=(main_h-overlay_h-10)";
		} else {
			/**
			 * Default to center
			 */
			overlay = "overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2";
		}
		List<String> cmd = new ArrayList<String>();

		cmd.add(pc.getConfiguration().getFfmpeg().getAbsolutePath());
		cmd.add("-loglevel");
		cmd.add(logLevel);

		cmd.add("-y");

		cmd.add("-i");
		cmd.add(video);
		cmd.add("-i");
		cmd.add(image);

		cmd.add("-c:v");
		cmd.add("libx264");
		cmd.add("-c:a");
		cmd.add("libmp3lame");

		cmd.add("-filter_complex");
		cmd.add(overlay);
		Optional<String> ext = getExtensionByString(video);
		String fileExt = ext.isPresent() ? ext.get() : "mp4";
		cmd.add(video + "-watermark." + fileExt);
		return new RunCommand(cmd);
	}

	/**
	 * Split is done by running two commands, one that create one video from 0 to
	 * splitPoint and another one from splitPoint to end.<br>
	 * ffmpeg -i edv_g24.mp4 -ss 0 -to 10 -c copy part1.mp4<br>
	 * ffmpeg -i edv_g24.mp4 -ss 10 -c copy part2.mp4
	 * 
	 * @param video
	 * @param pos
	 * @return
	 */
	public RunCommand split(String video, String pos) {
		List<List<String>> commands = new ArrayList<List<String>>();

		commands.add(createCommandFromStrings(pc.getConfiguration().getFfmpeg().getAbsolutePath(), "-loglevel",
				logLevel, "-y", "-i", video, "-ss", "0", "-to", pos, "-c:v", "libx264", "-c:a", "libmp3lame",
				video + "-split01." + getExtensionByString(video).get()));

		commands.add(createCommandFromStrings(pc.getConfiguration().getFfmpeg().getAbsolutePath(), "-loglevel",
				logLevel, "-y", "-i", video, "-ss", pos, "-c:v", "libx264", "-c:a", "libmp3lame",
				video + "-split02." + getExtensionByString(video).get()));

		return new RunCommand(commands);
	}

	/**
	 * Convert one media to a different format. For instance, <br>
	 * ffmpeg -i input.mp4 -c:v libx264 -c:a libmp3lame -b:a 384K output.avi
	 * 
	 * @return
	 */
	//
	private RunCommand processConvert() {
		List<String> cmd = new ArrayList<String>();

		createCommandFromStrings(pc.getConfiguration().getFfmpeg().getAbsolutePath(), "-loglevel", logLevel, "-y", "-i",
				sourceFileNames.get(0), "-c:v", "libx264", "-c:a", "libmp3lame",
				firstSourceFile.getParent() + "/" + destFileName);

		return new RunCommand(cmd);
	}

	/**
	 * Merge many files into one single file. If the files are of different types or
	 * different resolution, then we need to convert all of them to the same format.
	 * Once that is done, we want to take each of these files are concat them.
	 * 
	 * @return
	 */
	private RunCommand processMerge() {
		List<List<String>> commands = new ArrayList<List<String>>();

		for (String file : sourceFileNames) {
			String thisTmpFile = createTmpFile(file, ".mov");
			tmpFile.add(thisTmpFile);
			/**
			 * Only do convert command if we have different format or resolution
			 */
			if (!sameResolution) {
				commands.add(createCommandFromStrings(pc.getConfiguration().getFfmpeg().getAbsolutePath(), "-y", "-i",
						file, "-c:a", "pcm_s16le", thisTmpFile));
			}
		}
		File inputFileName = createInputFile();
		commands.add(createCommandFromStrings(pc.getConfiguration().getFfmpeg().getAbsolutePath(), "-y", "-f", "concat",
				"-safe", "0", "-i", inputFileName.getAbsolutePath(), "-c:v", "copy",
				firstSourceFile.getParent() + "/" + destFileName));

		return new RunCommand(commands);
	}

	private File createInputFile() {
		File txtFile = new File(createTmpFile("filelist", ".txt"));
		FileWriter fw;
		try {
			fw = new FileWriter(txtFile.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);

			for (int i = tmpFile.size() - 1; i >= 0; i--) {
				bw.write("file " + tmpFile.get(i).replace("\\", "\\\\") + "\n");
			}
			bw.close();
			return txtFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private File createTmpDir() {
		String tmpDir = Paths.get(System.getProperty("java.io.tmpdir"), "youtube-downloader").toString();
		File dir = new File(tmpDir);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		return dir;
	}

	private List<String> createConcatRunCommand(File inputFileName) {
		List<String> rval = new ArrayList<String>();
		rval.add(pc.getConfiguration().getFfmpeg().getAbsolutePath());
		rval.add("-y");
		rval.add("-f");
		rval.add("concat");
		rval.add("-safe");
		rval.add("0");
		rval.add("-i");
		rval.add(inputFileName.getAbsolutePath());

		rval.add("-c:v");
		rval.add("copy");
		rval.add(firstSourceFile.getParent() + "/" + destFileName);

		return rval;
	}

	/**
	 * Create a string that points to a temp file within the tmpdir.
	 * 
	 * @param from
	 * @param ext
	 * @return
	 */
	private String createTmpFile(String from, String ext) {
		String tmpdir = System.getProperty("java.io.tmpdir");
		File f = new File(from);
		return Paths.get(tmpdir, "youtube-downloader", f.getName()).toString() + ext;
	}

	private List<String> createCommandFromStrings(String... strings) {
		List<String> cmd = new ArrayList<String>();

		for (String s : strings) {
			cmd.add(s);
		}

		return cmd;
	}
}
