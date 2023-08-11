package dev.imprex.shieldedimpact.command.l10n.argument;

import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;

public final class LanguageArgumentLong extends LanguageArgument<Long> {

	LanguageArgumentLong(Long value) {
		super(value);
	}

	@Override
	public String format(ShieldedLanguage language) {
		return language.numberFormat().format(this.value);
	}
}