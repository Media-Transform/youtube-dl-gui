
/**
 * MainApplication.java (May 17, 2020 - 01:13:44 PM
 * YTDownloader : com.sunilsamuel.capture
 * Git User Name : 
 * Git User Email : 
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

package com.sunilsamuel.media.capture;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main application that starts the javafx system.
 * 
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class MainApplication extends Application {
	private Stage primaryStage;
	private Scene mainScene;
	private MainWidget mainWidget;
	private VBox mainBox;

	@Override
	public void start(Stage primaryStage) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("property/messages");

			locale = new SimpleObjectProperty<>(Locale.ENGLISH);
			locale.addListener((observable, oldValue, newValue) -> {
				System.out.println("Setting language to [" + newValue + "]");
				Locale.setDefault(newValue);
			});

			setStage(primaryStage, bundle);
			setScene();

			mainWidget = new MainWidget(mainBox);
			new CreateMenu(mainBox, mainWidget).process();

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setStage(Stage stage, ResourceBundle bundle) {
		primaryStage = stage;
		primaryStage.setTitle(bundle.getString("stage.title"));
		primaryStage.centerOnScreen();
		primaryStage.getIcons().add(new Image("/media/logo/media-download-icon.png"));
	}

	private void setScene() {
		mainBox = new VBox();
		mainBox.setId("main-box");
		mainScene = new Scene(mainBox, 800, 800);
		mainBox.prefHeightProperty().bind(mainScene.heightProperty());
		mainBox.prefWidthProperty().bind(mainScene.widthProperty());
		mainScene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		primaryStage.setScene(mainScene);
	}

	private ObjectProperty<Locale> locale;

	public static void main(String[] args) {
		launch(args);
	}

}
