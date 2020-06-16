
package com.sunilsamuel.media.capture;

import java.net.URL;
import java.util.Optional;

import com.sunilsamuel.media.menu.DownloadMenu;
import com.sunilsamuel.media.util.LanguageBinding;
import com.sunilsamuel.media.util.ProcessConfiguration;
import com.sunilsamuel.media.util.RunCommand;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Used to create GUI interfaces for text, dialog, alert, and other interfaces.
 * The following code is used:
 * <ul>
 * <li>setText (<titleKey>, textKey) - Set the content of the main stage.
 * titleKey is optional</li>
 * <li>setDialog (titleKey, contentKey, defaultValue) - A new dialog box is
 * opened</li>
 * </ul>
 * 
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class MainWidget {

	public enum RequiredFields {
		URL, DIRECTORY, FILENAME, MAXRECORDTIME, SUBTITLE
	};

	private VBox box;

//	private final ResourceBundle bundle;
	private final VBox mainBox;

	private final WebView webView;

	private final String userAgentYt = "Mozilla/5.0 (iPhone; CPU iPhone OS 8_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12D508";
	private final String userAgentTv = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";

	private final String id = "main-widget-box";

//	public MainWidget(ResourceBundle bundle, VBox mainBox) {
	public MainWidget(VBox mainBox) {
		this.mainBox = mainBox;
//		this.bundle = bundle;

		webView = new WebView();
		webView.getEngine().setUserAgent(userAgentTv);
	}

	/**
	 * Set the main scene box to a text.
	 * 
	 * @param textKey
	 */
	public void setText(String textKey) {
		clearWebView();
		findBox().getChildren().add(createText(textKey));
	}

	/**
	 * Set the main scene box to a label and a text.
	 * 
	 * @param titleKey
	 * @param textKey
	 */
	public void setText(String titleKey, String textKey) {
		clearWebView();
		findBox().getChildren().addAll(createLabel(titleKey), createText(textKey));
	}

	public String setDialog(String titleKey, String contentKey, String defaultValue) {
		TextInputDialog dialog = new TextInputDialog(defaultValue);
		LanguageBinding.bindTextProperty(dialog.titleProperty(), titleKey);
		dialog.setHeaderText("");
		LanguageBinding.bindTextProperty(dialog.contentTextProperty(), contentKey);
		dialog.setResizable(true);
		dialog.getDialogPane().setPrefWidth(600);
		dialog.getDialogPane().setCenterShape(true);

		Stage myStage = (Stage) dialog.getDialogPane().getScene().getWindow();
		MainWidget.addMainIcon(myStage);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}

	public void createTextAreaForRunCommand(RunCommand command) {
		Dialog<String> dialog = new Dialog<String>();
		dialog.setTitle(LanguageBinding.get("command.run"));
		LanguageBinding.bindTextProperty(dialog.titleProperty(), "command.run");
		dialog.setResizable(true);
		dialog.getDialogPane().setPrefWidth(600);
		dialog.getDialogPane().setCenterShape(true);

		Stage myStage = (Stage) dialog.getDialogPane().getScene().getWindow();
		MainWidget.addMainIcon(myStage);

		ButtonType stopButtonType = new ButtonType("Stop");
		dialog.getDialogPane().getButtonTypes().addAll(stopButtonType, ButtonType.CLOSE, ButtonType.CANCEL);
		dialog.initModality(Modality.WINDOW_MODAL);

		/**
		 * This is the 'Stop' button that will stop the external command from
		 * completing. This is a way for the user to stop downloading when they want to.
		 * Using addEventFilter so that the dialog will not close.
		 */
		final Button stopButton = (Button) dialog.getDialogPane().lookupButton(stopButtonType);

		final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
		okButton.setDisable(true);
		command.setOkButton(okButton);
		command.setStopButton(stopButton);

		TextArea ta = new TextArea();
		ta.prefWidthProperty().bind(dialog.widthProperty().subtract(80d));
		ta.setWrapText(false);
		ta.setEditable(false);
		ta.setManaged(true);

		dialog.getDialogPane().setContent(ta);

		stopButton.addEventFilter(ActionEvent.ACTION, event -> {
			command.stopProcess();
			ta.appendText("\n\n>>>>>>>>>>>>Stopping application since user clicked on 'Cancel'>>>>>>>>>>>>\n\n");
			event.consume();
			/**
			 * Once the button is clicked, just disable it so that they won't click on it
			 * again.
			 */
			stopButton.setDisable(true);
		});

		dialog.show();

		command.setTextArea(ta);
		command.start();
		System.out.println("DIALOG SHOW");
	}

	public ButtonType setAlertConfirm(String titleKey, String contentKey) {
		return setAlert(AlertType.CONFIRMATION, titleKey, contentKey);
	}

	public ButtonType setAlertWarning(String titleKey, String contentKey) {
		return setAlert(AlertType.WARNING, titleKey, contentKey);
	}

	public ButtonType setAlertError(String titleKey, String contentKey, final Object... args) {
		return setAlert(AlertType.ERROR, titleKey, contentKey, args);
	}

	public ButtonType setAlertInformation(String titleKey, String contentKey) {
		return setAlert(AlertType.INFORMATION, titleKey, contentKey);
	}

	public ButtonType setAlert(AlertType type, String titleKey, String contentKey, final Object... args) {
		Alert alert = new Alert(type);
		alert.setHeaderText("");
		LanguageBinding.bindTextProperty(alert.titleProperty(), titleKey);
		LanguageBinding.bindTextProperty(alert.contentTextProperty(), contentKey, args);
		Stage myStage = (Stage) alert.getDialogPane().getScene().getWindow();
		MainWidget.addMainIcon(myStage);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}
	
	public void setHtml(String htmlPage) {
		clearWebView();
		WebEngine webEngine = webView.getEngine();
		URL url = getClass().getClassLoader().getResource(htmlPage);
		webEngine.load(url.toExternalForm());

		VBox myBox = findBox();
		webView.prefHeightProperty().bind(myBox.heightProperty());
		webView.prefWidthProperty().bind(myBox.widthProperty());

		findBox().getChildren().add(webView);
	}

	/**
	 * This runs the html page using the webEngine and will call a javascript code
	 * inside this html page named 'init' with the value in var. This is a way to
	 * send data into the html page from the java application. You must make sure to
	 * have a javascript function named 'init' in the html file.
	 * 
	 * @param htmlPage
	 * @param var
	 */
	public void setHtml(String htmlPage, String var) {
		clearWebView();
		WebEngine webEngine = webView.getEngine();

		webEngine.setUserStyleSheetLocation(getClass().getClassLoader().getResource("css/html.css").toString());

		URL url = getClass().getClassLoader().getResource(htmlPage);
		ChangeListener<Worker.State> javaScriptLoadListener = (obs, oldValue, newValue) -> {
			if (newValue == State.SUCCEEDED) {
				webEngine.executeScript("init ('" + var + "')");
			}
		};

		VBox myBox = findBox();
		webView.prefHeightProperty().bind(myBox.heightProperty());
		webView.prefWidthProperty().bind(myBox.widthProperty());

		webEngine.getLoadWorker().stateProperty().addListener(javaScriptLoadListener);

		webEngine.load(url.toExternalForm());

		webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == State.RUNNING) {
				webEngine.getLoadWorker().stateProperty().removeListener(javaScriptLoadListener);
			}
		});

		myBox.getChildren().add(webView);
	}

	/**
	 * This is a shortcut to the code that displays the main page. This is what the
	 * user sees when they start the application.
	 */
	public void setRootPage() {
		clearWebView();
		setHtml("html/media-downloader-root-page.html", System.getProperty("os.name"));
	}

	/**
	 * Given a URL, open the website onto the webView area.
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		clearWebView();

		/**
		 * Create the URL text field.
		 */
		TextField urlField = new TextField();
		urlField.setId("url-field");
		urlField.setText(url);
		urlField.setPromptText(url);

		Button button = new Button();
		LanguageBinding.bindTextProperty(button.textProperty(), "text.download.button");
		button.setOnAction((e) -> {
			MainWidget.processRandomUrl(this, webView.getEngine().getLocation());
		});

		ProgressBar progressBar = new ProgressBar();
		// Bind the progress property of ProgressBar
		// with progress property of Worker
		progressBar.progressProperty().bind(webView.getEngine().getLoadWorker().progressProperty());

		/**
		 * Once we completed the loading, change the color of the progress bar so that
		 * people know that it is done.
		 */
		/**
		 * Webview has issues with different types of pages. Therefore, we have to send
		 * different user agents to the server to make it work.
		 */
		if (url.toLowerCase().contains("youtube")) {
			webView.getEngine().setUserAgent(userAgentYt);
		} else {
			webView.getEngine().setUserAgent(userAgentTv);
		}

		webView.getEngine().getLoadWorker().stateProperty().addListener((ob, oldValue, newValue) -> {
			System.out.println("Calling [" + webView.getEngine().getLocation() + "]");
			urlField.setText(webView.getEngine().getLocation());
			if (newValue == State.SCHEDULED) {
				System.out.println("In schedule");
			} else if (newValue == State.RUNNING) {
				System.out.println("Running now");
			} else if (newValue == State.SUCCEEDED) {
				progressBar.getStyleClass().add("progress-bar-completed");
				urlField.setText(webView.getEngine().getLocation());
				System.out.println("In succeeded [" + webView.getEngine().getUserAgent() + "]");
			}
		});

		webView.getEngine().load(url);
		webView.getEngine().setOnVisibilityChanged(e -> {
			webView.getEngine().load(null);
		});

		urlField.setOnAction(e -> {
			progressBar.getStyleClass().removeAll("progress-bar-completed");
			String newUrl;
			newUrl = urlField.getText();
			webView.getEngine().load(newUrl);
		});

		findBox().getChildren().addAll(urlField, webView, button, progressBar);
	}

	private Text createText(String strKey) {
		Text text = new Text();
		LanguageBinding.bindTextProperty(text.textProperty(), strKey);
		text.wrappingWidthProperty().bind(mainBox.widthProperty().subtract(80d));
		text.setId("main-widget-text");
		return text;
	}

	private Label createLabel(String strKey) {
		Label label = new Label();
		LanguageBinding.bindTextProperty(label.textProperty(), strKey);
		label.setId("main-widget-label");
		return label;
	}

	private void clearWebView() {
		webView.getEngine().load(null);
	}

	/**
	 * Iterate over all of the children in the pane and find the node with the same
	 * id as the hbox. Once found, delete everything from inside the hbox and return
	 * it so that new content can be added into. it.
	 * 
	 * @return
	 */
	private VBox findBox() {
		VBox thisBox = null;

		for (Node c : mainBox.getChildren()) {
			if (c.getId().contentEquals(id)) {
				thisBox = (VBox) c;
				break;
			}
		}
		/**
		 * If we didn't find the box, then that means we need to create it and add it to
		 * our scene.
		 */
		if (thisBox == null) {
			box = new VBox();
			box.setId(id);

			box.prefWidthProperty().bind(mainBox.widthProperty());
			box.prefHeightProperty().bind(mainBox.heightProperty());

			/**
			 * Add the box into the pane although there is no element inside it. Subsequent
			 * calls will add data into.
			 */
			mainBox.getChildren().add(box);
			// StackPane.setAlignment(box, Pos.CENTER);
			box.setAlignment(Pos.CENTER);
			thisBox = box;
		}
		thisBox.getChildren().clear();
		return thisBox;
	}

	public static void processRandomUrl(MainWidget mainWidget, String url) {
		if (url == null || url.trim().isEmpty()) {
			return;
		}
		if (url.toLowerCase().contains("youtube")) {
			new DownloadMenu(mainWidget, ProcessConfiguration.getInstance()).processYouTubeVideoForm(url);
		} else if (url.toLowerCase().contains("tubitv")) {
			new DownloadMenu(mainWidget, ProcessConfiguration.getInstance()).processTubiTvVideoForm(url);
		} else {
			/**
			 * For generic url, we can use the TubiTv video form for now.
			 */
			new DownloadMenu(mainWidget, ProcessConfiguration.getInstance()).processTubiTvVideoForm(url);
		}
	}

	public static void addMainIcon(Stage stage) {
		stage.getIcons().add(new Image("/media/logo/media-download-icon.png"));
	}

}
