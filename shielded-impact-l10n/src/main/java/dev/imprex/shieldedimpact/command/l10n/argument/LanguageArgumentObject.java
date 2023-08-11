package dev.imprex.shieldedimpact.command.l10n.argument;

import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;

public final class LanguageArgumentObject extends LanguageArgument<Object> {

	LanguageArgumentObject(Object value) {
		super(value);
	}

	@Override
	public String format(ShieldedLanguage language) {
		return this.value.toString();
	}
}