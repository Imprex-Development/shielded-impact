package dev.imprex.shieldedimpact.api.l10n;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public interface ShieldedLanguage {

	String format(String key, ShieldedLanguageArgument<?>... args);

	Locale locale();

	ResourceBundle bundle();

	DateTimeFormatter dateFormat();

	NumberFormat numberFormat();

	PercentFormat percentFormat();
}
