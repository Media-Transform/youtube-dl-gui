/**
 * 
 */
package com.sunilsamuel.media.model;

import java.io.Serializable;

/**
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */

public class Store implements Serializable {
	private static final long serialVersionUID = -1911160958953215019L;
	private String appUniqName;
	private String url;
	private String outputDirectory;
	private String outputFileName;
	private String maxRecordTime;
	private boolean downloadSubtitle;

	/**
	 * @return the appUniqName
	 */
	public String getAppUniqName() {
		return appUniqName;
	}

	/**
	 * @param appUniqName the appUniqName to set
	 */
	public void setAppUniqName(String appUniqName) {
		this.appUniqName = appUniqName;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the outputDirectory
	 */
	public String getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * @param outputDirectory the outputDirectory to set
	 */
	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	/**
	 * @return the outputFileName
	 */
	public String getOutputFileName() {
		return outputFileName;
	}

	/**
	 * @param outputFileName the outputFileName to set
	 */
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	/**
	 * @return the maxRecordTime
	 */
	public String getMaxRecordTime() {
		return maxRecordTime;
	}

	/**
	 * @param maxRecordTime the maxRecordTime to set
	 */
	public void setMaxRecordTime(String maxRecordTime) {
		this.maxRecordTime = maxRecordTime;
	}

	/**
	 * @return the downloadSubtitle
	 */
	public boolean isDownloadSubtitle() {
		return downloadSubtitle;
	}

	/**
	 * @param downloadSubtitle the downloadSubtitle to set
	 */
	public void setDownloadSubtitle(boolean downloadSubtitle) {
		this.downloadSubtitle = downloadSubtitle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appUniqName == null) ? 0 : appUniqName.hashCode());
		result = prime * result + (downloadSubtitle ? 1231 : 1237);
		result = prime * result + ((maxRecordTime == null) ? 0 : maxRecordTime.hashCode());
		result = prime * result + ((outputDirectory == null) ? 0 : outputDirectory.hashCode());
		result = prime * result + ((outputFileName == null) ? 0 : outputFileName.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Store other = (Store) obj;
		if (appUniqName == null) {
			if (other.appUniqName != null)
				return false;
		} else if (!appUniqName.equals(other.appUniqName))
			return false;
		if (downloadSubtitle != other.downloadSubtitle)
			return false;
		if (maxRecordTime == null) {
			if (other.maxRecordTime != null)
				return false;
		} else if (!maxRecordTime.equals(other.maxRecordTime))
			return false;
		if (outputDirectory == null) {
			if (other.outputDirectory != null)
				return false;
		} else if (!outputDirectory.equals(other.outputDirectory))
			return false;
		if (outputFileName == null) {
			if (other.outputFileName != null)
				return false;
		} else if (!outputFileName.equals(other.outputFileName))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Store [appUniqName=").append(appUniqName).append(", url=").append(url)
				.append(", outputDirectory=").append(outputDirectory).append(", outputFileName=").append(outputFileName)
				.append(", maxRecordTime=").append(maxRecordTime).append(", downloadSubtitle=").append(downloadSubtitle)
				.append("]");
		return builder.toString();
	}
}
