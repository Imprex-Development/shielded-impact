package dev.imprex.shieldedimpact.command.l10n;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import dev.imprex.shieldedimpact.api.l10n.PercentFormat;
import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;
import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguageArgument;

public record Language(Locale locale, ResourceBundle bundle, DateTimeFormatter dateFormat, NumberFormat numberFormat, PercentFormat percentFormat) implements ShieldedLanguage {

	@Override
	public String format(String key, ShieldedLanguageArgument<?>... args) {
		if (!this.bundle.containsKey(key)) {
			return key;
		}

		String message = this.bundle.getString(key);
		char[] array = message.toCharArray();

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			char letter = array[i];

			if (array[i] == '{' && array.length > i + 2 && array[i + 2] == '}') {	
				try {
					int index = Integer.valueOf(String.valueOf(array[i + 1]));

					i += 2;

					if (args.length > index) {
						builder.append(args[index]);
					}
				} catch (NumberFormatException e) {
				}
				continue;
			}

			builder.append(letter);
		}

		return builder.toString();
	}
}