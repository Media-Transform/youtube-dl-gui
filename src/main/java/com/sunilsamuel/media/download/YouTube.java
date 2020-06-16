
/**
 * YouTubeLive.java (May 17, 2020 - 11:47:42 PM
 * media-downloader : com.sunilsamuel.download
 * Git User Name : Sunil Samuel
 * Git User Email : sunil@redhat.com
 * 
 * Sunil Samuel CONFIDENTIAL
 * 
 *  [2020] Sunil Samuel 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Sunil Samuel. The intellectual and technical
 * concepts contained herein are proprietary to Sunil Samuel
 * and may be covered by U.S. and Foreign Patents, patents in 
 * process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written permission
 * is obtained from Sunil Samuel.
 */

package com.sunilsamuel.media.download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sunilsamuel.media.util.ProcessConfiguration;
import com.sunilsamuel.media.util.RunCommand;

/**
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class YouTube extends DownloadBase {
	private final ProcessConfiguration pc;
	private final String url;
	private final String logLevel;
	private final String timeout;
	private final Type type;
	private final String outputDir;
	private final String outputFile;
	private final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";

	private String webPage;
	private String m3u8;
	private String error;

	public enum Type {
		Live, Video
	};

	public YouTube(ProcessConfiguration pc, String url, String logLevel, String outputDir, String outputFile,
			String timeout, Type type) {
		this.pc = pc;
		this.url = url;
		this.logLevel = logLevel;
		this.type = type;
		this.outputDir = outputDir;
		this.outputFile = outputFile;
		this.timeout = timeout;
	}

	public RunCommand process() throws Exception {
		if (type == Type.Live) {
			return processLive();
		}

		return processVideo();
	}

	/**
	 * Process a 'non-live' video and download it using youtube-dl.
	 * 
	 * @return
	 */
	private RunCommand processVideo() {
		List<String> cmd = new ArrayList<String>();
		String outputFileName = createOutputFile(outputDir, outputFile);

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
		 * If the user is looking for an mp4 file, then make sure to set the extension
		 * to mp4 and get the best video and best audio.
		 */
		if (outputFileName.toLowerCase().endsWith("mp4")) {
			cmd.add("-f");
			cmd.add("bestvideo[ext=mp4]+bestaudio[ext=m4a]");
		} else if (outputFileName.toLowerCase().endsWith("mp3")) {
			/**
			 * If the extension is mp3, then add additional parameters to the application to
			 * extract the audio to mp3.
			 */
			cmd.add("--extract-audio");
			cmd.add("--audio-format");
			cmd.add("mp3");
		}

		/**
		 * Provide the location of the ffmpeg since youtube-dl uses it to convert.
		 */
		cmd.add("--ffmpeg-location");
		cmd.add(pc.getConfiguration().getFfmpeg().getParent());
		/**
		 * Define the output file by using the extension of the file as the file type to
		 * save it as.
		 */
		cmd.add("--output");
		// "xxx.%(ext)s" otherwise the output will be corrupted.
		cmd.add(outputFileName + "%(ext)s");
		/**
		 * Finally, add the url of the youtube video.
		 */
		cmd.add(url);
		return new RunCommand(cmd);
	}

	public RunCommand processLive() throws IOException {
		try {
			fetchUrl();
			findM3u8();
			return createCommand();
		} catch (MalformedURLException e) {
			error = "URL [" + url + "] is malformed. " + e.getMessage();
			throw e;
		} catch (UnknownHostException e) {
			error = "URL [" + url + "] has unknown host. " + e.getMessage();
			throw e;
		} catch (IOException e) {
			error = "URL [" + url + "] was not processed.  Could not get content. " + e.getMessage();
			throw e;
		}
	}

	/**
	 * --------------------------- Running ffmpeg Application
	 * --------------------------- Running command [
	 * C:\Users\web_w\Personal_Folder\Applications\ffmpeg-20200403-52523b6-win64-static\bin\ffmpeg.exe
	 * -loglevel info -y -i
	 * "https://manifest.googlevideo.com/api/manifest/hls_variant/expire/1589915028/ei/NNnDXrmNENWxhwaiq7XoAw/ip/96.241.192.82/id/JRtgYXeC86Y.0/source/yt_live_broadcast/requiressl/yes/hfr/1/playlist_duration/30/manifest_duration/30/maudio/1/vprv/1/go/1/keepalive/yes/dover/11/itag/0/playlist_type/DVR/sparams/expire%2Cei%2Cip%2Cid%2Csource%2Crequiressl%2Chfr%2Cplaylist_duration%2Cmanifest_duration%2Cmaudio%2Cvprv%2Cgo%2Citag%2Cplaylist_type/sig/AOq0QJ8wRQIgFb50dnbD_sILgCa3J9HWccE7RzKSyj1W9KJ5kjxK8CoCIQDf4O40ba1MAYsD9jL_C22zTYbi-094vqjAbcTE9rWmsQ%3D%3D/file/index.m3u8"
	 * -c copy -bsf:a aac_adtstoasc -t 08:00:00
	 * C:\Users\web_w\Documents\Tuesday.mp4]
	 * 
	 * 
	 * 
	 * Output File: C:\Users\web_w\Documents\Tuesday.mp4
	 * 
	 * 
	 * 
	 * NOTE: Hit CTRL+C on top of the download window to stop the application.
	 * --------------------------- OK ---------------------------
	 * 
	 */
	private RunCommand createCommand() {
		List<String> cmd = new ArrayList<String>();

		cmd.add(pc.getConfiguration().getFfmpeg().getAbsolutePath());
		cmd.add("-loglevel");
		cmd.add(logLevel);
		cmd.add("-y");
		cmd.add("-i");
		cmd.add(m3u8);
		cmd.add("-c");
		cmd.add("copy");
		cmd.add("-bsf:a");
		cmd.add("aac_adtstoasc");
		if (timeout != null && !timeout.trim().isEmpty()) {
			cmd.add("-t");
			cmd.add(timeout);
		}
		cmd.add(createOutputFile(outputDir, outputFile));

		return new RunCommand(cmd);
	}

	public String getM3u8Url() {
		return m3u8;
	}

	public String getError() {
		return error;
	}

	/**
	 * @throws IOException
	 * 
	 */
	private void findM3u8() throws IOException {
		Pattern patttern = Pattern.compile("hlsManifestUrl\\S*?(https\\S*?\\.m3u8)");
		Matcher matcher = patttern.matcher(webPage);
		if (matcher.find()) {
			if (matcher.groupCount() != 1) {
				throw new IOException(
						"Could not find the .m3u8 URL from the YouTube URL, no match in regular expression");
			}
			m3u8 = matcher.group(1);
			m3u8 = m3u8.replace("\\", "");
		} else {
			throw new IOException("Could not find the .m3u8 URL from the YouTube URL [" + url + "]");
		}
	}

	public String getWebPage() {
		return webPage;
	}

	private void fetchUrl() throws IOException {
		URL video;
		validateUrl();
		video = new URL(url);
		URLConnection uc = video.openConnection();
		uc.addRequestProperty("User-Agent", userAgent);
		BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String inputLine;
		StringBuilder sb = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
		}
		in.close();
		webPage = sb.toString();
	}

	private void validateUrl() throws MalformedURLException {
		Pattern pattern = Pattern.compile("watch\\?v=");
		Matcher matcher = pattern.matcher(url);

		if (!matcher.find()) {
			throw new MalformedURLException("URL does not contain 'watch?v=' string");
		}

		pattern = Pattern.compile("/channel/");
		matcher = pattern.matcher(url);
		if (matcher.find()) {
			throw new MalformedURLException("URL cannot be a channel, it must not contain '/channel/' string");
		}
	}

}
