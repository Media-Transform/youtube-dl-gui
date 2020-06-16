
/**
 * ProcessConfiguration.java (May 18, 2020 - 10:08:31 AM
 * media-downloader : com.sunilsamuel.media.util
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

package com.sunilsamuel.media.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sunilsamuel.media.model.Configuration;
import com.sunilsamuel.media.model.Store;

/**
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class ProcessConfiguration {

	private static ProcessConfiguration singleton;
	private Configuration configuration;
	private final String configFileName;

	public static ProcessConfiguration getInstance() {
		if (singleton == null) {
			singleton = new ProcessConfiguration();
		}
		return singleton;

	}

	public ProcessConfiguration() {
		String home = getDefaultDirectory();
		this.configFileName = home + "/.media-downloader.cfg";
		this.configuration = new Configuration();
		loadConfiguration();
	}

	public static String getDefaultDirectory() {
		String home = System.getProperty("user.home");
		if (home == null || home.trim().isEmpty()) {
			home = System.getenv("HOME");
		}
		if (home == null || home.trim().isEmpty()) {
			home = System.getProperty("java.io.tmpdir");
		}
		if (home.endsWith("/")) {
			home = home.substring(0, home.length() - 1);
		}
		return home;
	}

	public Store getStoreByKey(String key) {
		if (configuration == null) {
			return null;
		}
		if (configuration.getAppDefaults() == null) {
			return null;
		}
		return configuration.getAppDefaults().get(key);
	}

	public void addStoreByKey(String key, Store value) {
		if (configuration != null) {
			configuration.getAppDefaults().put(key, value);
		}
	}

	public void loadConfiguration() {
		try {
			FileInputStream in = new FileInputStream(getAbsoluteFileName());

			ObjectInputStream ois = new ObjectInputStream(in);
			configuration = (Configuration) ois.readObject();
			in.close();

		} catch (IOException | NullPointerException | ClassNotFoundException e) {
			System.out.println("Caught exception " + e);
			configuration = new Configuration();
		}
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void writeConfiguration() {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(getAbsoluteFileName(), false);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(configuration);

		} catch (IOException e) {
			System.err.println("Could not write configuration file to directory");
			e.printStackTrace();
		} finally {
			try {
				oos.close();
				fos.close();
			} catch (IOException e) {
				System.err.println("Could not close configuration file");
				e.printStackTrace();
			}
		}
	}

	private String getAbsoluteFileName() {
		return configFileName;
	}
}
