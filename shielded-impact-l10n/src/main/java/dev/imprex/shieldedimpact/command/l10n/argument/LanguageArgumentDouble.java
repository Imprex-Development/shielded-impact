package dev.imprex.shieldedimpact.command.l10n.argument;

import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;

public final class LanguageArgumentDouble extends LanguageArgument<Double> {

	LanguageArgumentDouble(Double value) {
		super(value);
	}

	@Override
	public String format(ShieldedLanguage language) {
		return language.numberFormat().format(this.value);
	}
}