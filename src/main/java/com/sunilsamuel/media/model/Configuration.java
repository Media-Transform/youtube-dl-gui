
/**
 * Configuration.java (May 18, 2020 - 09:43:21 AM
 * media-downloader : com.sunilsamuel.media.model
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

package com.sunilsamuel.media.model;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class Configuration implements Serializable {
	private static final long serialVersionUID = 384915208971746379L;

	/**
	 * This will store the defaults for all of the applications keyed by a uniq
	 * name.
	 */
	private Map<String, Store> appDefaults = new HashMap<String, Store>();

	/**
	 * Location of applications.
	 */
	private File ffmpeg = null;
	private File youtubeDl = null;
	private File python = null;

	/**
	 * @return the appDefaults
	 */
	public Map<String, Store> getAppDefaults() {
		return appDefaults;
	}

	/**
	 * @param appDefaults the appDefaults to set
	 */
	public void setAppDefaults(Map<String, Store> appDefaults) {
		this.appDefaults.clear();
		this.appDefaults.putAll(appDefaults);
	}

	/**
	 * @return the ffmpeg
	 */
	public File getFfmpeg() {
		return ffmpeg;
	}

	/**
	 * @param ffmpeg the ffmpeg to set
	 */
	public void setFfmpeg(File ffmpeg) {
		this.ffmpeg = ffmpeg;
	}

	/**
	 * @return the youtubeDl
	 */
	public File getYoutubeDl() {
		return youtubeDl;
	}

	/**
	 * @param youtubeDl the youtubeDl to set
	 */
	public void setYoutubeDl(File youtubeDl) {
		this.youtubeDl = youtubeDl;
	}

	/**
	 * @return the python
	 */
	public File getPython() {
		return python;
	}

	/**
	 * @param python the python to set
	 */
	public void setPython(File python) {
		this.python = python;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appDefaults == null) ? 0 : appDefaults.hashCode());
		result = prime * result + ((ffmpeg == null) ? 0 : ffmpeg.hashCode());
		result = prime * result + ((python == null) ? 0 : python.hashCode());
		result = prime * result + ((youtubeDl == null) ? 0 : youtubeDl.hashCode());
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
		Configuration other = (Configuration) obj;
		if (appDefaults == null) {
			if (other.appDefaults != null)
				return false;
		} else if (!appDefaults.equals(other.appDefaults))
			return false;
		if (ffmpeg == null) {
			if (other.ffmpeg != null)
				return false;
		} else if (!ffmpeg.equals(other.ffmpeg))
			return false;
		if (python == null) {
			if (other.python != null)
				return false;
		} else if (!python.equals(other.python))
			return false;
		if (youtubeDl == null) {
			if (other.youtubeDl != null)
				return false;
		} else if (!youtubeDl.equals(other.youtubeDl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Configuration [appDefaults=").append(appDefaults).append(", ffmpeg=").append(ffmpeg)
				.append(", youtubeDl=").append(youtubeDl).append(", python=").append(python).append("]");
		return builder.toString();
	}
}
