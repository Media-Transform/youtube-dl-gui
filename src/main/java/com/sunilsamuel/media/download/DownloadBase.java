/**
 * 
 */
package com.sunilsamuel.media.download;

import java.util.Optional;

/**
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public abstract class DownloadBase {
	protected String createOutputFile(String outputDir, String outputFile) {
		String rval = "";
		if (outputDir != null && !outputDir.trim().isEmpty()) {
			rval = outputDir + "/";
		}
		if (outputFile != null && !outputFile.trim().isEmpty()) {
			rval = rval + outputFile;
		}
		return rval;
	}

	protected Optional<String> getExtensionByString(String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}

}
