package dev.imprex.shieldedimpact.command.l10n.argument;

import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;

public final class LanguageArgumentPercent extends LanguageArgument<Integer> {

	LanguageArgumentPercent(Integer value) {
		super(value);
	}

	@Override
	public String format(ShieldedLanguage language) {
		return language.percentFormat().format(this.value);
	}
}