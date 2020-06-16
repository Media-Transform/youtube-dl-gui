/**
 * 
 */
package com.sunilsamuel.media.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

/**
 * @author web_w
 * @author Sunil Samuel (web@sunilsamuel.com)
 *
 */
public class LanguageBinding {
	/** the current selected Locale. */
	private static final ObjectProperty<Locale> locale;

	static {
		locale = new SimpleObjectProperty<>(Locale.ENGLISH);
		locale.addListener((observable, oldValue, newValue) -> {
			Locale.setDefault(newValue);
		});
	}

	public static void setLocale(Locale locale) {
		localeProperty().set(locale);
		Locale.setDefault(locale);
	}

	public static ObjectProperty<Locale> localeProperty() {
		return locale;
	}

	/**
	 * gets the string with the given key from the resource bundle for the current
	 * locale and uses it as first argument to MessageFormat.format, passing in the
	 * optional args and returning the result.
	 *
	 * @param key  message key
	 * @param args optional arguments for the message
	 * @return localized formatted string
	 */
	public static String get(final String key, final Object... args) {
		ResourceBundle bundle = ResourceBundle.getBundle("property/messages", locale.get());
		return MessageFormat.format(bundle.getString(key), args);
	}

	/**
	 * creates a String binding to a localized String for the given message bundle
	 * key
	 *
	 * @param key key
	 * @return String binding
	 */
	public static StringBinding createStringBinding(final String key, Object... args) {
		return Bindings.createStringBinding(() -> get(key, args), locale);
	}

	public static void bindTextProperty(StringProperty prop, final String key, final Object... args) {
		prop.bind(createStringBinding(key, args));
	}

}
