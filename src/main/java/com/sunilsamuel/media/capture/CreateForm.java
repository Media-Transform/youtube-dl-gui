/**
 * 
 */
package com.sunilsamuel.media.capture;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sunilsamuel.media.util.LanguageBinding;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Put together a form with different types of nodes such as TextField,
 * ComboBox, and others. The caller can use this class to create a form
 * according to their requirement.
 * 
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class CreateForm {
	private final Dialog<Map<String, String>> dialog = new Dialog<Map<String, String>>();

	private Button okButton;
	private ButtonType okButtonType;
	private GridPane grid;

	private int gridRowIndex = 0;
	private List<Node> textFields = new ArrayList<Node>();

	private String[] validateIds = null;

	public CreateForm() {
		/**
		 * Set some of the default values for the dialog.
		 */
		dialog.setResizable(true);
		dialog.getDialogPane().setPrefWidth(600);
		dialog.getDialogPane().setCenterShape(true);
		Stage myStage = (Stage) dialog.getDialogPane().getScene().getWindow();
		MainWidget.addMainIcon(myStage);

		/**
		 * This is the grid, that will hold all of the different dialog pieces,
		 * including the text, textfield, and the combo.
		 */
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setId("dialog-grid");
	}

	/**
	 * Once all of the form elements are created and we are ready to present the
	 * form to the end user, this method will finalize the form and present it to
	 * the end user.
	 * 
	 * @return A map of results that the end user entered where the key is the id of
	 *         the field
	 */
	public Map<String, String> process() {
		/**
		 * Validate initially that there is data in the fields that are required to have
		 * values.
		 */
		boolean disable = false;
		for (Node field : textFields) {
			String key = field.getId();
			boolean valueExists;
			String value = getValueOfNode(field);
			valueExists = (value != null && !value.trim().isEmpty());
			if (validateIds != null) {
				if (keyInArray(validateIds, key) && !valueExists) {
					disable = true;
				}
			} else {
				if (!valueExists) {
					disable = true;
				}
			}
			okButton.setDisable(disable);
		}

		/**
		 * User created all of the elements in the form, now set it to the dialog.
		 */
		dialog.getDialogPane().setContent(grid);
		/**
		 * Process when the user hits the 'OK' button.
		 */
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType) {
				Map<String, String> rval = new HashMap<String, String>();
				for (Node field : textFields) {
					String value = getValueOfNode(field);
					rval.put(field.getId(), value);
				}
				return rval;
			}
			return null;
		});

		Optional<Map<String, String>> result = dialog.showAndWait();
		return (result.isPresent() ? result.get() : null);
	}

	public CreateForm setDialogTitle(String titleKey) {
		LanguageBinding.bindTextProperty(dialog.titleProperty(), titleKey);

		return this;
	}

	public CreateForm createSeparator() {
		Separator separator = new Separator();
		separator.setId("separator");
		separator.autosize();
		separator.setOrientation(Orientation.HORIZONTAL);
		separator.setPrefWidth(10);
		grid.add(separator, 0, gridRowIndex++, 2, 1);

		return this;
	}

	public CreateForm createCheckBox(String textKey, String id) {
		CheckBox cb = new CheckBox();

		cb.setId(id);
		textFields.add(cb);

		LanguageBinding.bindTextProperty(cb.textProperty(), textKey);
		grid.add(cb, 0, gridRowIndex++, 2, 1);

		return this;
	}

	public CreateForm createRadioButton(List<String> buttonKeys, String id) {
		ToggleGroup group = new ToggleGroup();

		/**
		 * Tile for the radio button.
		 */
		TilePane tilePane = new TilePane();
		tilePane.setId(id);
		textFields.add(tilePane);

		for (String buttonKey : buttonKeys) {
			String[] split = buttonKey.split(":");
			RadioButton button = new RadioButton();
			button.setUserData(split[1]);
			button.setToggleGroup(group);
			LanguageBinding.bindTextProperty(button.textProperty(), split[0]);
			tilePane.getChildren().add(button);
		}
		group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			if (group.getSelectedToggle() != null) {
				Toggle selected = group.getSelectedToggle();
				tilePane.setUserData(selected.getUserData());
			}
		});
		grid.add(tilePane, 0, gridRowIndex++, 2, 1);
		return this;
	}

	/**
	 * Create buttons, one for 'OK' and the other for 'Cancel'. The OK button title
	 * can be different and is based on the parameter to this method, title. Then
	 * disable it, so that we can enable it once the user enters something into the
	 * field. This is a way to make sure that the user cannot hit "OK", unless there
	 * is something in the field.
	 * 
	 * @param title
	 */
	public CreateForm setButtonTitle(String okButtonKey, String cancelButtonKey) {
		/**
		 * First create the button type to add as a button.
		 */
		okButtonType = new ButtonType(LanguageBinding.get(okButtonKey), ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType(LanguageBinding.get(cancelButtonKey), ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

		/**
		 * Get the button from the button type just created.
		 */
		okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
		Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancelButtonType);
		LanguageBinding.bindTextProperty(okButton.textProperty(), okButtonKey);
		LanguageBinding.bindTextProperty(cancelButton.textProperty(), cancelButtonKey);

		okButton.setDisable(true);
		return this;
	}

	/**
	 * Create a message area that places the 'message' string into the field. This
	 * is just some information regarding the input. This will span 2 columns and 1
	 * row.
	 * 
	 * @param message
	 */
	public CreateForm createTextMessage(String messageKey) {
		Text messageText = new Text();
		messageText.setId("main-widget-text-message");
		messageText.wrappingWidthProperty().bind(dialog.widthProperty().subtract(80d));

		LanguageBinding.bindTextProperty(messageText.textProperty(), messageKey);

		grid.add(messageText, 0, gridRowIndex++, 2, 1);
		return this;
	}

	/**
	 * Create a TextField that will allow the user to enter data into it. It will
	 * create a prompt using the 'promptText', set the id of the of the textField as
	 * 'id', and the default text as 'defaultText'. If 'defaultText' is null, then
	 * it will not set any default text. It will use the 'labelText' to define the
	 * label.
	 * 
	 * @param labelText
	 * @param promptText
	 * @param id
	 * @param defaultText
	 */
	public CreateForm createTextField(String labelTextKey, String promptTextKey, String id, String defaultText) {
		TextField textField = new TextField();
		textField.setPromptText(LanguageBinding.get(promptTextKey));
		textField.setId(id);
		textField.setPrefWidth(400);
		if (defaultText != null) {
			textField.setText(defaultText);
		}

		textFields.add(textField);
		Label label = new Label();
		LanguageBinding.bindTextProperty(label.textProperty(), labelTextKey);

		grid.add(label, 0, gridRowIndex);
		grid.add(textField, 1, gridRowIndex++);
		return this;
	}

	/**
	 * Create a text field, where when the user focus on this field, it will open a
	 * directory chooser for the user to select a directory.
	 * 
	 * @param labelText
	 * @param promptText
	 * @param id
	 * @param defaultText
	 */
	public CreateForm createTextFieldDirChooser(String labelTextKey, String promptTextKey, String id,
			String defaultText) {
		/**
		 * Create the text field where the data will be written to by the dir chooser.
		 */
		TextField textField = new TextField();
		textField.setPromptText(LanguageBinding.get(promptTextKey));

		textField.setId(id);
		if (defaultText != null) {
			textField.setText(defaultText);
		}

		textFields.add(textField);

		/**
		 * Create the listener on this text field so that when someone focus on it, it
		 * will ask the user to pick a directory.
		 */
		textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			/**
			 * We only want to run this event if the focus is true not false.
			 */
			if (newVal == true) {
				/**
				 * The "running" is to prevent multiple directory chooser from opening up if the
				 * user clicks on the textfield multiple times.
				 */
				if (textField.getProperties().get("running") == null) {
					textField.getProperties().put("running", Boolean.TRUE);
					DirectoryChooser dirChooser = new DirectoryChooser();
					// dirChooser.setTitle(labelText);
					LanguageBinding.bindTextProperty(dirChooser.titleProperty(), labelTextKey);
					File file = new File(defaultText);
					dirChooser.setInitialDirectory(file);
					File pickedDirectory = dirChooser.showDialog(null);
					if (pickedDirectory != null) {
						textField.setText(pickedDirectory.getAbsolutePath());
					}
					textField.getProperties().remove("running");
				}
			}
		});

		Label label = new Label();
		LanguageBinding.bindTextProperty(label.textProperty(), labelTextKey);
		grid.add(label, 0, gridRowIndex);
		grid.add(textField, 1, gridRowIndex++);
		return this;
	}

	public CreateForm createTextFieldFileChooser(String labelTextKey, String promptTextKey, String id,
			String defaultText) {
		return createTextFieldFileChooser(labelTextKey, promptTextKey, id, defaultText, false);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public CreateForm createTextFieldFileChooser(String labelTextKey, String promptTextKey, String id,
			String defaultText, boolean multiple) {

		/**
		 * Create the text field where the data will be written to by the dir chooser.
		 */
		TextField textField = new TextField();
		textField.setPromptText(LanguageBinding.get(promptTextKey));

		textField.setId(id);
		if (defaultText != null) {
			textField.setText(defaultText);
		}

		textFields.add(textField);

		/**
		 * Create the listener on this text field so that when someone focus on it, it
		 * will ask the user to pick a directory.
		 */
		textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			/**
			 * We only want to run this event if the focus is true not false.
			 */
			if (newVal == true) {
				/**
				 * The "running" is to prevent multiple directory chooser from opening up if the
				 * user clicks on the textfield multiple times.
				 */
				if (textField.getProperties().get("running") == null) {
					textField.getProperties().put("running", Boolean.TRUE);
					FileChooser fileChooser = new FileChooser();
					LanguageBinding.bindTextProperty(fileChooser.titleProperty(), labelTextKey);
					if (defaultText != null) {
						String firstFile = defaultText;
						if (defaultText.contains(",")) {
							firstFile = defaultText.replaceAll("[\\[\\]]", "");
							firstFile = firstFile.split(",")[0];
						}
						File file = new File(firstFile);
						if (file.isDirectory()) {
							fileChooser.setInitialDirectory(file);
						} else {
							fileChooser.setInitialDirectory(file.getParentFile());
						}
					}
					Object pickedDirectory;
					if (multiple) {
						pickedDirectory = fileChooser.showOpenMultipleDialog(null);
						if (pickedDirectory != null) {
							textField.setText(pickedDirectory.toString());
						}
					} else {
						pickedDirectory = (File) fileChooser.showOpenDialog(null);
						if (pickedDirectory != null) {
							textField.setText(((File) pickedDirectory).getAbsolutePath());
						}
					}
					textField.getProperties().remove("running");
				}
			}
		});

		Label label = new Label();
		LanguageBinding.bindTextProperty(label.textProperty(), labelTextKey);
		grid.add(label, 0, gridRowIndex);
		grid.add(textField, 1, gridRowIndex++);
		return this;
	}

	/**
	 * Create a comboBox, which is a dropdown list of options. The list of options
	 * available to the user comes from the options list. The initial value the user
	 * sees from the list is defined by the 'defaultOptionIndex'.
	 * 
	 * @param labelText
	 * @param options
	 * @param defaultOptionIndex
	 */
	public CreateForm createComboBox(String labelTextKey, String id, String[] options, int defaultOptionIndex) {
		ObservableList<String> obOptions = FXCollections.observableArrayList(options);

		ComboBox<String> combo = new ComboBox<String>(obOptions);
		combo.setId(id);
		combo.getSelectionModel().select(defaultOptionIndex);

		textFields.add(combo);

		Label label = new Label();
		LanguageBinding.bindTextProperty(label.textProperty(), labelTextKey);

		grid.add(label, 0, gridRowIndex);
		grid.add(combo, 1, gridRowIndex++);
		return this;
	}

	/**
	 * Validate all of the TextField fields to have a value. If any one of the text
	 * fields do not have a value, then the 'OK' button will not be enabled.
	 * Whenever any one of the field content is changed, this event will trigger and
	 * validate all of them.
	 */
	public CreateForm validateFieldsListener() {
		/**
		 * Add the listener for each text field. We only need to add listeners to the
		 * text field.
		 */
		for (Node field : textFields) {
			if (field instanceof TextField) {
				TextField textField = (TextField) field;
				textField.textProperty().addListener((observable, oldValue, newValue) -> {
					boolean enable = true;
					for (Node f : textFields) {
						if (f instanceof TextField) {
							String value = getValueOfNode(f);
							if (value == null || value.trim().isEmpty()) {
								enable = false;
							}
						}
					}
					okButton.setDisable(!enable);
				});
			}
		}

		return this;
	}

	/**
	 * Validate TextField fields to have a value. If any one of the text fields with
	 * one of the id in 'ids' do not have a value, then the 'OK' button will not be
	 * enabled. Whenever any one of the field content is changed, this event will
	 * trigger and validate all of them.
	 */
	public CreateForm validateFieldsListener(String[] ids) {
		validateIds = ids;
		for (Node field : textFields) {
			if (field instanceof TextField) {
				TextField textField = (TextField) field;
				textField.textProperty().addListener((observable, oldValue, newValue) -> {
					boolean enable = true;
					for (Node f : textFields) {
						if (f instanceof TextField) {
							String value = getValueOfNode(f);
							if (keyInArray(ids, f.getId())) {
								if (value == null || value.trim().isEmpty()) {
									enable = false;
								}
							}
						}
					}
					okButton.setDisable(!enable);
				});
			}
		}
		return this;
	}

	private boolean keyInArray(String[] array, String key) {
		if (array == null || array.length == 0) {
			return false;
		}
		for (String s : array) {
			if (s.equals(key)) {
				return true;
			}
		}
		return false;
	}

	private String getValueOfNode(Node node) {
		if (node == null) {
			return null;
		}
		if (node instanceof TextField) {
			TextField f = (TextField) node;
			return (f.getText() == null ? null : f.getText().trim());
		} else if (node instanceof ComboBox) {
			ComboBox<?> f = (ComboBox<?>) node;
			return (f.getValue() == null ? null : ((String) f.getValue()).trim());
		} else if (node instanceof CheckBox) {
			CheckBox cb = (CheckBox) node;
			return (cb.isSelected() ? "true" : "false");
		} else if (node instanceof TilePane) {
			TilePane tb = (TilePane) node;
			return (String) tb.getUserData();
		}
		return null;
	}

}
